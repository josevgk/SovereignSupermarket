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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.holder.ConsumerHolder;
import com.sovereign.supermarket.holder.ItemHolder;
import com.sovereign.supermarket.model.Consumer;
import com.sovereign.supermarket.model.Item;
import com.sovereign.supermarket.model.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

public class SupermarketOrdersActivity extends AppCompatActivity {

    private DatabaseReference dbOrders;
    private String keySupermarket;
    private String keyCategory;
    private FirebaseRecyclerAdapter mAdapterItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_items);


        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = this.getIntent().getExtras();
        keySupermarket = bundle.getString("keySupermarket");
        actionBar.setTitle("Orders");
        dbOrders = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("orders");


        RecyclerView recycler = (RecyclerView) findViewById(R.id.listItems);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAdapterItem =
                new FirebaseRecyclerAdapter<Consumer, ConsumerHolder>(
                        Consumer.class, R.layout.list_consumer, ConsumerHolder.class, dbOrders) {

                    @Override
                    public void populateViewHolder(ConsumerHolder consumerHolder, final Consumer consumer, int position) {

                        consumerHolder.setName(consumer.getName()+" "+consumer.getSurnames());
                        consumerHolder.setAddress(consumer.getAddress());
                        consumerHolder.setContact("Contact: "+consumer.getPhone()+", "+consumer.getEmail());
                        if(!consumer.getInformation().isEmpty()) {
                            consumerHolder.setAdditionalInformation("Additional information: " + consumer.getInformation());
                        }else{
                            consumerHolder.setAdditionalInformation("Additional information: Nothing" );
                        }
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            consumerHolder.getConsumer().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Context context = view.getContext();
                                    Intent intent = new Intent(context, SupermarketProductsOrderActivity.class);
                                    Bundle b = new Bundle();

                                    b.putString("keySupermarket",keySupermarket);
                                    b.putString("code",consumer.getCode());
                                    intent.putExtras(b);
                                    startActivity(intent);

                                }
                            });

                        }
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


}