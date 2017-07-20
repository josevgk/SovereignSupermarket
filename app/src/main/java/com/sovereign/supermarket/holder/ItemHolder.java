package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.Exclude;
import com.sovereign.supermarket.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jose on 08/07/2017.
 */

public class ItemHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ItemHolder(View itemView) {
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
    public LinearLayout getItem(){
        return (LinearLayout) mView.findViewById(R.id.lblItem);
    }

}
