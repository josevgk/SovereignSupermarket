package com.sovereign.supermarket.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sovereign.supermarket.R;

/**
 * Created by Jose on 08/07/2017.
 */

public class ConsumerHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ConsumerHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(R.id.lblNameConsumer);
        field.setText(name);
    }

    public void setAddress(String address) {
        TextView field = (TextView) mView.findViewById(R.id.lblAddressConsumer);
        field.setText(address);
    }

    public void setContact(String contact) {
        TextView field = (TextView) mView.findViewById(R.id.lblPhoneConsumer);
        field.setText(contact);
    }

    public void setAdditionalInformation(String information) {
        TextView field = (TextView) mView.findViewById(R.id.lblAdditionalInformation);
        field.setText(information);
    }

    public LinearLayout getConsumer(){
        return (LinearLayout) mView.findViewById(R.id.lblConsumer);
    }

}
