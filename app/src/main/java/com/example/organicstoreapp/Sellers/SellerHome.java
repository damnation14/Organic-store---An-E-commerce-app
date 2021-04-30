package com.example.organicstoreapp.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.organicstoreapp.Admin.AdminCheckNewProducts;
import com.example.organicstoreapp.Buyer.MainActivity;
import com.example.organicstoreapp.Model.Products;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.ViewHolder.ItemViewHolder;
import com.example.organicstoreapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unApprovedProdRef;



    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
    = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent2=new Intent(SellerHome.this, SellerHome.class);
                    startActivity(intent2);

                    return true;
                case R.id.navigation_add:
                    Intent intent1=new Intent(SellerHome.this, SellerProductCategory.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intent=new Intent(SellerHome.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        unApprovedProdRef= FirebaseDatabase.getInstance().getReference().child("products");
        recyclerView=findViewById(R.id.seller_home_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        navView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);


                    // Passing each menu ID as a set of Ids because each
                    // menu should be considered as top level destinations.
//                    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                            R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
//                            .build();
//                    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
//        NavigationUI.setupWithNavController(navView,navController);
                }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unApprovedProdRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products,ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holderr, int i, @NonNull final Products model) {
                        holderr.textProductName.setText(model.getPname());
                        holderr.textProductDescription.setText(model.getDescription());
                        holderr.textProductprice.setText("Price="+model.getPrice()+"RS");
                        holderr.txtProductStatus.setText("State="+model.getProductState());
                        Picasso.get().load(model.getImage()).into(holderr.imageview);


                        holderr.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String productID=model.getPid();

                                CharSequence options1[]=new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHome.this);
                                builder.setTitle("Do you want to Delete this Product?");
                                builder.setItems(options1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0){
                                            deleteProduct(productID);
                                        }
                                        if(i==1){

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                        ItemViewHolder holder=new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productID)
    {
        unApprovedProdRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SellerHome.this,"Item has been Deleted Successfully!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
