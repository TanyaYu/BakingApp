package com.example.tanyayuferova.bakingapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanyayuferova.bakingapp.databinding.FragmentRecipeMasterBinding;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.ui.adapters.StepsAdapter;
import com.example.tanyayuferova.bakingapp.ui.adapters.IngredientsAdapter;

/**
 * Created by Tanya Yuferova on 11/13/2017.
 */

public class RecipeMasterFragment extends Fragment {

    protected FragmentRecipeMasterBinding binding;
    protected IngredientsAdapter ingredientsAdapter;
    protected StepsAdapter stepsAdapter;
    public static final String RECIPE_ITEM_ARGUMENT = "arg.recipe_item";

    public RecipeMasterFragment() {
    }

    public static RecipeMasterFragment newInstance(Recipe item) {
        RecipeMasterFragment fragment = new RecipeMasterFragment();
        fragment.setArguments(new Bundle());
        fragment.getArguments().putParcelable(RECIPE_ITEM_ARGUMENT, item);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeMasterBinding.inflate(inflater, container, false);
        Recipe item = getArguments().getParcelable(RECIPE_ITEM_ARGUMENT);
        binding.setRecipe(item);

        initIngredientsTable();
        initStepsTable();

        return binding.getRoot();
    }


    protected void initIngredientsTable() {
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIngredients.setHasFixedSize(true);
        ingredientsAdapter = new IngredientsAdapter(binding.getRecipe().getIngredients());
        binding.rvIngredients.setAdapter(ingredientsAdapter);
        binding.rvIngredients.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    protected void initStepsTable() {
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSteps.setHasFixedSize(true);
        stepsAdapter = new StepsAdapter(binding.getRecipe().getSteps());
        binding.rvSteps.setAdapter(stepsAdapter);
    }
}
