package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sovereign.supermarket.R;

/**
 * Created by Jose on 08/07/2017.
 */

public class ShoppingCartHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ShoppingCartHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.lblNameItem);
        field.setText(name);
    }

    public void setDescription(String description) {
        TextView field = (TextView) mView.findViewById(R.id.lblDescriptionItem);
        field.setText(description);
    }

    public void setPrice(String price) {
        TextView field = (TextView) mView.findViewById(R.id.lblPriceItem);
        field.setText(price);
    }

    public void setQuantity(String quantity) {
        TextView field = (TextView) mView.findViewById(R.id.lblQuantityItem);
        field.setText(quantity);
    }
    public LinearLayout getItem(){
        return (LinearLayout) mView.findViewById(R.id.lblItem);
    }

}
