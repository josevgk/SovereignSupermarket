package com.sovereign.supermarket.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.adapter.SupermarketFragmentPagerAdapter;

public class SupermarketActivity extends AppCompatActivity {

    private DatabaseReference dbSupermarket;
    String keySupermarket;
    Boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);

        Bundle bundle = this.getIntent().getExtras();

        final String code = bundle.getString("keySupermarket");
        isAdmin = bundle.getBoolean("admin");
        keySupermarket=code;
        dbSupermarket = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("info");


        dbSupermarket.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameSupermarket=dataSnapshot.child("name").getValue().toString();
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {


                    if (dataSnapshot.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                    }
                }
                toolbar.setTitle(nameSupermarket);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new SupermarketFragmentPagerAdapter(
                getSupportFragmentManager(), keySupermarket)); //Sustituir el 1 por la key del supermercado cuando se implemente el listado

        TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_shopping_cart:
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    Intent intent = new Intent(SupermarketActivity.this, SupermarketShoppingCartActivity.class);
                    Bundle b = new Bundle();

                    b.putString("keySupermarket",keySupermarket);
                    intent.putExtras(b);
                    startActivity(intent);
                    Toast.makeText(this, "Shopping cart", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "You need to be logged", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("admin",isAdmin);
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 0);
                return true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
