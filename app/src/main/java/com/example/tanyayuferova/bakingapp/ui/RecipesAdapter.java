package com.example.tanyayuferova.bakingapp.ui;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.databinding.RecipeItemBinding;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private List<Recipe> data;

    public RecipesAdapter() {
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {
        protected final RecipeItemBinding binding;

        public RecipesAdapterViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Recipe item) {
            binding.setRecipe(item);
            binding.setContext((MainActivity) itemView.getContext());
        }
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeItemBinding itemBinding = RecipeItemBinding.inflate(layoutInflater, parent, false);
        return new RecipesAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder holder, int position) {
        Recipe item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public void setData(List<Recipe> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .error(R.drawable.ic_cupcake_color)
                .into(view);
        if(view.getDrawable() == null){
            view.setImageResource(R.drawable.ic_cupcake_color);
        }
    }
}
