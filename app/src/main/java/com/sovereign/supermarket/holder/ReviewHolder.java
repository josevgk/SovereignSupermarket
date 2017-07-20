package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sovereign.supermarket.R;

/**
 * Created by Jose on 08/07/2017.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ReviewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.lblNameReview);
        field.setText(name);
    }

    public void setText(String text) {
        TextView field = (TextView) mView.findViewById(R.id.lblTextReview);
        field.setText(text);
    }
    public void setStars(String stars) {
        TextView field = (TextView) mView.findViewById(R.id.lblStarsReview);
        field.setText(stars);
    }
    public void setDate(String date) {
        TextView field = (TextView) mView.findViewById(R.id.lblDateReview);
        field.setText(date);
    }


    public LinearLayout getReview(){
        return (LinearLayout) mView.findViewById(R.id.lblReview);
    }
}
