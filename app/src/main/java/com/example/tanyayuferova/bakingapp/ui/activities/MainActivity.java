package com.example.tanyayuferova.bakingapp.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.databinding.ActivityMainBinding;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.ui.adapters.RecipesAdapter;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipesAdapter.OnClickRecipesHandler {

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
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int columns;
        if(size.x < 1100)
            columns = 1;
        else if(size.x < 1800)
            columns = 2;
        else if(size.x < 2600)
            columns = 3;
        else columns = 4;

        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        binding.rvRecipes.setLayoutManager(layoutManager);
        binding.rvRecipes.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(this);
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
                return JsonUtils.getAllRecipes();
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

    @Override
    public void onClickRecipe(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.RECIPE_ITEM_EXTRA, recipe);
        startActivity(intent);
    }
}
