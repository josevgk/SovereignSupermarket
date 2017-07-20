package com.sovereign.supermarket.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sovereign.supermarket.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class SupermarketHomeFragment extends Fragment {

    private static final String TAGLOG = "firebase-db";

    private DatabaseReference dbInfo;
    private ValueEventListener eventListenerInfo;
    private ImageView img;
    private View viewFragment;
    private String keySupermarket;
    private TextView lblName;
    private TextView lblAddress;
    private TextView lblEmail;
    private TextView lblPhone;
    private TextView lblReceivingHours;
    private FirebaseStorage storageReference=FirebaseStorage.getInstance();


    public static SupermarketHomeFragment newInstance(String keySupermarket) {
        SupermarketHomeFragment fragment = new SupermarketHomeFragment();
        Bundle b = new Bundle();
        b.putString("keySupermarket", keySupermarket);
        fragment.setArguments(b);

        return fragment;
    }


    public SupermarketHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        keySupermarket = b.getString("keySupermarket");
        dbInfo = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("info");




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewFragment = inflater.inflate(R.layout.fragment_supermarket_info, container, false);

        lblName = (TextView) viewFragment.findViewById(R.id.lblName);
        lblAddress = (TextView) viewFragment.findViewById(R.id.lblAddress);
        lblEmail = (TextView) viewFragment.findViewById(R.id.lblEmail);
        lblPhone = (TextView) viewFragment.findViewById(R.id.lblPhone);
        lblReceivingHours = (TextView) viewFragment.findViewById(R.id.lblReicivingHours);
        img = (ImageView) viewFragment.findViewById(R.id.imageView);


        eventListenerInfo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lblName.setText(dataSnapshot.child("name").getValue().toString());
                lblAddress.setText(dataSnapshot.child("location").child("address").getValue().toString());
                lblEmail.setText(dataSnapshot.child("email").getValue().toString());
                lblPhone.setText(dataSnapshot.child("phone").getValue().toString());
                lblReceivingHours.setText(dataSnapshot.child("receivingHours").getValue().toString());

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference dateRef = storageRef.child("/supermarket"+keySupermarket+".jpg");
                dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {
                        new GetImageToURL().execute(downloadUrl.toString());
                    }
                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAGLOG, "Error!", databaseError.toException());
            }
        };
        dbInfo.addListenerForSingleValueEvent(eventListenerInfo);

        return viewFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class GetImageToURL extends AsyncTask < String, Void, Bitmap > {

        @Override
        protected Bitmap doInBackground(String...params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap myBitMap) {
            img.setImageBitmap(myBitMap);
        }
    }


}