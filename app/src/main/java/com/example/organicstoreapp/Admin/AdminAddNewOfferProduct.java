package com.example.organicstoreapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.organicstoreapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewOfferProduct extends AppCompatActivity {
    private String CategoryName1,Description1,Price1,Pname1,saveCurrentDate1,saveCurrentTime1,productRandomKey1,downloadImageUrl1;
    private Button add_new_prod_btn1;
    private ImageView Input_prod_image1;
    private EditText Input_product_name1,Input_product_desc1,Input_product_price1;
    private static final int GalleryPick1=1;
    private Uri ImageUri1;
    private StorageReference productImagesRef1;
    private DatabaseReference productRef1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_offer_product);

        Input_prod_image1=findViewById(R.id.select_product_image);
        Input_product_name1=findViewById(R.id.product_name);
        Input_product_desc1=findViewById(R.id.product_description);
        Input_product_price1=findViewById(R.id.product_price);
        add_new_prod_btn1=findViewById(R.id.add_new_prod_button);

        add_new_prod_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();

            }
        });

        Input_prod_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });


        CategoryName1=getIntent().getExtras().get("category").toString();
        productImagesRef1= FirebaseStorage.getInstance().getReference().child("Product Offer Images");
        productRef1= FirebaseDatabase.getInstance().getReference().child("offer products");


    }

    private void ValidateProductData() {
        Description1=Input_product_desc1.getText().toString().trim();
        Price1=Input_product_price1.getText().toString().trim();
        Pname1=Input_product_name1.getText().toString().trim();


        if(ImageUri1==null){
            Toast.makeText(this,"Product image is mandatory.",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Description1)){
            Toast.makeText(this,"Please write product Description.",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(Price1)){
            Toast.makeText(this,"Please write product Price.",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(Pname1)){
            Toast.makeText(this,"Please write product name.",Toast.LENGTH_SHORT).show();

        }else {

            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate1=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime1=currentTime.format(calendar.getTime());

        productRandomKey1=saveCurrentDate1+saveCurrentTime1;

        final StorageReference filePath=productImagesRef1.child((ImageUri1.getLastPathSegment()+productRandomKey1+".jpg"));
        final UploadTask uploadTask=filePath.putFile(ImageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewOfferProduct.this,"Error:"+message,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewOfferProduct.this,"Product Image Uploaded Successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl1=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl1=task.getResult().toString();
                            Toast.makeText(AdminAddNewOfferProduct.this,"Getting Product Image URL Successfully ",Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });



    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey1);
        productMap.put("date",saveCurrentDate1);
        productMap.put("time",saveCurrentTime1);
        productMap.put("description",Description1);
        productMap.put("image",downloadImageUrl1);
        productMap.put("category",CategoryName1);
        productMap.put("price",Price1);
        productMap.put("pname",Pname1);

        productRef1.child(productRandomKey1).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent im=new Intent(AdminAddNewOfferProduct.this, AdminOfferCategory.class);
                    startActivity(im);
                    Toast.makeText(AdminAddNewOfferProduct.this,"Product is added successfully",Toast.LENGTH_SHORT).show();
                }else {
                    String mes=task.getException().toString();
                    Toast.makeText(AdminAddNewOfferProduct.this,"Error:"+mes,Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void OpenGallery() {

        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick1 && resultCode==RESULT_OK && data!=null){
            ImageUri1=data.getData();
            Input_prod_image1.setImageURI(ImageUri1);

        }
    }
}