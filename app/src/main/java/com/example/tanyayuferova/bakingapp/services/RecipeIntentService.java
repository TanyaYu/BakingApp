package com.example.tanyayuferova.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.tanyayuferova.bakingapp.ui.widgets.IngredientsWidget;
import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.ui.widgets.RecipeWidget;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/14/2017.
 */

public class RecipeIntentService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGET = "action.update_recipe_widget";
    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "action.update_ingredients_widgets";
    public static final String EXTRA_RECIPE_ITEM_ID = "extra.recipe_id";
    public static final String EXTRA_RECIPE_ITEM_NAME = "extra.recipe_name";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }
    /**
     * Starts this service to perform UpdateRecipeWidgets action with the given parameters
     *
     * @see IntentService
     */
    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }
    /**
     * Starts this service to perform UpdateIngredientsWidgets action with the given parameters
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredientsWidget(Context context, int widgetId, Recipe recipe) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra(EXTRA_RECIPE_ITEM_ID, recipe.getId());
        intent.putExtra(EXTRA_RECIPE_ITEM_NAME, recipe.getName());
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGET.equals(action)) {
                handleActionUpdateRecipeWidgets();
            } else if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                int recipeId = intent.getIntExtra(EXTRA_RECIPE_ITEM_ID, 0);
                String recipeName = intent.getStringExtra(EXTRA_RECIPE_ITEM_NAME);
                handleActionUpdateIngredientsWidget(widgetId, recipeId, recipeName);
            }
        }
    }

    private void handleActionUpdateIngredientsWidget(int widgetId, int recipeId, String recipeName) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.ingredients_widget_grid_view);
        IngredientsWidget.updateIngredientsWidget(this, appWidgetManager, widgetId, recipeId, recipeName);
    }

    private void handleActionUpdateRecipeWidgets() {
        List<Recipe> allRecipes = JsonUtils.getAllRecipes();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        // Update all widgets
        if(allRecipes != null && allRecipes.size() > 0)
            RecipeWidget.updateRecipeWidgets(this, appWidgetManager, allRecipes.get(0), appWidgetIds);
    }
}
