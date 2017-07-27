package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.holder.ConsumerHolder;
import com.sovereign.supermarket.holder.ItemHolder;
import com.sovereign.supermarket.model.Consumer;
import com.sovereign.supermarket.model.Item;

public class SupermarketProductsOrderActivity extends AppCompatActivity {

    private DatabaseReference dbItems;
    private String keySupermarket;
    private String codePerson;
    private String namePerson;
    private Button buttonRemoveOrder;
    private FirebaseRecyclerAdapter mAdapterItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_items);
        buttonRemoveOrder=(Button) findViewById(R.id.buttonRemoveOrder);
        buttonRemoveOrder.setText("Delivered");
        buttonRemoveOrder.setVisibility(View.VISIBLE);
        buttonRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOrder();
                Toast.makeText(getApplicationContext(), "Order removed", Toast.LENGTH_SHORT).show();
                buttonRemoveOrder.setVisibility(View.INVISIBLE);

            }
        });


        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = this.getIntent().getExtras();
        keySupermarket = bundle.getString("keySupermarket");
        codePerson=bundle.getString("code");
        namePerson=bundle.getString("name");
        actionBar.setTitle(namePerson);
        dbItems = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket)
                .child("orders").child(codePerson).child("products");


        RecyclerView recycler = (RecyclerView) findViewById(R.id.listItems);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAdapterItem =
                new FirebaseRecyclerAdapter<Item, ItemHolder>(
                        Item.class, R.layout.list_item, ItemHolder.class, dbItems) {

                    @Override
                    public void populateViewHolder(ItemHolder itemHolder, final Item item, int position) {

                        itemHolder.setName(item.getName());
                        itemHolder.setDescription(item.getDescription());
                        itemHolder.setPrice("Price: " + String.valueOf(item.getPrice()));



                    }
                };
        recycler.setAdapter(mAdapterItem);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapterItem.cleanup();
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

    public void removeOrder() {

        FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket)
                .child("orders").child(codePerson).removeValue();

    }

}