package com.sovereign.supermarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by JoseV on 7/7/17.
 */

public class HomeActivity extends AppCompatActivity{

    private static final String TAG = "HomeActivity";

    private Button buttonSearch;
    private Button buttonMap;
    private Button mySupermarket;
    private Button buttonOrders;
    private LatLng coordAddressSearched;
    private DatabaseReference dbEmails;

    private Button buttonTestLogin; //Temporal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonSearch = (Button) findViewById(R.id.btnSearch);
        buttonTestLogin = (Button) findViewById(R.id.btnTestLogin);
        buttonMap = (Button) findViewById(R.id.buttonMap);
        mySupermarket = (Button) findViewById(R.id.mySupermarket);
        buttonOrders = (Button) findViewById(R.id.buttonOrders);
        dbEmails = FirebaseDatabase.getInstance().getReference().child("supermarkets");

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint(getString(R.string.searchHint));

        autocompleteFragment.setFilter(new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build());

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                coordAddressSearched = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        dbEmails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    final String email = d.child("info").child("email").getValue().toString();
                    final String keySupermarket= d.child("info").child("key").getValue().toString();
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
                            mySupermarket.setVisibility(View.VISIBLE);
                            mySupermarket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
                                        Intent intent = new Intent(HomeActivity.this, SupermarketActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("keySupermarket", keySupermarket);
                                        b.putBoolean("admin",true);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getBaseContext(), "Access Denied", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            buttonOrders.setVisibility(View.VISIBLE);
                            buttonOrders.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email)) {
                                        Intent intent = new Intent(HomeActivity.this, SupermarketOrdersActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("keySupermarket", keySupermarket);
                                        intent.putExtras(b);
                                        Toast.makeText(getBaseContext(), "Orders", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getBaseContext(), "Access Denied", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListSupermarketsActivity.class);
                Bundle b = new Bundle();

                b.putParcelable("coordAddressSearched", coordAddressSearched);
                intent.putExtras(b);

                if (coordAddressSearched == null) {
                    Toast.makeText(getBaseContext(), R.string.emptySearch, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }
        });



        buttonTestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EmailPasswordActivity.class);
                startActivity(intent);

            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SupermarketMap.class);
                startActivity(intent);

            }
        });

    }
}


