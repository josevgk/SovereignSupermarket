package com.sovereign.supermarket.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.activity.SupermarketAddCategoryActivity;
import com.sovereign.supermarket.activity.SupermarketItemsActivity;
import com.sovereign.supermarket.activity.SupermarketProductsOrderActivity;
import com.sovereign.supermarket.holder.CategoryHolder;
import com.sovereign.supermarket.model.Category;


public class SupermarketProductsFragment extends Fragment {

    private static final String TAGLOG = "firebase-db";

    private DatabaseReference dbSupermarketCategories;
    private String keySupermarket;
    private Button newCategory;
    private FirebaseRecyclerAdapter mAdapterCategory;
    private FirebaseRecyclerAdapter mAdapterItem;

    public static SupermarketProductsFragment newInstance(String keySupermarket, String keyCategory) {
        SupermarketProductsFragment fragment = new SupermarketProductsFragment();
        Bundle b = new Bundle();
        b.putString("keySupermarket", keySupermarket);
        b.putString("keyCategory", keyCategory);
        fragment.setArguments(b);

        return fragment;
    }


    public SupermarketProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        keySupermarket = b.getString("keySupermarket");
        dbSupermarketCategories = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("categories");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View viewFragment = inflater.inflate(R.layout.fragment_supermarket_products, container, false);

        RecyclerView recycler = (RecyclerView) viewFragment.findViewById(R.id.listCategories);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        newCategory=(Button) viewFragment.findViewById(R.id.buttonNewCategory);
        Bundle bundle = getActivity().getIntent().getExtras();

        final Boolean isAdmin = bundle.getBoolean("admin");
        if(isAdmin==true){
            newCategory.setVisibility(View.VISIBLE);
            newCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SupermarketAddCategoryActivity.class);
                    Bundle b = new Bundle();

                    System.out.println(keySupermarket);
                    b.putString("keySupermarket",keySupermarket);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }

        mAdapterCategory =
                new FirebaseRecyclerAdapter<Category, CategoryHolder>(
                        Category.class, R.layout.list_category, CategoryHolder.class, dbSupermarketCategories) {

                    @Override
                    public void populateViewHolder(CategoryHolder categoryHolder, Category category, int position) {
                        categoryHolder.setName(category.getName());
                        categoryHolder.setDescription(category.getDescription());

                        final String category_ = category.getName();

                        categoryHolder.getCategory().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), SupermarketItemsActivity.class);
                                Bundle b = new Bundle();
                                b.putString("keySupermarket", keySupermarket);
                                b.putString("keyCategory", category_);
                                b.putBoolean("admin",isAdmin);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
                    }
                };
        recycler.setAdapter(mAdapterCategory);

        return viewFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapterCategory.cleanup();
    }
}