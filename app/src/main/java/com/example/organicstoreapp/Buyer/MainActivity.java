package com.example.organicstoreapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organicstoreapp.Model.Users;
import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.Sellers.SellerHome;
import com.example.organicstoreapp.Sellers.SellerRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinnow,login;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sellerBegin=findViewById(R.id.seller_begin);
        joinnow=findViewById(R.id.join_now_btn);
        login=findViewById(R.id.loginbtn);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mtol=new Intent(MainActivity.this, Login.class);
                startActivity(mtol);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mtos=new Intent(MainActivity.this, SellerRegistration.class);
                startActivity(mtos);
            }
        });

        joinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mtor=new Intent(MainActivity.this, Register.class);
                startActivity(mtor);
            }
        });

        String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPhoneKey !="" && UserPasswordKey !=""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            Intent intent=new Intent(MainActivity.this, SellerHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String phone, final String pass) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users usersData=snapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(pass)){
                            Toast.makeText(MainActivity.this,"Logged in Successfully..",Toast.LENGTH_SHORT).show();
                            Intent mtol=new Intent(MainActivity.this, Home.class);
                            Prevalent.CurrentOnlineUser=usersData;
                            startActivity(mtol);
                        }else {
                            Toast.makeText(MainActivity.this,"Password is incorrect.",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(MainActivity.this,"Account with"+phone+"number do not exists.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}