package com.example.organicstoreapp.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.organicstoreapp.Sellers.SellerProductCategory;
import com.example.organicstoreapp.Admin.AdminOfferCategory;
import com.example.organicstoreapp.R;

public class SelectnewOrOffer extends AppCompatActivity {
    private Button btnoff,btnnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectnew_or_offer);
        btnnew=findViewById(R.id.newbtn);
        btnoff=findViewById(R.id.offerbtn);
        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(SelectnewOrOffer.this, SellerProductCategory.class);
                startActivity(in);
            }
        });
        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(SelectnewOrOffer.this, AdminOfferCategory.class);
                startActivity(in);
            }
        });
    }
}