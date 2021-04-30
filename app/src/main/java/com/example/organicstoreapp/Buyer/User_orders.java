package com.example.organicstoreapp.Buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organicstoreapp.Admin.AddminUserProducts;
import com.example.organicstoreapp.Model.AdminOrders;
import com.example.organicstoreapp.Model.Products;
import com.example.organicstoreapp.Model.UserOrders;
import com.example.organicstoreapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_orders extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_orders_list);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.orders_list1);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserOrders> options=
                new FirebaseRecyclerOptions.Builder<UserOrders>().setQuery(orderRef,UserOrders.class).build();

        FirebaseRecyclerAdapter<UserOrders,User_orders.userOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<UserOrders, User_orders.userOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull User_orders.userOrdersViewHolder holder, final int i, @NonNull final UserOrders model) {
                        holder.userName.setText("Name:"+model.getName());
                        holder.userPhoneNumber.setText("Phone:"+model.getPhone());
                        holder.userTotalPrice.setText("Total Amount:"+model.getTotal_Amount());
                        holder.userDateTime.setText("Order at:"+model.getDate()+" "+model.getTime());
                        holder.userShippAddress.setText("Shipping Address:"+model.getAddress());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID=getRef(i).getKey();
                                Intent intent=new Intent(User_orders.this, UserOrderProducts.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public User_orders.userOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_list,parent,false);
                        return new User_orders.userOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class userOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippAddress;
        public Button showOrdersBtn;
        public userOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippAddress=itemView.findViewById(R.id.order_address_city);
            showOrdersBtn=itemView.findViewById(R.id.show_all_prod_btn);

        }
    }

}
