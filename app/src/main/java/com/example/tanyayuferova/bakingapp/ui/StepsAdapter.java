package com.example.tanyayuferova.bakingapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.tanyayuferova.bakingapp.databinding.StepItemBinding;
import com.example.tanyayuferova.bakingapp.entity.Step;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/10/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private List<Step> data;

    public StepsAdapter() {

    }

    public StepsAdapter(List<Step> data) {
        this.data = data;
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder {
        protected final StepItemBinding binding;

        public StepsAdapterViewHolder(StepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Step item) {
            binding.setStep(item);
            binding.setContext((RecipeActivity) itemView.getContext());
        }
    }

    @Override
    public StepsAdapter.StepsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        StepItemBinding itemBinding = StepItemBinding.inflate(layoutInflater, parent, false);
        return new StepsAdapter.StepsAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsAdapterViewHolder holder, int position) {
        Step item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public void setData(List<Step> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
