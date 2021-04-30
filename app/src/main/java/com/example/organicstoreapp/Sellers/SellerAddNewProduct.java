package com.example.organicstoreapp.Sellers;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class SellerAddNewProduct extends AppCompatActivity {
private String CategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
private Button add_new_prod_btn;
private ImageView Input_prod_image;
private EditText Input_product_name,Input_product_desc,Input_product_price;
private static final int GalleryPick=1;
private Uri ImageUri;
private StorageReference productImagesRef;
private DatabaseReference productRef,sellersRef;
private String sName,sAddress,sPhone,sEmail,sId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        sellersRef=FirebaseDatabase.getInstance().getReference().child("Sellers");

        Input_prod_image=findViewById(R.id.select_product_image);
        Input_product_name=findViewById(R.id.product_name);
        Input_product_desc=findViewById(R.id.product_description);
        Input_product_price=findViewById(R.id.product_price);
        add_new_prod_btn=findViewById(R.id.add_new_prod_button);

        add_new_prod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();

            }
        });

        Input_prod_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });


        CategoryName=getIntent().getExtras().get("category").toString();
        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef=FirebaseDatabase.getInstance().getReference().child("products");

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    sName=snapshot.child("name").getValue().toString();
                    sAddress=snapshot.child("address").getValue().toString();
                   sPhone =snapshot.child("phone").getValue().toString();
                   sId=snapshot.child("sid").getValue().toString();
                   sEmail=snapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ValidateProductData() {
        Description=Input_product_desc.getText().toString().trim();
        Price=Input_product_price.getText().toString().trim();
        Pname=Input_product_name.getText().toString().trim();


        if(ImageUri==null){
            Toast.makeText(this,"Product image is mandatory.",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Please write product Description.",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this,"Please write product Price.",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(Pname)){
            Toast.makeText(this,"Please write product name.",Toast.LENGTH_SHORT).show();

        }else {

            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
       saveCurrentTime=currentTime.format(calendar.getTime());

       productRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filePath=productImagesRef.child((ImageUri.getLastPathSegment()+productRandomKey+".jpg"));
        final UploadTask uploadTask=filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(SellerAddNewProduct.this,"Error:"+message,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
           Toast.makeText(SellerAddNewProduct.this,"Product Image Uploaded Successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       downloadImageUrl=task.getResult().toString();
                       Toast.makeText(SellerAddNewProduct.this,"Getting Product Image URL Successfully ",Toast.LENGTH_SHORT).show();

                   saveProductInfoToDatabase();
                   }
                    }
                });
            }
        });



    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",Pname);

        productMap.put("sellerName",sName);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sid",sId);
        productMap.put("productState","Not Approved");

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful()){
               Intent im=new Intent(SellerAddNewProduct.this, SellerHome.class);
               startActivity(im);
               Toast.makeText(SellerAddNewProduct.this,"Product is added successfully",Toast.LENGTH_SHORT).show();
           }else {
               String mes=task.getException().toString();
               Toast.makeText(SellerAddNewProduct.this,"Error:"+mes,Toast.LENGTH_SHORT).show();
           }
            }
        });



    }

    private void OpenGallery() {

        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri=data.getData();
            Input_prod_image.setImageURI(ImageUri);

        }
    }

}