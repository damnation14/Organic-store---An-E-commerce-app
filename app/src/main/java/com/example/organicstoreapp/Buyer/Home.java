package com.example.organicstoreapp.Buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.organicstoreapp.Admin.AdminMaintainProducts;
import com.example.organicstoreapp.Model.OfferProducts;
import com.example.organicstoreapp.Model.Products;
import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.Sellers.SellerHome;
import com.example.organicstoreapp.ViewHolder.OfferProductViewHolder;
import com.example.organicstoreapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference productReference,offerprodref;
    private RecyclerView recyclerView,recyclerView1;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    private String type="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle != null){
            type=getIntent().getExtras().get("Admin").toString().trim();

        }



        productReference= FirebaseDatabase.getInstance().getReference().child("products");
        offerprodref=FirebaseDatabase.getInstance().getReference().child("offer products");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin")){
                    Intent in=new Intent(Home.this, Cart.class);
                    startActivity(in);
                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview= navigationView.getHeaderView(0);
        TextView usernametv=headerview.findViewById(R.id.User_Profile_nameTV);
        CircleImageView profileimageview=headerview.findViewById(R.id.user_profile_image);

//        if(!type.equals("Admin")){
//            usernametv.setText(Prevalent.CurrentOnlineUser.getName());
//            Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileimageview);
//        }

        recyclerView=findViewById(R.id.recycler_menu);
        System.out.println("........................................RECYCLER VIEW STARTED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        //layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //GridLayoutManager layoutManager1= new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false);
        //recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager(layoutManager);
        //LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1=findViewById(R.id.recycler_menu1);
        System.out.println("........................................RECYCLER VIEW STARTED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        recyclerView1.setHasFixedSize(true);
        layoutManager1=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView1.setLayoutManager(layoutManager1);



        
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ON START() CALLED");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productReference.orderByChild("productState").equalTo("Approved"), Products.class).build();


        FirebaseRecyclerOptions<OfferProducts> options1=
                new FirebaseRecyclerOptions.Builder<OfferProducts>().setQuery(offerprodref,OfferProducts.class).build();


        FirebaseRecyclerAdapter<OfferProducts, OfferProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OfferProducts, OfferProductViewHolder>
                (options1) {
            @Override
            protected void onBindViewHolder(@NonNull OfferProductViewHolder offerProductViewHolder, int i, @NonNull final OfferProducts offerProducts) {
            offerProductViewHolder.textProductName.setText(offerProducts.getPname());
            offerProductViewHolder.textProductDescription.setText(offerProducts.getDescription());
            offerProductViewHolder.textProductprice.setText("Price="+offerProducts.getPrice()+"RS");
            Picasso.get().load(offerProducts.getImage()).into(offerProductViewHolder.imageview);

                offerProductViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String productID=offerProducts.getPid();

                        CharSequence options1[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                        builder.setTitle("Do you want to Add this Product to Cart?");
                        builder.setItems(options1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                   // deleteProduct(productID);
                                }
                                if(i==1){

                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

//            @Override
//            public int getItemViewType(int position) {
//                System.out.println("_____________________________________________________________"+position+"___________________________________");
//                return super.getItemViewType(position);
//       }

            @NonNull
            @Override
            public OfferProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                OfferProductViewHolder holder=new OfferProductViewHolder(view);
                return holder;
            }
        };
        recyclerView1.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();




        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holderr, int i, @NonNull final Products model) {

                holderr.textProductName.setText(model.getPname());
                holderr.textProductDescription.setText(model.getDescription());
                holderr.textProductprice.setText("Price="+model.getPrice()+"RS");
                Picasso.get().load(model.getImage()).into(holderr.imageview);



                holderr.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type.equals("Admin")){
                            Intent inte=new Intent(Home.this, AdminMaintainProducts.class);
                            inte.putExtra("pid",model.getPid());
                            startActivity(inte);

                        }else {
                            Intent inte=new Intent(Home.this, ProductDetails.class);
                            inte.putExtra("pid",model.getPid());
                            startActivity(inte);
                        }

                    }
                });
            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }};

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        //recyclerView1.setAdapter(adapter);
        //adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        switch (id)
        {
            case R.id.Nav_cart:
                if(!type.equals("Admin")){
                    Intent in=new Intent(Home.this,Cart.class);
                    startActivity(in);
                }
                break;
            case R.id.Nav_search:
                if(!type.equals("Admin")) {
                    Intent ins = new Intent(Home.this, SearchProducts.class);
                    startActivity(ins);
                }
                break;
            case R.id.Nav_orders:
                if(!type.equals("Admin")) {
                    Intent ins1 = new Intent(Home.this, User_orders.class);
                    startActivity(ins1);
                }
                break;
            case R.id.Nav_settings:
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(Home.this, Settings.class);
                    startActivity(intent);
                }
                break;

            case R.id.Nav_logout:
                if(!type.equals("Admin")) {
                    Paper.book().destroy();
                    Intent i = new Intent(Home.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                break;
            default:
                return true;
        }


        return true;
    }
}