package com.example.organicstoreapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
private Button createacbtn;
private EditText etusename,etphone,etpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createacbtn=findViewById(R.id.registerbtn);
        etusename=findViewById(R.id.username_register);
        etphone=findViewById(R.id.phonenumberregister);
        etpass=findViewById(R.id.passwordregister);
        createacbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountMethod();
            }
        });
    }

    private void createAccountMethod() {
        String NAME=etusename.getText().toString().trim();
        String PHONENO=etphone.getText().toString().trim();
        String PASS=etpass.getText().toString().trim();

        if(TextUtils.isEmpty(NAME))
        {
           etusename.setError("Please enter Name");
            etusename.requestFocus();
        }
        else if(TextUtils.isEmpty(PHONENO)){
            etphone.setError("Please enter phone number");
            etphone.requestFocus();
        }
        else if(TextUtils.isEmpty(PASS)){
            etpass.setError("Please enter Password");
            etpass.requestFocus();
        }
        else {
            validatePhoneNumber(NAME,PHONENO,PASS);
        }



    }

    private void validatePhoneNumber(final String name, final String phoneno, final String pass)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phoneno).exists())){
                    HashMap<String,Object> userdatamap= new HashMap<>();
                    userdatamap.put("phone",phoneno);
                    userdatamap.put("password",pass);
                    userdatamap.put("name",name);

                    RootRef.child("Users").child(phoneno).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"Account Created Successfully!",Toast.LENGTH_SHORT).show();
                                Intent mtol=new Intent(Register.this, Login.class);
                                startActivity(mtol);
                            }else {
                                Toast.makeText(Register.this,"Error, Please Try Again!",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else {
                    Toast.makeText(Register.this,"This"+phoneno+"already exists!",Toast.LENGTH_SHORT).show();
                    Intent mtol=new Intent(Register.this, MainActivity.class);
                    startActivity(mtol);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}