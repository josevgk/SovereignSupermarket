package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sovereign.supermarket.R;

public class SupermarketHolder extends RecyclerView.ViewHolder{

    private View mView;

    public SupermarketHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.lblName);
        field.setText(name);
    }

    public void setLocation(String location) {
        TextView field = (TextView) mView.findViewById(R.id.lblLocation);
        field.setText(location);
    }

    public LinearLayout getSupermarket(){
        return (LinearLayout) mView.findViewById(R.id.lblSupermarket);
    }



}
