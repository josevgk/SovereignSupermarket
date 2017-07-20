package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.holder.ItemHolder;
import com.sovereign.supermarket.model.Item;
import com.sovereign.supermarket.model.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

public class SupermarketItemsActivity extends AppCompatActivity {

    private DatabaseReference dbItems;
    private String keySupermarket;
    private String keyCategory;
    private FirebaseRecyclerAdapter mAdapterItem;
    private Button buttonAdd;
    private Button buttonRemove;
    private Button buttonCancel;
    private TextView quantity, questionRemove;
    private EditText number;
    private Button buttonNewItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_items);
        buttonAdd=(Button) findViewById(R.id.buttonAdd);
        buttonCancel=(Button) findViewById(R.id.buttonCancel);
        quantity=(TextView) findViewById(R.id.quantity);
        questionRemove=(TextView) findViewById(R.id.questionSecurityRemove);
        buttonRemove=(Button) findViewById(R.id.buttonRemove);
        number=(EditText) findViewById(R.id.number);

        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = this.getIntent().getExtras();
        keySupermarket = bundle.getString("keySupermarket");
        keyCategory = bundle.getString("keyCategory");
        System.out.println(keyCategory);
        final Boolean isAdmin=bundle.getBoolean("admin");
        if(isAdmin==true){
            buttonNewItem=(Button) findViewById(R.id.buttonNewItem);
            buttonNewItem.setVisibility(View.VISIBLE);
            buttonNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SupermarketAddItemActivity.class);
                    Bundle b = new Bundle();

                    System.out.println(keySupermarket);
                    b.putString("keySupermarket",keySupermarket);
                    b.putString("keyCategory",keyCategory);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
        actionBar.setTitle(keyCategory);
        dbItems = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("items");


        RecyclerView recycler = (RecyclerView) findViewById(R.id.listItems);
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAdapterItem =
                new FirebaseRecyclerAdapter<Item, ItemHolder>(
                        Item.class, R.layout.list_item, ItemHolder.class, dbItems.orderByChild("category").equalTo(keyCategory)) {

                    @Override
                    public void populateViewHolder( ItemHolder itemHolder, final Item item, int position) {

                        itemHolder.setName(item.getName());
                        itemHolder.setDescription(item.getDescription());
                        itemHolder.setPrice("Price: " + String.valueOf(item.getPrice()));
                        if (FirebaseAuth.getInstance().getCurrentUser() != null &&isAdmin==false) {
                            itemHolder.getItem().setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    number.setVisibility(View.VISIBLE);
                                    number.setHint("Number");
                                    quantity.setVisibility(View.VISIBLE);
                                    quantity.setText("Quantity " + item.getName());
                                    buttonAdd.setVisibility(View.VISIBLE);
                                    buttonCancel.setVisibility(View.VISIBLE);
                                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Integer numberItems = Integer.parseInt(number.getText().toString());
                                            if (numberItems >= 1) {
                                                addtoShoppingCart(item.getSku(), item.getName(), item.getDescription(), item.getPrice(), numberItems);
                                                Toast.makeText(getApplicationContext(), "Add to shopping cart", Toast.LENGTH_SHORT).show();
                                                number.setVisibility(View.INVISIBLE);
                                                quantity.setVisibility(View.INVISIBLE);
                                                buttonAdd.setVisibility(View.INVISIBLE);
                                                buttonCancel.setVisibility(View.INVISIBLE);
                                            }
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

                        }else if(isAdmin==true){
                            itemHolder.getItem().setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    buttonCancel.setVisibility(View.VISIBLE);
                                    buttonRemove.setVisibility(View.VISIBLE);
                                    questionRemove.setVisibility(View.VISIBLE);

                                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            removeItemSupermarket(item.getSku());
                                            Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                                            buttonRemove.setVisibility(View.INVISIBLE);
                                            buttonCancel.setVisibility(View.INVISIBLE);
                                            questionRemove.setVisibility(View.INVISIBLE);

                                        }
                                    });

                                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            buttonRemove.setVisibility(View.INVISIBLE);
                                            buttonCancel.setVisibility(View.INVISIBLE);
                                            questionRemove.setVisibility(View.INVISIBLE);

                                        }
                                    });



                                    return false;
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

    public void addtoShoppingCart(String sku, String name, String description, Double price, Integer quantity) {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ShoppingCart shoppingCart = new ShoppingCart(sku,name, description, price, quantity);
        Map<String, Object> shoppingCartValues = shoppingCart.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/shoppingCarts/" + key + "/products/"+sku+"/", shoppingCartValues);

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

    }

    public void removeItemSupermarket(String sku) {

        FirebaseDatabase.getInstance().getReference().getRoot().child("supermarkets").child("supermarket"+keySupermarket).
                child("items").child(sku).removeValue();

    }
}