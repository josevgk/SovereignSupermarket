package com.sovereign.supermarket.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sovereign.supermarket.DividerItemDecoration;
import com.sovereign.supermarket.R;
import com.sovereign.supermarket.activity.SupermarketAddCategoryActivity;
import com.sovereign.supermarket.activity.SupermarketAddReviewActivity;
import com.sovereign.supermarket.activity.SupermarketItemsActivity;
import com.sovereign.supermarket.holder.CategoryHolder;
import com.sovereign.supermarket.holder.ReviewHolder;
import com.sovereign.supermarket.model.Category;
import com.sovereign.supermarket.model.Review;


public class SupermarketReviewFragment extends Fragment {

    private static final String TAGLOG = "firebase-db";

    private DatabaseReference dbSupermarketReviews;
    private ValueEventListener eventListenerInfo;
    private View viewFragment;
    private String keySupermarket;
    private TextView lblName;
    private TextView lblAddress;
    private TextView lblEmail;
    private Button newReview;
    private FirebaseRecyclerAdapter mAdapterReview;


    public static SupermarketReviewFragment newInstance(String keySupermarket) {
        SupermarketReviewFragment fragment = new SupermarketReviewFragment();
        Bundle b = new Bundle();
        b.putString("keySupermarket", keySupermarket);
        fragment.setArguments(b);

        return fragment;
    }


    public SupermarketReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        keySupermarket = b.getString("keySupermarket");
        dbSupermarketReviews = FirebaseDatabase.getInstance().getReference().child("supermarkets").child("supermarket" + keySupermarket).child("reviews");
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View viewFragment = inflater.inflate(R.layout.fragment_supermarket_reviews, container, false);

        RecyclerView recycler = (RecyclerView) viewFragment.findViewById(R.id.listReviews);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        newReview=(Button) viewFragment.findViewById(R.id.buttonNewReview);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            newReview.setVisibility(View.INVISIBLE);
        }
        Bundle bundle = getActivity().getIntent().getExtras();

        newReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SupermarketAddReviewActivity.class);
                Bundle b = new Bundle();

                System.out.println(keySupermarket);
                b.putString("keySupermarket",keySupermarket);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        mAdapterReview =
                new FirebaseRecyclerAdapter<Review, ReviewHolder>(
                        Review.class, R.layout.list_reviews, ReviewHolder.class, dbSupermarketReviews) {

                    @Override
                    public void populateViewHolder(ReviewHolder reviewHolder, Review review, int position) {
                        reviewHolder.setName(review.getName());
                        reviewHolder.setText(review.getText());
                        reviewHolder.setStars(review.getStars().toString());
                        reviewHolder.setDate(review.getDate().toString());


                    }
                };
        recycler.setAdapter(mAdapterReview);

        return viewFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapterReview.cleanup();
    }
}