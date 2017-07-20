package com.sovereign.supermarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sovereign.supermarket.R;
import com.sovereign.supermarket.activity.ListSupermarketsActivity;
import com.sovereign.supermarket.activity.SupermarketActivity;
import com.sovereign.supermarket.activity.SupermarketItemsActivity;
import com.sovereign.supermarket.holder.SupermarketHolder;
import com.sovereign.supermarket.model.Supermarket;

import java.util.ArrayList;


public class SupermarketRecyclerAdapter extends RecyclerView.Adapter<SupermarketHolder> {

    private ArrayList<Supermarket> supermarkets;

    public SupermarketRecyclerAdapter(ArrayList<Supermarket> supermarkets) {
        this.supermarkets = supermarkets;
    }


    @Override
    public SupermarketHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_supermarket, viewGroup, false);

        SupermarketHolder tvh = new SupermarketHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(SupermarketHolder viewHolder, int pos) {
        final Supermarket supermarket = supermarkets.get(pos);

        viewHolder.setName(supermarket.getName());
        viewHolder.setLocation(supermarket.getAddress());
        viewHolder.getSupermarket().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketActivity.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",supermarket.getKeySupermarket());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return supermarkets.size();
    }

    public ArrayList<Supermarket> getItems() {
        return supermarkets;
    }


}