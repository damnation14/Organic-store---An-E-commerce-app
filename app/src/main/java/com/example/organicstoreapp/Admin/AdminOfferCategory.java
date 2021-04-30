package com.example.organicstoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.organicstoreapp.R;

public class AdminOfferCategory extends AppCompatActivity {
    private ImageView wheat,dals,dryfruits,spices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_offer_category);
        wheat=findViewById(R.id.IV_wheat);
        dals=findViewById(R.id.IV_dals);
        dryfruits =findViewById(R.id.IV_dryfruits);
        spices=findViewById(R.id.IV_spices);

        wheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminOfferCategory.this, AdminAddNewOfferProduct.class);
                i.putExtra("category","Wheat");
                startActivity(i);
            }
        });

        dals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminOfferCategory.this,AdminAddNewOfferProduct.class);
                i.putExtra("category","Dals");
                startActivity(i);
            }
        });

        dryfruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminOfferCategory.this,AdminAddNewOfferProduct.class);
                i.putExtra("category","Dryfruits");
                startActivity(i);
            }
        });

        spices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminOfferCategory.this,AdminAddNewOfferProduct.class);
                i.putExtra("category","Spices");
                startActivity(i);
            }
        });
    }
}