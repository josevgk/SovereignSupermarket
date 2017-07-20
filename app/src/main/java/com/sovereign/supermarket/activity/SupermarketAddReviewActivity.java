package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.model.Item;
import com.sovereign.supermarket.model.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupermarketAddReviewActivity extends AppCompatActivity implements View.OnClickListener{


    Button buttonAddReview, buttonCancelReview;
    EditText editTextNameReview, editTextStarsReview, editTextTextReview;
    private String keySupermarket;
    private Boolean isAdmin;
    private DatabaseReference dbReviews;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);
        Bundle bundle = this.getIntent().getExtras();

        keySupermarket = bundle.getString("keySupermarket");
        isAdmin = bundle.getBoolean("admin");

        buttonAddReview = (Button) findViewById(R.id.buttonAddReview);
        buttonCancelReview = (Button) findViewById(R.id.buttonCancelReview);
        editTextNameReview = (EditText) findViewById(R.id.nameNewReview);
        editTextStarsReview = (EditText) findViewById(R.id.starsNewReview);
        editTextTextReview = (EditText) findViewById(R.id.textNewReview);

        buttonAddReview.setOnClickListener(this);
        buttonCancelReview.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle("Create new review");
        dbReviews = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket"+keySupermarket).child("reviews");








    }




    private void createReview(String name, String text, String stars) {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate = sdf.format(cal.getTime());
        System.out.println("Current date in String Format: " + strDate);
        Review review = new Review(name,text,stars,strDate);
        Map<String, Object> infoReview = review.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/reviews/"+key+"/", infoReview);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        Toast.makeText(getApplicationContext(), "Review created", Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddReview:
                String name = editTextNameReview.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    editTextNameReview.setError("The name must be filled");
                    return;
                }
                String text = editTextTextReview.getText().toString();
                if(TextUtils.isEmpty(text)) {
                    editTextTextReview.setError("The text must be filled");
                    return;
                }
                String stars = editTextStarsReview.getText().toString();
                if(TextUtils.isEmpty(stars) ) {
                    editTextStarsReview.setError("Stars must be filled");
                    return;
                }
                if(Integer.parseInt(stars)>5){
                    editTextStarsReview.setError("Stars must be from 0 to 5 stars");
                    return;
                }
                createReview(name, text,stars);
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketActivity.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",keySupermarket);
                intent.putExtras(b);
                context.startActivity(intent);
                break;
            case R.id.buttonCancelReview:
                Context context2 = view.getContext();
                Intent intent2 = new Intent(context2, SupermarketActivity.class);
                Bundle b2 = new Bundle();

                b2.putString("keySupermarket",keySupermarket);
                intent2.putExtras(b2);
                context2.startActivity(intent2);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

