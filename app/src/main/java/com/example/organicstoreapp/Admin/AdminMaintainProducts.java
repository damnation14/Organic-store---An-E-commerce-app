package com.example.organicstoreapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.organicstoreapp.R;
import com.example.organicstoreapp.Sellers.SellerProductCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProducts extends AppCompatActivity {
    private Button applyChngBtn,deletebtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        productID=getIntent().getStringExtra("pid");

        productsRef= FirebaseDatabase.getInstance().getReference().child("products").child(productID);


        setContentView(R.layout.activity_admin_maintain_products);
        applyChngBtn=findViewById(R.id.apply_changes_btn);
        name=findViewById(R.id.productnamentv_maintain);
        price=findViewById(R.id.productpricetv_maintain);
        description=findViewById(R.id.productdescriptiontv_maintain);
        imageView=findViewById(R.id.productimageIV_maintain);
        deletebtn=findViewById(R.id.delete_prod_btn);

        displaySpecificProductInfo();

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThisProduct();
            }
        });





        applyChngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();

            }
        });
    }

    private void deleteThisProduct() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(AdminMaintainProducts.this, SellerProductCategory.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProducts.this,"Product is deleted Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void applyChanges() {

        String pname=name.getText().toString();
        String pprice=price.getText().toString();
        String pdescription= description.getText().toString();

        if(pname.equals("")){
            Toast.makeText(this,"Write down product Name:",Toast.LENGTH_SHORT).show();
        }else if(pprice.equals("")){
            Toast.makeText(this,"Write down product Price:",Toast.LENGTH_SHORT).show();
        }else if(pdescription.equals("")) {
            Toast.makeText(this, "Write down product Description:", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String,Object> productMap=new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",pdescription);
            productMap.put("price",pprice);
            productMap.put("pname",pname);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProducts.this, "Changes applied Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainProducts.this, SellerProductCategory.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pname=snapshot.child("pname").getValue().toString();
                    String pprice=snapshot.child("price").getValue().toString();
                    String pdescription=snapshot.child("description").getValue().toString();
                    String pimage=snapshot.child("image").getValue().toString();
                    name.setText(pname);
                    price.setText(pprice);
                    description.setText(pdescription);
                    Picasso.get().load(pimage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}