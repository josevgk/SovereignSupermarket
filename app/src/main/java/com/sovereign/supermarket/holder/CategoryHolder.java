package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sovereign.supermarket.R;

/**
 * Created by Jose on 08/07/2017.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {

    private View mView;

    public CategoryHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.lblNameCategory);
        field.setText(name);
    }

    public void setDescription(String description) {
        TextView field = (TextView) mView.findViewById(R.id.lblDescriptionCategory);
        field.setText(description);
    }

    public LinearLayout getCategory(){
        return (LinearLayout) mView.findViewById(R.id.lblCategory);
    }
}
