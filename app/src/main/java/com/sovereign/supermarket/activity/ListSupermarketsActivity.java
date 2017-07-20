package com.sovereign.supermarket.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.adapter.SupermarketRecyclerAdapter;
import com.sovereign.supermarket.model.Supermarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListSupermarketsActivity extends AppCompatActivity {
    private static final String TAGLOG = "firebase-db";

    private DatabaseReference dbLocations;
    private SupermarketRecyclerAdapter supermarketRecyclerAdapter;
    private ArrayList<Supermarket> supermarketsNearby;
    private ValueEventListener eventListenerSupermarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_supermarkets);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Supermarkets near from you");

        // Se inicializan las variables y se obtienen los extras (ubicación introducida) de la actividad anterior
        dbLocations = FirebaseDatabase.getInstance().getReference().child("supermarkets");
        supermarketsNearby = new ArrayList<>();
        supermarketRecyclerAdapter = new SupermarketRecyclerAdapter(supermarketsNearby);
        Bundle bundle = this.getIntent().getExtras();
        final LatLng coordAddressSearched = bundle.getParcelable("coordAddressSearched");

        // Se inicializa la RecyclerView
        RecyclerView recView = (RecyclerView) findViewById(R.id.listSupermarket);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // Se añade el adaptador a la Recycler view
        recView.setAdapter(supermarketRecyclerAdapter);

        // Se crea el listener para la referencia a la base de datos
        eventListenerSupermarket = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se obtienen las coordenadas de cada ubicación y se mide la distancia con la ubicación introducida
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String name = d.child("info").child("name").getValue().toString();
                    String dbLat = d.child("info").child("location").child("latitude").getValue().toString();
                    String dbLon = d.child("info").child("location").child("longitude").getValue().toString();
                    String address = d.child("info").child("location").child("address").getValue().toString();
                    String key = d.child("info").child("key").getValue().toString();
                    float[] results = new float[1];

                    Location.distanceBetween(coordAddressSearched.latitude, coordAddressSearched.longitude, Double.valueOf(dbLat), Double.valueOf(dbLon), results);
                    float distance = results[0] / (float) 1E3;
                    // Si la distancia es menor a 1km se añade a la lista de supermercados cercanos y se le notifica al adaptador
                    if (distance < 1.) {
                        Supermarket s = new Supermarket(name, dbLat, dbLon,key,address);
                        supermarketsNearby.add(s);
                        supermarketRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAGLOG, "Error!", databaseError.toException());
            }
        };



        // Se añade el listener a la referencia a la base de datos
        dbLocations.addListenerForSingleValueEvent(eventListenerSupermarket);

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
