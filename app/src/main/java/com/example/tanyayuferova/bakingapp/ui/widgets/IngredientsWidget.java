package com.example.tanyayuferova.bakingapp.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.services.IngredientsGridWidgetService;
import com.example.tanyayuferova.bakingapp.services.RecipeIntentService;
import com.example.tanyayuferova.bakingapp.ui.activities.RecipeActivity;

/**
 * Ingredients widget displays ingredients list for selected recipe
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager,
                                               int appWidgetId, int recipeId, String recipeName) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_recipe_title, recipeName);

        Intent intent = new Intent(context, IngredientsGridWidgetService.class);
        intent.putExtra(RecipeIntentService.EXTRA_RECIPE_ITEM_ID, recipeId);

        views.setRemoteAdapter(R.id.ingredients_widget_grid_view, intent);

        Intent appIntent = new Intent(context, RecipeActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, appWidgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.ingredients_widget_grid_view, appPendingIntent);

        views.setEmptyView(R.id.ingredients_widget_grid_view, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }
}
