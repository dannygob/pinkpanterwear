package com.example.pinkpanterwear.pinkpanterwear;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinkpanterwear.R;
import com.example.slickkwear.Model.Categories;
import com.example.slickkwear.ViewHolder.CategoryCategoryViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserCategoryFragment extends Fragment {

    private RecyclerView recyclerViewCategory;
    private RecyclerView.LayoutManager layoutManagerCategories;
    private FirebaseFirestore categoryRef;
    private Query queryCategory;
    private View view;


    public UserCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_category, container, false);

        categoryRef = FirebaseFirestore.getInstance();
        queryCategory = categoryRef.collection("Categories");

        recyclerViewCategory = view.findViewById(R.id.recycler_view_user_category);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setNestedScrollingEnabled(false);
        layoutManagerCategories = new GridLayoutManager(getContext(), 2);
        recyclerViewCategory.setLayoutManager(layoutManagerCategories);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<Categories> options = new FirestoreRecyclerOptions.Builder<Categories>()
                .setQuery(queryCategory, Categories.class)
                .build();

        FirestoreRecyclerAdapter<Categories, CategoryCategoryViewHolder> adapter = new FirestoreRecyclerAdapter<Categories, CategoryCategoryViewHolder>(options) {
            @NonNull
            @Override
            public CategoryCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_category_category_layout, parent, false);
                return new CategoryCategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryCategoryViewHolder holder, int position, @NonNull Categories model) {
                holder.txtCategoryName.setText(model.getCategoryName());
                Picasso.get().load(model.getCategoryImage()).into(holder.txtCategoryImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), AdminProductsDetailsActivity.class);
                        intent.putExtra("ProductUniqueID", model.getCategoryUniqueID());
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerViewCategory.setAdapter(adapter);
        adapter.startListening();
    }
}