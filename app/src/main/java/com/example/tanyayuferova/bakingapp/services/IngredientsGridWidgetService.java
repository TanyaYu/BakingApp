package com.example.tanyayuferova.bakingapp.services;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Ingredient;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.ui.RecipeActivity;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/20/2017.
 */

public class IngredientsGridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsGridRemoteViewsFactory(this.getApplicationContext());
    }
}

class IngredientsGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    Recipe recipe;

    public IngredientsGridRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        List<Recipe> allRecipes = JsonUtils.getAllRecipes();
        if (allRecipes != null && allRecipes.size() > 0) {
            recipe = allRecipes.get(0);
        }
    }

    @Override
    public void onDestroy() {
        recipe = null;
    }

    @Override
    public int getCount() {
        if (recipe == null)
            return 0;
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipe == null)
            return null;

        Ingredient ingredient = recipe.getIngredients().get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_item);

        // Ingredient measure
        int imageResource = ingredient.getMeasureIdResource(context);
        if(imageResource == 0)
            views.setViewVisibility(R.id.iv_measure, View.GONE);
        else views.setImageViewResource(R.id.iv_measure, imageResource);

        // Ingredient name
        views.setTextViewText(R.id.tv_description, ingredient.getName());
        // Ingredient quantity
        views.setTextViewText(R.id.tv_ingredient_quantity, String.valueOf(ingredient.getQuantity()));

        // Pending intent
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeActivity.RECIPE_ITEM_EXTRA, recipe);
        views.setOnClickFillInIntent(R.id.layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
