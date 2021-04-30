package com.example.organicstoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.organicstoreapp.Buyer.Home;
import com.example.organicstoreapp.Buyer.MainActivity;
import com.example.organicstoreapp.Model.AdminOrders;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.Sellers.SellerAddNewProduct;

public class AdminHome extends AppCompatActivity {
     private Button logoutbtn,chckorderbtn,maintainProdbtn,checkApprovebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

                logoutbtn=findViewById(R.id.admin_logout_btn);
        maintainProdbtn=findViewById(R.id.Admin_maintain_btn);
        chckorderbtn=findViewById(R.id.check_order_btn);
        checkApprovebtn=findViewById(R.id.Approve_product_btn);




        maintainProdbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(AdminHome.this, Home.class);
                  intent.putExtra("Admin","Admin");
                  startActivity(intent);
              }
          });

          logoutbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(AdminHome.this, SellerAddNewProduct.class);
                  startActivity(intent);

              }
          });


          chckorderbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(AdminHome.this, AdminNewOrders.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
                  finish();

              }
          });

        checkApprovebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHome.this, AdminCheckNewProducts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }
}