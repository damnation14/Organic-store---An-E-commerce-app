package com.example.organicstoreapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organicstoreapp.Interface.ItemClickListener;
import com.example.organicstoreapp.R;

public class OfferProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /*public TextView textProductName1,textProductDescription1,textProductprice1;
    public ImageView imageview1;
    public ItemClickListener listener;*/

    public TextView textProductName,textProductDescription,textProductprice;
    public ImageView imageview;
    public ItemClickListener listener;
    public OfferProductViewHolder(@NonNull View itemView) {
        /*super(itemView);
         textProductName1=itemView.findViewById(R.id.productnametv1);
         textProductDescription1=itemView.findViewById(R.id.productsdescriptiontv1);
        textProductprice1=itemView.findViewById(R.id.productpricetv1);
         imageview1=itemView.findViewById(R.id.productimage1);*/


        super(itemView);
        imageview=itemView.findViewById(R.id.productimageIV);
        textProductName=itemView.findViewById(R.id.productnametv);
        textProductDescription=itemView.findViewById(R.id.productdescriptiontv);
        textProductprice=itemView.findViewById(R.id.productpricetv);
    }
    public void setItemClicklistener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}
