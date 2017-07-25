package com.sovereign.supermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.model.Category;
import com.sovereign.supermarket.model.Consumer;
import com.sovereign.supermarket.model.ShoppingCart;

import java.util.HashMap;
import java.util.Map;


public class SupermarketAddCategoryActivity extends AppCompatActivity implements View.OnClickListener{


    Button buttonAddCategory, buttonCancelCategory;
    EditText editTextNameCategory, editTextDescriptionCategory;
    private String keySupermarket;
    private DatabaseReference dbCategory;
    Long numberCategories;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Bundle bundle = this.getIntent().getExtras();

        keySupermarket = bundle.getString("keySupermarket");

        buttonAddCategory = (Button) findViewById(R.id.buttonAddCategory);
        buttonCancelCategory = (Button) findViewById(R.id.buttonCancelCategory);
        editTextNameCategory = (EditText) findViewById(R.id.nameNewCategory);
        editTextDescriptionCategory = (EditText) findViewById(R.id.descriptionNewCategory);

        buttonAddCategory.setOnClickListener(this);
        buttonCancelCategory.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        actionBar.setTitle("Create new category");
        dbCategory = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket"+keySupermarket).child("categories");

        dbCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    System.out.println("NUUUUUMBER EQWQEQWE");
                    numberCategories = dataSnapshot.getChildrenCount();
                    System.out.println("NUUUUUMBER"+numberCategories);
                }else{
                    numberCategories=0L;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }




    private void createCategory(String name, String description) {
        System.out.println("EEERRRRRROORRR 11111");
        Category category = new Category(name,description);
        System.out.println("EEERRRRRROORRR 2222");
        Map<String, Object> infoCategory = category.toMap();
        System.out.println("EEERRRRRROORRR 33333");

        Map<String, Object> childUpdates = new HashMap<>();
        String keyCategory;
        Long sum=numberCategories+1;
        System.out.println("EEERRRRRROORRR 3233"+sum);
        if(numberCategories<10){
            keyCategory="0"+sum.toString();
        }else{
            keyCategory=sum.toString();
        }
        System.out.println("Holaa");

        childUpdates.put("/supermarkets/supermarket"+keySupermarket+"/"+"/categories/"+keyCategory+"/", infoCategory);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        Toast.makeText(getApplicationContext(), "Category created", Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddCategory:
                String name = editTextNameCategory.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    editTextNameCategory.setError("The name must be filled");
                    return;
                }
                String description = editTextDescriptionCategory.getText().toString();
                if(TextUtils.isEmpty(description)) {
                    editTextDescriptionCategory.setError("The description must be filled");
                    return;
                }
                createCategory(name, description);
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketActivity.class);
                Bundle b = new Bundle();

                b.putString("keySupermarket",keySupermarket);
                b.putBoolean("admin",true);
                intent.putExtras(b);
                context.startActivity(intent);
                break;
            case R.id.buttonCancelCategory:
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

