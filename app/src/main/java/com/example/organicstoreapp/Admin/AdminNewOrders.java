package com.example.organicstoreapp.Admin;

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

import com.example.organicstoreapp.Model.AdminOrders;
import com.example.organicstoreapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrders extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(orderRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,adminOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, adminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull adminOrdersViewHolder holder,final int pos, @NonNull final AdminOrders model) {
                        holder.userName.setText("Name:"+model.getName());
                        holder.userPhoneNumber.setText("Phone:"+model.getPhone());
                        holder.userTotalPrice.setText("Total Amount:"+model.getTotal_Amount());
                        holder.userDateTime.setText("Order at:"+model.getDate()+" "+model.getTime());
                        holder.userShippAddress.setText("Shipping Address:"+model.getAddress());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID=getRef(pos).getKey();
                                Intent intent=new Intent(AdminNewOrders.this, AddminUserProducts.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[]=new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrders.this);
                                builder.setTitle("Have you shipped this order products?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       if(i==0){
                                           String uID=getRef(pos).getKey();
                                           RemoverOrder(uID);
                                       }else {
                                           finish();
                                       }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public adminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
                        return new AdminNewOrders.adminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class adminOrdersViewHolder extends RecyclerView.ViewHolder{
    public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippAddress;
    public Button showOrdersBtn;
        public adminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippAddress=itemView.findViewById(R.id.order_address_city);
            showOrdersBtn=itemView.findViewById(R.id.show_all_prod_btn);

        }
    }


    private void RemoverOrder(String uID) {
        orderRef.child(uID).removeValue();
    }
}