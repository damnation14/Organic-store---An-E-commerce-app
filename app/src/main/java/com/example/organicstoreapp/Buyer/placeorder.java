package com.example.organicstoreapp.Buyer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class  placeorder extends AppCompatActivity {

    Dialog myDialog;
    private EditText user_name, phone_number, address, pin_code;
    private Button confirm,checkEmail,continueOrders;
    private String price="";
    private TextView cutdialogbtn1;
    private Spinner dropdown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        myDialog=new Dialog(this);
        user_name=(EditText) findViewById(R.id.username);
        phone_number=(EditText) findViewById(R.id.phonenumber);
        address=(EditText) findViewById(R.id.address);
        //pin_code=(EditText) findViewById(R.id.pincode);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Select Pincode","1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        confirm=(Button)findViewById(R.id.confirmbtn);

        price=getIntent().getStringExtra("Total Price");
        Toast.makeText(this,price,Toast.LENGTH_SHORT).show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                whenclicked();
            }
        });
    }

    private void whenclicked()
    {
        if(TextUtils.isEmpty(user_name.getText()))
        {
            Toast.makeText(this,"Username not entered",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone_number.getText()))
        {
            Toast.makeText(this,"Phone number not entered",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            Toast.makeText(this,"Address not entered",Toast.LENGTH_SHORT).show();
        }
        else if(dropdown.getSelectedItem()=="Select Pincode")
        {
            Toast.makeText(this,"Pin code not entered",Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmorder();
        }
    }
    private void confirmorder()
    {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.CurrentOnlineUser.getPhone());
        HashMap<String, Object> ordermap= new HashMap<>();
        ordermap.put("Phone",phone_number.getText().toString());
        ordermap.put("Name",user_name.getText().toString());
        ordermap.put("Total_Amount",price.toString());
        ordermap.put("Address",address.getText().toString());
        ordermap.put("Pincode",dropdown.getSelectedItem());
        ordermap.put("date",saveCurrentDate);
        ordermap.put("time",saveCurrentTime);
        ordermap.put("Order_Status","Not shipped");

        databaseReference.updateChildren(ordermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.CurrentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                         Toast.makeText(placeorder.this,"Order placed successfully",Toast.LENGTH_SHORT).show();
                                         myDialog.setContentView(R.layout.ordercustompopup);
                                        cutdialogbtn1=(TextView) myDialog.findViewById(R.id.cutbtn1);
                                        checkEmail=(Button) myDialog.findViewById(R.id.selectemail);
                                        continueOrders=(Button) myDialog.findViewById(R.id.selectShopping);

                                        checkEmail.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent mailclient=getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                                              startActivity(mailclient);
                                            }
                                        });
                                        continueOrders.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                               // Toast.makeText(placeorder.this,"Order placed successfully",Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(placeorder.this, Home.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        cutdialogbtn1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myDialog.dismiss();
                                            }
                                        });
                                        myDialog.show();
                                    }

                                }
                            });
                }

            }
        });


    }
}