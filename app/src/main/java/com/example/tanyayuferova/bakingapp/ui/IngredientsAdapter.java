package com.example.tanyayuferova.bakingapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.tanyayuferova.bakingapp.databinding.IngredientItemBinding;
import com.example.tanyayuferova.bakingapp.entity.Ingredient;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/10/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> data;

    public IngredientsAdapter() {
    }

    public IngredientsAdapter(List<Ingredient> data){
        this.data = data;
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        protected final IngredientItemBinding binding;

        public IngredientsAdapterViewHolder(IngredientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient item) {
            binding.setIngredient(item);
        }
    }

    @Override
    public IngredientsAdapter.IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientItemBinding itemBinding = IngredientItemBinding.inflate(layoutInflater, parent, false);
        return new IngredientsAdapter.IngredientsAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsAdapterViewHolder holder, int position) {
        Ingredient item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
