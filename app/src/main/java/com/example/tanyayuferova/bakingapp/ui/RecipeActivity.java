package com.example.tanyayuferova.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.databinding.ActivityRecipeBinding;
import com.example.tanyayuferova.bakingapp.entity.Recipe;

public class RecipeActivity extends AppCompatActivity {

    protected ActivityRecipeBinding binding;
    protected IngredientsAdapter ingredientsAdapter;
    protected StepsAdapter stepsAdapter;
    public static final String RECIPE_ITEM_EXTRA = "extra.recipe_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        if(getIntent().hasExtra(RECIPE_ITEM_EXTRA)) {
            binding.setRecipe((Recipe) getIntent().getParcelableExtra(RECIPE_ITEM_EXTRA));
        }

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(binding.getRecipe().getName());

        initIngredientsTable();
        initStepsTable();
    }

    protected void initIngredientsTable() {
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        binding.rvIngredients.setHasFixedSize(true);
        ingredientsAdapter = new IngredientsAdapter(binding.getRecipe().getIngredients());
        binding.rvIngredients.setAdapter(ingredientsAdapter);
    }

    protected void initStepsTable() {
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSteps.setHasFixedSize(true);
        stepsAdapter = new StepsAdapter(binding.getRecipe().getSteps());
        binding.rvSteps.setAdapter(stepsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
