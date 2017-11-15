package com.example.tanyayuferova.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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

    public RecipeIntentService() {
        super("RecipeIntentService");
    }
    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateRecipeWidgets();
            }
        }
    }

    private void handleActionUpdateRecipeWidgets() {
        List<Recipe> allRecipes = JsonUtils.getAllRecipes(this);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        // Update all widgets
        if(allRecipes != null && allRecipes.size() > 0)
            RecipeWidget.updateRecipeWidgets(this, appWidgetManager, allRecipes.get(0), appWidgetIds);
    }
}
