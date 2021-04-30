package com.example.organicstoreapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organicstoreapp.Prevalent.Prevalent;
import com.example.organicstoreapp.R;
import com.example.organicstoreapp.ViewHolder.Cart_View_Holder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalAmount;
    private int totalprice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn=findViewById(R.id.next_process_btn);
        txtTotalAmount=findViewById(R.id.total_price_TV);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cart.this, placeorder.class);
                intent.putExtra("Total Price",String.valueOf(totalprice));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {



        super.onStart();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<com.example.organicstoreapp.Model.Cart> options=new FirebaseRecyclerOptions.Builder<com.example.organicstoreapp.Model.Cart>().setQuery(cartListRef.child("User View")
        .child(Prevalent.CurrentOnlineUser.getPhone()).child("Products"), com.example.organicstoreapp.Model.Cart.class).build();


        FirebaseRecyclerAdapter<com.example.organicstoreapp.Model.Cart,Cart_View_Holder> adapter=new FirebaseRecyclerAdapter<com.example.organicstoreapp.Model.Cart, Cart_View_Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cart_View_Holder cart_view_holder, int i, @NonNull final com.example.organicstoreapp.Model.Cart cart) {
                cart_view_holder.txtProductQuantity.setText("Quantity = "+ cart.getQuantity());
                cart_view_holder.txtProductPrice.setText("Price = "+cart.getPrice()+"RS");
                cart_view_holder.txtProductName.setText(cart.getPname());
                int eachproductprice=(Integer.valueOf(cart.getPrice()))*(Integer.valueOf(cart.getQuantity()));
                totalprice+=eachproductprice;
                txtTotalAmount.setText("Total price="+"Rs"+String.valueOf(totalprice));

                cart_view_holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[]=new CharSequence[] {
                           "Edit",
                            "Remove"

                            };
                        AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
                        builder.setTitle("Cart options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    Intent intent1= new Intent(Cart.this, ProductDetails.class );
                                    intent1.putExtra("pid",cart.getPid());
                                    startActivity(intent1);
                                }
                                if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(Cart.this,"Item removed",Toast.LENGTH_LONG).show();
                                                        Intent intent1= new Intent(Cart.this,Cart.class);
                                                        startActivity(intent1);
                                                    }

                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                        }

                    }
                );
            }

            @NonNull
            @Override
            public Cart_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                Cart_View_Holder holder=new Cart_View_Holder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}