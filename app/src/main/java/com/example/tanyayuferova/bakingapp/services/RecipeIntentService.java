package com.example.tanyayuferova.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.tanyayuferova.bakingapp.IngredientsWidget;
import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.RecipeWidget;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;

import java.util.List;

/**
 * Created by Tanya Yuferova on 11/14/2017.
 */

public class RecipeIntentService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "action.update_recipe_widgets";
    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "action.update_ingredients_widgets";

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
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }
    /**
     * Starts this service to perform UpdateIngredientsWidgets action with the given parameters
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateRecipeWidgets();
            } else if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                handleActionUpdateIngredientsWidgets();
            }
        }
    }

    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_grid_view);
        IngredientsWidget.updateIngredientsWidgets(this, appWidgetManager, appWidgetIds);
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
