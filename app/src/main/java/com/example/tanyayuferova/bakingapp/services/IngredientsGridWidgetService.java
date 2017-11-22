package com.example.tanyayuferova.bakingapp.services;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Ingredient;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.ui.activities.RecipeActivity;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.text.DecimalFormat;

/**
 * Created by Tanya Yuferova on 11/20/2017.
 */

public class IngredientsGridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsGridRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class IngredientsGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Recipe recipe;
    private int recipeId;

    public IngredientsGridRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        recipeId = intent.getIntExtra(RecipeIntentService.EXTRA_RECIPE_ITEM_ID, 0);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = JsonUtils.getRecipe(recipeId);
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
        views.setTextViewText(R.id.tv_ingredient_quantity, new DecimalFormat("#.##").format(ingredient.getQuantity()));

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
