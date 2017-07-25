package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.model.Category;
import com.sovereign.supermarket.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupermarketAddItemActivity extends AppCompatActivity implements View.OnClickListener{


    Button buttonAddItem, buttonCancelItem;
    EditText editTextNameItem, editTextDescriptionItem, editTextPriceItem;
    private String keySupermarket, keyCategory;
    private Boolean isAdmin;
    private DatabaseReference dbItems;
    Long numberItems;
    List<Item> listItems= new ArrayList<Item>();
    List<String> listSku= new ArrayList<String>();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Bundle bundle = this.getIntent().getExtras();

        keySupermarket = bundle.getString("keySupermarket");
        keyCategory=bundle.getString("keyCategory");
        isAdmin=bundle.getBoolean("admin");

        buttonAddItem = (Button) findViewById(R.id.buttonAddItem);
        buttonCancelItem = (Button) findViewById(R.id.buttonCancelItem);
        editTextNameItem = (EditText) findViewById(R.id.nameNewItem);
        editTextDescriptionItem = (EditText) findViewById(R.id.descriptionNewItem);
        editTextPriceItem = (EditText) findViewById(R.id.priceNewItem);

        buttonAddItem.setOnClickListener(this);
        buttonAddItem.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle("Create new item");
        dbItems = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket"+keySupermarket).child("items");

        dbItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    numberItems = dataSnapshot.getChildrenCount();
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        listSku.add(d.child("sku").getValue().toString());
                    }
                }else{
                    numberItems=0L;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }




    private void createItem(String name, String description, Double price) {

        String sku;
        Integer pos = (int) Math.floor(Math.random() * 10000);
        if(pos<10){
            sku="01-000"+pos.toString();
        }else if(pos<100 && pos>=10){
            sku="01-00"+pos.toString();
        }else if(pos<1000 && pos>=100) {
            sku = "01-0" + pos.toString();
        }else{
            sku = "01-" + pos.toString();
        }
        while(listSku.contains(sku)){
            pos = (int) Math.floor(Math.random() * 10000);
            if(pos<10){
                sku="01-000"+pos.toString();
            }else if(pos<100 && pos>=10){
                sku="01-00"+pos.toString();
            }else if(pos<1000 && pos>=100) {
                sku = "01-0" + pos.toString();
            }else{
                sku = "01-" + pos.toString();
            }
        }
        String picture="/..";
        Item item = new Item(sku,name,description,price,picture,false,keyCategory);
        Map<String, Object> infoItems = item.createNewToMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/items/"+sku+"/", infoItems);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        Toast.makeText(getApplicationContext(), "Item created", Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddItem:
                String name = editTextNameItem.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    editTextNameItem.setError("The name must be filled");
                    return;
                }
                String description = editTextDescriptionItem.getText().toString();
                if(TextUtils.isEmpty(description)) {
                    editTextDescriptionItem.setError("The description must be filled");
                    return;
                }
                String price = editTextPriceItem.getText().toString();
                if(TextUtils.isEmpty(price)) {
                    editTextPriceItem.setError("The price must be filled");
                    return;
                }
                createItem(name, description,Double.parseDouble(price));
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketActivity.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",keySupermarket);
                b.putBoolean("admin",true);
                intent.putExtras(b);
                context.startActivity(intent);
                break;
            case R.id.buttonCancelItem:
                Context context2 = view.getContext();
                Intent intent2 = new Intent(context2, SupermarketActivity.class);
                Bundle b2 = new Bundle();

                b2.putString("keySupermarket",keySupermarket);
                b2.putBoolean("admin",true);
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

