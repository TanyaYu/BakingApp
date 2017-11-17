package com.example.tanyayuferova.bakingapp.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.ui.RecipeActivity;
import com.example.tanyayuferova.bakingapp.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tanya Yuferova on 11/14/2017.
 */

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Recipe> data;
    // How many recipes to display
    int display = 4;

    public GridRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Display only several first recipes
        data = JsonUtils.getAllRecipes();
        if (data.size() > display)
            data = data.subList(0, display);
    }

    @Override
    public void onDestroy() {
        data = null;
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (data == null)
            return null;

        Recipe recipe = data.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(context).load(recipe.getImage()).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Recipe preview image
        if (bitmap == null)
            views.setImageViewResource(R.id.iv_recipe_preview, R.drawable.ic_cupcake_back);
        else
            views.setImageViewBitmap(R.id.iv_recipe_preview, bitmap);
        // Recipe name
        views.setTextViewText(R.id.tv_recipe_name, recipe.getName());

        // Pending intent when click on image
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeActivity.RECIPE_ITEM_EXTRA, recipe);
        views.setOnClickFillInIntent(R.id.iv_recipe_preview, fillInIntent);

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
