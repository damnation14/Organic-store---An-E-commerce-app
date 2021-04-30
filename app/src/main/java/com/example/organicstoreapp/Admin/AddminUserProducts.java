package com.example.organicstoreapp.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organicstoreapp.Model.Cart;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.ViewHolder.Cart_View_Holder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddminUserProducts extends AppCompatActivity {
    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
   private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmin_user_products);
        userID=getIntent().getStringExtra("uid");
        productList=findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

        cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List")
        .child("Admin View").child(userID).child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, Cart_View_Holder> adapter=new FirebaseRecyclerAdapter<Cart, Cart_View_Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cart_View_Holder cart_view_holder, int i, @NonNull Cart cart) {
                cart_view_holder.txtProductQuantity.setText("Quantity = "+ cart.getQuantity());
                cart_view_holder.txtProductPrice.setText("Price = "+cart.getPrice()+"RS");
                cart_view_holder.txtProductName.setText(cart.getPname());
            }

            @NonNull
            @Override
            public Cart_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                Cart_View_Holder holder=new Cart_View_Holder(view);
                return holder;
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();
    }
}