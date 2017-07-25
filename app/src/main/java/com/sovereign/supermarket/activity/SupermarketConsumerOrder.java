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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.model.Consumer;
import com.sovereign.supermarket.model.ShoppingCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupermarketConsumerOrder extends AppCompatActivity implements View.OnClickListener{


    Button buttonConfirm, buttonCancelOrder;
    EditText editTextName, editTextSurname, editTextPhone, editTextAddress, editTextInformation;
    TextView toPay;
    private String keySupermarket, totalPrice;
    List<ShoppingCart> allProducts=new ArrayList<ShoppingCart>();

    private FirebaseAuth mAuth;
    private DatabaseReference dbConsumerOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        Bundle bundle = this.getIntent().getExtras();

        keySupermarket = bundle.getString("keySupermarket");
        totalPrice=bundle.getString("totalPrice");


        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonCancelOrder = (Button) findViewById(R.id.buttonCancelOrder);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextInformation = (EditText) findViewById(R.id.editTextInformation);
        toPay=(TextView) findViewById(R.id.toPay) ;
        toPay.setText("TO PAY: "+totalPrice+"€");


        buttonConfirm.setOnClickListener(this);
        buttonCancelOrder.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Order");
        dbConsumerOrder = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket"+keySupermarket).child("shoppingCarts").child(mAuth.getCurrentUser().getUid()).child("products");

        dbConsumerOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Double price = Double.parseDouble(d.child("price").getValue().toString());
                    Integer quantity= Integer.parseInt(d.child("quantity").getValue().toString());
                    String name=d.child("name").getValue().toString();
                    String description=d.child("description").getValue().toString();
                    String sku=d.child("sku").getValue().toString();

                    ShoppingCart shoppingCart=new ShoppingCart(sku,name,description,price,quantity);
                    allProducts.add(shoppingCart);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    private void createOrder(String name, String surname, String phone, String address, String information) {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Consumer consumer = new Consumer(name,surname, phone, address,key,email, information);
        Map<String, Object> infoValues = consumer.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/orders/" + key + "/", infoValues);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        Map<String, Object> childUpdates2 = new HashMap<>();
        for(ShoppingCart sp:allProducts){
            Map<String, Object> products = sp.toMap();
            childUpdates2.put("/supermarkets/supermarket"+keySupermarket+"/"+"/orders/" + key + "/products/"+sp.getSku()+"/", products);
        }


        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates2);
        Toast.makeText(getApplicationContext(), "Order sent successful", Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().getRoot().child("supermarkets").child("supermarket"+keySupermarket).
                child("shoppingCarts").child(key).removeValue();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConfirm:
                String name = editTextName.getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    editTextName.setError("The name must be filled");
                    return;
                }
                String surname = editTextSurname.getText().toString().trim();
                if(TextUtils.isEmpty(surname)) {
                    editTextSurname.setError("The surname must be filled");
                    return;
                }
                String phone = editTextPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    editTextPhone.setError("The phone must be filled");
                    return;
                }
                String address = editTextAddress.getText().toString().trim();
                if(TextUtils.isEmpty(address)) {
                    editTextAddress.setError("The address must be filled");
                    return;
                }
                String information = editTextInformation.getText().toString().trim();
                createOrder(name, surname, phone, address,information);
                Context context2 = view.getContext();
                Intent intent2 = new Intent(context2, SupermarketActivity.class);
                Bundle b2 = new Bundle();

                b2.putString("keySupermarket",keySupermarket);
                intent2.putExtras(b2);
                context2.startActivity(intent2);
                break;
            case R.id.buttonCancel:
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketShoppingCartActivity.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",keySupermarket);
                intent.putExtras(b);
                context.startActivity(intent);
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

