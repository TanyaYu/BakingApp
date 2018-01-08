package com.example.tanyayuferova.bakingapp.ui.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.services.RecipeIntentService;
import com.example.tanyayuferova.bakingapp.ui.adapters.RecipesAdapter;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

/**
 * To set up recipe for ingredients widget
 *
 * Created by Tanya Yuferova on 11/20/2017.
 */

public class IngredientsWidgetConfigActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>, RecipesAdapter.OnClickRecipesHandler {

    protected RecipesAdapter recipesAdapter;
    private static final int RECIPES_LOADER_ID = 11;
    private RecyclerView recyclerView;

    private int widgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_configuration);
        recyclerView = findViewById(R.id.rv_recipes);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        initRecyclerView();
    }

    protected void initRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(this);
        recyclerView.setAdapter(recipesAdapter);
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, IngredientsWidgetConfigActivity.this);
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
        if (data == null) {
            Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_LONG)
                    .show();
        }
        recipesAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onClickRecipe(Recipe recipe) {
        RecipeIntentService.startActionUpdateIngredientsWidget(this, widgetId, recipe);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(Activity.RESULT_OK, resultValue);
        finish();
    }
}
