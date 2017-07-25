package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.holder.ItemHolder;
import com.sovereign.supermarket.holder.ShoppingCartHolder;
import com.sovereign.supermarket.model.Item;
import com.sovereign.supermarket.model.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

public class SupermarketShoppingCartActivity extends AppCompatActivity {

    private DatabaseReference dbItems;
    private String keySupermarket;
    private String keyCategory;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter mAdapterItem;
    private Button buttonAdd;
    private Button buttonOrder;
    private Button buttonRemove;
    private Button buttonCancel;
    private Button buttonRemoveOrder;
    private TextView quantity;
    private TextView totalCost;
    private EditText number;
    Double totalPrice=0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_items);
        Bundle bundle = this.getIntent().getExtras();

        keySupermarket = bundle.getString("keySupermarket");

        buttonAdd=(Button) findViewById(R.id.buttonAdd);
        buttonOrder=(Button) findViewById(R.id.buttonOrder);
        buttonRemove=(Button) findViewById(R.id.buttonRemove);
        buttonCancel=(Button) findViewById(R.id.buttonCancel);
        quantity=(TextView) findViewById(R.id.quantity);
        totalCost=(TextView) findViewById(R.id.totalCost);
        number=(EditText) findViewById(R.id.number);
        buttonRemoveOrder=(Button) findViewById(R.id.buttonRemoveOrder);
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
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Shopping Cart");
        dbItems = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket"+keySupermarket).child("shoppingCarts").child(mAuth.getCurrentUser().getUid()).child("products");


        dbItems.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    totalPrice=0.0;
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Double price = Double.parseDouble(d.child("price").getValue().toString());
                        Integer quantity= Integer.parseInt(d.child("quantity").getValue().toString());
                        Double mult=price*quantity;
                        totalPrice+=mult;
                    }
                    buttonOrder.setVisibility(View.VISIBLE);
                    totalCost.setVisibility(View.VISIBLE);
                    String ptotal= String.format("%.2f", totalPrice);
                    totalCost.setText("Total: "+ptotal+"€");
                }else{
                    buttonOrder.setVisibility(View.INVISIBLE);
                    totalCost.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketConsumerOrder.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",keySupermarket);
                b.putString("totalPrice",totalPrice.toString());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        RecyclerView recycler = (RecyclerView) findViewById(R.id.listItems);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(this));


        mAdapterItem =
                new FirebaseRecyclerAdapter<ShoppingCart, ShoppingCartHolder>(
                        ShoppingCart.class, R.layout.list_item, ShoppingCartHolder.class, dbItems) {

                    @Override
                    public void populateViewHolder(ShoppingCartHolder shoppingCartHolder, final ShoppingCart shoppingCart, int position) {

                        shoppingCartHolder.setName(shoppingCart.getName());
                        shoppingCartHolder.setDescription(shoppingCart.getDescription());
                        shoppingCartHolder.setPrice("Price: "+String.valueOf(shoppingCart.getPrice()));
                        shoppingCartHolder.setQuantity("Quantity: "+String.valueOf(shoppingCart.getQuantity()));


                        shoppingCartHolder.getItem().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                number.setVisibility(View.VISIBLE);
                                buttonCancel.setVisibility(View.VISIBLE);
                                number.setHint("Number");
                                quantity.setVisibility(View.VISIBLE);
                                quantity.setText("Quantity "+shoppingCart.getName());
                                buttonAdd.setVisibility(View.VISIBLE);
                                buttonRemove.setVisibility(View.VISIBLE);
                                buttonAdd.setText("Change");
                                buttonAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(!TextUtils.isEmpty(number.getText().toString())) {
                                            Integer numberItems = Integer.parseInt(number.getText().toString());
                                            if(numberItems>=1) {
                                                addtoShoppingCart(shoppingCart.getSku(), shoppingCart.getName(), shoppingCart.getDescription(), shoppingCart.getPrice(), numberItems);
                                                Toast.makeText(getApplicationContext(), "Change quantity", Toast.LENGTH_SHORT).show();
                                                number.setVisibility(View.INVISIBLE);
                                                quantity.setVisibility(View.INVISIBLE);
                                                buttonAdd.setVisibility(View.INVISIBLE);
                                                buttonRemove.setVisibility(View.INVISIBLE);
                                                buttonCancel.setVisibility(View.INVISIBLE);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "The number must be at least 1", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "The number must be filled", Toast.LENGTH_SHORT).show();
                                        }
                                        

                                    }
                                });

                                buttonRemove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        removetoShoppingCart(shoppingCart.getSku());
                                        Toast.makeText(getApplicationContext(), "Product removed", Toast.LENGTH_SHORT).show();
                                        number.setVisibility(View.INVISIBLE);
                                        quantity.setVisibility(View.INVISIBLE);
                                        buttonAdd.setVisibility(View.INVISIBLE);
                                        buttonRemove.setVisibility(View.INVISIBLE);
                                        buttonCancel.setVisibility(View.INVISIBLE);

                                    }
                                });

                                buttonCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        number.setVisibility(View.INVISIBLE);
                                        quantity.setVisibility(View.INVISIBLE);
                                        buttonAdd.setVisibility(View.INVISIBLE);
                                        buttonCancel.setVisibility(View.INVISIBLE);

                                    }
                                });





                                return false;
                            }
                        });

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



    public void addtoShoppingCart(String sku, String name, String description, Double price, Integer quantity) {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ShoppingCart shoppingCart = new ShoppingCart(sku,name, description, price, quantity);
        Map<String, Object> shoppingCartValues = shoppingCart.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/shoppingCarts/" + key + "/products/"+sku+"/", shoppingCartValues);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

    }

    public void removetoShoppingCart(String sku) {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().getRoot().child("supermarkets").child("supermarket"+keySupermarket).
                child("shoppingCarts").child(key).child("products").child(sku).removeValue();

    }

    public void removeOrder() {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().getRoot().child("supermarkets").child("supermarket"+keySupermarket).
                child("shoppingCarts").child(key).removeValue();

    }


}