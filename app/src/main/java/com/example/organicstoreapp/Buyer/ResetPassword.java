package com.example.organicstoreapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPassword extends AppCompatActivity {
    private String check="";
    private TextView pageTitle,titleQuestions;
    private EditText phoneNumber,quest1,quest2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");


        pageTitle=findViewById(R.id.page_title);
        titleQuestions=findViewById(R.id.title_questions);
        phoneNumber=findViewById(R.id.find_phone_number);
        quest1=findViewById(R.id.question_1);
        quest2=findViewById(R.id.question_2);
        verifyBtn=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);

        if(check.equals("settings")){
            pageTitle.setText("Set Questions");
            titleQuestions.setText("Please Set Answers For  the Following Security Questions.");
            verifyBtn.setText("Set");
            displayPreviousAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswers();

                }

            });

        }else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });

        }
    }

    private void verifyUser() {

        final String phone = phoneNumber.getText().toString();
        final String answer1 = quest1.getText().toString().toLowerCase();
        final String answer2 = quest2.getText().toString().toLowerCase();
        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(phone);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mPhone = snapshot.child("phone").getValue().toString();
                    if (snapshot.hasChild("Security Questions")) {
                        String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                        String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                        if (!ans1.equals(answer1)) {
                            Toast.makeText(ResetPassword.this, "Your 1st answer is incorrect.", Toast.LENGTH_SHORT).show();
                        } else if (!ans2.equals(answer2)) {
                            Toast.makeText(ResetPassword.this, "Your 2nd answer is incorrect.", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                            builder.setTitle("New Password");
                            final EditText newPassword = new EditText(ResetPassword.this);
                            newPassword.setHint("Write Password here..");
                            builder.setView(newPassword);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (!newPassword.getText().toString().equals("")) {
                                        ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ResetPassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(ResetPassword.this, Login.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                        }

                    } else {
                        Toast.makeText(ResetPassword.this, "You have not set security questions", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ResetPassword.this, "This phone Number not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }else {
            Toast.makeText(ResetPassword.this, "Please complete the form.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnswers(){
        String answer1=quest1.getText().toString().toLowerCase();
        String answer2=quest2.getText().toString().toLowerCase();

        if(quest1.equals("")&& quest2.equals("")){
            Toast.makeText(ResetPassword.this,"Please answer both questions",Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.CurrentOnlineUser.getPhone());


            HashMap<String,Object> userdatamap= new HashMap<>();
            userdatamap.put("answer1",answer1);
            userdatamap.put("answer2",answer2);


            ref.child("Security Questions").updateChildren(userdatamap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this,"You have set Security questions Successfully.",Toast.LENGTH_SHORT).show();
                                Intent in=new Intent(ResetPassword.this, Home.class);
                                startActivity(in);
                            }
                        }
                    });


        }

    }
    private void displayPreviousAnswers(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans1=snapshot.child("answer1").getValue().toString();
                    String ans2=snapshot.child("answer2").getValue().toString();

                    quest1.setText(ans1);
                    quest2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}