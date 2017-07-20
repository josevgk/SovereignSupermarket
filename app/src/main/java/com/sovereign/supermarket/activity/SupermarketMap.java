package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.Manifest;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.model.Supermarket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoseV on 10/7/17.
 */

public class SupermarketMap extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAGLOG = "firebase-db";
    private GoogleMap mMap;
    private DatabaseReference dbLocations;
    private Marker marcador;
    private List<Marker> listMarcador= new ArrayList<Marker>();
    private ArrayList<Supermarket> supermarketsNearby;
    private ValueEventListener eventListenerSupermarket;
    double lat = 0.0;
    double lng = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbLocations = FirebaseDatabase.getInstance().getReference().child("supermarkets");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventListenerSupermarket = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se obtienen las coordenadas de cada ubicación y se mide la distancia con la ubicación introducida
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String name = d.child("info").child("name").getValue().toString();
                    String dbLat = d.child("info").child("location").child("latitude").getValue().toString();
                    String dbLon = d.child("info").child("location").child("longitude").getValue().toString();
                    String code=d.child("info").child("key").getValue().toString();

                    // Si la distancia es menor a 1km se añade a la lista de supermercados cercanos y se le notifica al adaptador
                    agregarMarcador(new Double(dbLat),new Double(dbLon),name,code);

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAGLOG, "Error!", databaseError.toException());
            }


        };
        dbLocations.addListenerForSingleValueEvent(eventListenerSupermarket);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
    }

    private void agregarMarcadorHere(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Current position").icon(BitmapDescriptorFactory.fromResource(R.mipmap.you_are_here_icon)));
        marcador.setTag("current");
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTag().equals("current")) {
                    Intent intent = new Intent(SupermarketMap.this, SupermarketActivity.class);
                    Bundle b = new Bundle();

                    b.putString("keySupermarket", marker.getTag().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
        mMap.animateCamera(miUbicacion);
    }

    private void agregarMarcador(double lat, double lng,String name,final String code) {
        LatLng coordenadas = new LatLng(lat, lng);
        final Marker marcador1 = mMap.addMarker(new MarkerOptions().position(coordenadas).title(name).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        marcador1.setTag(code);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTag().equals("current")) {
                    Intent intent = new Intent(SupermarketMap.this, SupermarketActivity.class);
                    Bundle b = new Bundle();

                    b.putString("keySupermarket", marker.getTag().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
        listMarcador.add(marcador1);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcadorHere(lat, lng);

        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,locListener);
    }





}
