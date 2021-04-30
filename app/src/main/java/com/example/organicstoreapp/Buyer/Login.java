package com.example.organicstoreapp.Buyer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organicstoreapp.Admin.AdminHome;
import com.example.organicstoreapp.Model.Users;
import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    Dialog myDialog;
    private EditText loginphone,loginpass;
    private Button loginbtn,selWA,selPhone,selApp;
    private CheckBox chkRememberMe;
    private TextView tvadmin,tvnotAdmin,cutdialogbtn,forgetpassLink;
    private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDialog=new Dialog(this);
        loginphone=findViewById(R.id.phonenumberlogin);
        loginpass=findViewById(R.id.passwordlogin);
        loginbtn=findViewById(R.id.loginbtnlogin);
        tvadmin=findViewById(R.id.admin_panel_link);
        tvnotAdmin=findViewById(R.id.not_admin_panel_link);
        chkRememberMe=findViewById(R.id.remembermechk);
        forgetpassLink=findViewById(R.id.forgetpass);
        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }

        });
        forgetpassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, ResetPassword.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        tvadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText("Login Admin");
                tvadmin.setVisibility(View.INVISIBLE);
                tvnotAdmin.setVisibility(View.VISIBLE);
                parentDbName ="Admins";
            }
        });
        tvnotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText("Login");
                tvadmin.setVisibility(View.VISIBLE);
                tvnotAdmin.setVisibility(View.INVISIBLE);
                parentDbName ="Users";
            }
        });

    }
    private void LoginUser() {
        String PHONE=loginphone.getText().toString().trim();
        String PASS=loginpass.getText().toString().trim();

        if(TextUtils.isEmpty(PHONE)){
            loginphone.setError("Please enter phone number");
            loginphone.requestFocus();
        }
        else if(TextUtils.isEmpty(PASS)){
            loginpass.setError("Please enter Password");
            loginpass.requestFocus();
        }else {
            AllowAccessToAccount(PHONE,PASS);
        }


    }

    private void AllowAccessToAccount(final String phone,final String pass) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists()){
                    final Users usersData=snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(pass)){
                            if(parentDbName.equals("Admins")){
                                Toast.makeText(Login.this,"Welcome Admin you are Logged in Successfully..",Toast.LENGTH_SHORT).show();

                                Intent mtol=new Intent(Login.this, AdminHome.class);
                                startActivity(mtol);
                            }else if(parentDbName.equals("Users")) {
                                    myDialog.setContentView(R.layout.custompopup);
                                    cutdialogbtn=(TextView) myDialog.findViewById(R.id.cutbtn);
                                    selWA=(Button) myDialog.findViewById(R.id.selectWhatsApp);
                                    selPhone=(Button) myDialog.findViewById(R.id.selectPhone);
                                    selApp=(Button) myDialog.findViewById(R.id.selectApp);

                                    selWA.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i=getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                                            startActivity(i);
                                        }
                                    });
                                    selApp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(Login.this,"Logged in Successfully..",Toast.LENGTH_SHORT).show();
                                            Intent mtol=new Intent(Login.this, Home.class);
                                            Prevalent.CurrentOnlineUser=usersData;
                                            startActivity(mtol);

                                        }
                                    });
                                    selPhone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(Intent.ACTION_DIAL);
                                            startActivity(intent);
                                        }
                                    });
                                    cutdialogbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });
                                    myDialog.show();
                            }
                        }else {
                            Toast.makeText(Login.this,"Password is incorrect.",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(Login.this,"Account with"+phone+"number do not exists.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}