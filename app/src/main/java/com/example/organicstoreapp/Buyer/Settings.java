package com.example.organicstoreapp.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullnametxt,userphonetxt,addresstxt,passwordtxt;
    private TextView profilechngbtn,closetxtbtn,updatetxtbtn;
    private Button setSecurityQuestions;

    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageprofilepicRef;
    private String checker="";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageprofilepicRef= FirebaseStorage.getInstance().getReference().child("Profile picture");

        profileImageView=findViewById(R.id.setting_profile_image);
        profilechngbtn=findViewById(R.id.profile_img_change_btn);
        fullnametxt=findViewById(R.id.settings_fullname);
        userphonetxt=findViewById(R.id.settings_phone_number);
        addresstxt=findViewById(R.id.settings_address);
        closetxtbtn=findViewById(R.id.close_settingsbtn);
        updatetxtbtn=findViewById(R.id.update_settingsbtn);
        passwordtxt=findViewById(R.id.settings_password);
        setSecurityQuestions=findViewById(R.id.security_question_btn);


        userInfoDisplay(profileImageView,fullnametxt,userphonetxt,addresstxt,passwordtxt);

        closetxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSecurityQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this, ResetPassword.class);
                intent.putExtra("check","settings");
                startActivity(intent);

            }
        });

        updatetxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }else {
                    updateOnlyUserInfo();
                }
            }
        });
        profilechngbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="clicked";

                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(Settings.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data !=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }else {
            Toast.makeText(Settings.this,"Error,Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this,Settings.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullnametxt.getText().toString());
        userMap.put("address",addresstxt.getText().toString());
        userMap.put("phone",userphonetxt.getText().toString());
        userMap.put("password",passwordtxt.getText().toString());
        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);
        startActivity(new Intent(Settings.this, Home.class));
        Toast.makeText(Settings.this,"Info. updated Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void uploadImage() {
        if(imageUri !=null){
            final StorageReference fileRef=storageprofilepicRef.child(Prevalent.CurrentOnlineUser.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                       Uri downloadUrl=task.getResult();

                       myUrl=downloadUrl.toString();
                       DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullnametxt.getText().toString());
                        userMap.put("address",addresstxt.getText().toString());
                        userMap.put("phone",userphonetxt.getText().toString());
                        userMap.put("password",passwordtxt.getText().toString());
                        userMap.put("image",myUrl);

                        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

                        startActivity(new Intent(Settings.this,Home.class));
                        Toast.makeText(Settings.this,"Info. updated Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(Settings.this,"Error...",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(Settings.this,"Image is not selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullnametxt.getText().toString())){
            fullnametxt.setError("Please enter Full Name");
            fullnametxt.requestFocus();
        }else if(TextUtils.isEmpty(addresstxt.getText().toString())){
            addresstxt.setError("Please enter Address");
            addresstxt.requestFocus();
        }else if(TextUtils.isEmpty(userphonetxt.getText().toString())){
            userphonetxt.setError("Please enter Full Name");
            userphonetxt.requestFocus();
        }else if(TextUtils.isEmpty(passwordtxt.getText().toString())){
            passwordtxt.setError("Please enter Full Name");
            passwordtxt.requestFocus();
        }else if(checker.equals("clicked")){
            uploadImage();
        }








    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullnametxt, final EditText userphonetxt, final EditText addresstxt, final EditText passwordtxt) {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("image").exists()){
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();
                        String pass=snapshot.child("password").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullnametxt.setText(name);
                        userphonetxt.setText(phone);
                        addresstxt.setText(address);
                        passwordtxt.setText(pass);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}