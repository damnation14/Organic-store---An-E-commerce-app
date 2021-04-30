package com.example.organicstoreapp.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.organicstoreapp.R;

public class SellerProductCategory extends AppCompatActivity {
    private ImageView wheat,dals,dryfruits,spices,oils,producedfood,rice,saltsugar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

         wheat=findViewById(R.id.IV_wheat);
        dals=findViewById(R.id.IV_dals);
       dryfruits =findViewById(R.id.IV_dryfruits);
        spices=findViewById(R.id.IV_spices);
         oils=findViewById(R.id.IV_oils);
          producedfood=findViewById(R.id.IV_producedfood);
          rice =findViewById(R.id.IV_rice);
          saltsugar =findViewById(R.id.IV_saltsugar);



          wheat.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                  i.putExtra("category","Wheat");
                  startActivity(i);
              }
          });

        dals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Dals");
                startActivity(i);
            }
        });

        dryfruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Dryfruits");
                startActivity(i);
            }
        });

        spices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Spices");
                startActivity(i);
            }
        });

        oils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Oils");
                startActivity(i);
            }
        });

        producedfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Produced Food");
                startActivity(i);
            }
        });

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Rice");
                startActivity(i);
            }
        });

        saltsugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SellerProductCategory.this, SellerAddNewProduct.class);
                i.putExtra("category","Salt Sugar");
                startActivity(i);
            }
        });

    }
}