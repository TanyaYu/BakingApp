package com.example.tanyayuferova.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.databinding.ActivityMainBinding;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>  {

    private ActivityMainBinding binding;
    protected RecipesAdapter recipesAdapter;
    private static final int RECIPES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initRecyclerView();
    }

    protected void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvRecipes.setLayoutManager(layoutManager);
        binding.rvRecipes.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter();
        binding.rvRecipes.setAdapter(recipesAdapter);
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {

            List<Recipe> data = null;

            @Override
            protected void onStartLoading() {
                if (data != null) {
                    deliverResult(data);
                } else {
                    binding.progress.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            @Override
            public List<Recipe> loadInBackground() {
                return JsonUtils.getAllRecipes(MainActivity.this);
            }

            public void deliverResult(List<Recipe> data) {
                this.data = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        binding.progress.setVisibility(View.INVISIBLE);
        if (data == null) {
            Toast.makeText(MainActivity.this, getString(R.string.load_error), Toast.LENGTH_LONG)
                    .show();
        }
        recipesAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    public void recipeItemOnClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.RECIPE_ITEM_EXTRA, recipe);
        startActivity(intent);
    }
}
