package com.example.organicstoreapp.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organicstoreapp.Buyer.Login;
import com.example.organicstoreapp.Buyer.MainActivity;
import com.example.organicstoreapp.R;
import com.google.android.gms.auth.api.signin.internal.HashAccumulator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistration extends AppCompatActivity {
    private Button sellerLoginBeginbtn;
    private EditText nameInput,phoneInput,emailInput,passInput,addressInput;
    private Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);


        mAuth=FirebaseAuth.getInstance();
        sellerLoginBeginbtn=findViewById(R.id.seller_already_hvAc_btn);
        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passInput=findViewById(R.id.seller_password);
        addressInput=findViewById(R.id.seller_address);
        registerBtn=findViewById(R.id.seller_reg_btn);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSeller();
            }
        });

       sellerLoginBeginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mtol=new Intent(SellerRegistration.this,SellerLogin.class);
                startActivity(mtol);
            }
        });
    }

    private void registerSeller()
    {
        final String name=nameInput.getText().toString();
        final String phone=phoneInput.getText().toString();
        final String email=emailInput.getText().toString();
        final String pass=passInput.getText().toString();
        final String address=addressInput.getText().toString();

            if(!name.equals("")&& !phone.equals("") && !email.equals("") && !pass.equals("") && !address.equals("")){
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final DatabaseReference rootRef;
                                rootRef= FirebaseDatabase.getInstance().getReference();
                                String sid=mAuth.getCurrentUser().getUid();
                                HashMap<String,Object> sellerMap=new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);
                                sellerMap.put("password",pass);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(SellerRegistration.this, "You are registered Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(SellerRegistration.this, SellerHome.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(SellerRegistration.this,"Error in Seller Regstration",Toast.LENGTH_SHORT).show();
                                                }
                                                }
                                        });


                            }
                        }
                    });
            }else{
            Toast.makeText(SellerRegistration.this,"Please complete registration form.",Toast.LENGTH_SHORT).show();
        }
    }
}