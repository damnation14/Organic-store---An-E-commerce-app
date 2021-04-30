package com.example.organicstoreapp.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLogin extends AppCompatActivity {
    private Button loginrSellerBtn;
    private EditText emailInput,passInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth=FirebaseAuth.getInstance();

       loginrSellerBtn=findViewById(R.id.seller_login_btn);
        emailInput=findViewById(R.id.seller_login_email);
        passInput=findViewById(R.id.seller_login_password);

        loginrSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSeller();
            }
        });
    }

    private void loginSeller()
    {
        final String email=emailInput.getText().toString();
        final String pass=passInput.getText().toString();


        if(!email.equals("") && !pass.equals("")) {
            mAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(SellerLogin.this, SellerHome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
        }else {
            Toast.makeText(SellerLogin.this,"Please complete Login form.",Toast.LENGTH_SHORT).show();

        }
        }
}