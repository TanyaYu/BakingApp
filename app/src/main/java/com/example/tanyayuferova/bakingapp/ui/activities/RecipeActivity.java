package com.example.tanyayuferova.bakingapp.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Ingredient;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.entity.Step;
import com.example.tanyayuferova.bakingapp.ui.fragments.RecipeMasterFragment;
import com.example.tanyayuferova.bakingapp.ui.fragments.RecipeStepFragment;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity
        implements RecipeStepFragment.OnPageSelectedCallBack {

    private Snackbar measureHintBar;
    private CoordinatorLayout coordinatorLayout;
    public static final String RECIPE_ITEM_EXTRA = "extra.recipe_item";
    /* True if master detail flow */
    private boolean twoPane = false;
    protected Recipe item;
    private RecipeStepFragment stepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        item = getIntent().getParcelableExtra(RECIPE_ITEM_EXTRA);
        getSupportActionBar().setTitle(item.getName());

        // Master fragment
        if(getSupportFragmentManager().findFragmentById(R.id.master_list_fragment) == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.master_list_fragment, RecipeMasterFragment.newInstance(item))
                    .commit();

        if(getResources().getBoolean(R.bool.isTablet)) {
            twoPane = true;
            // Steps fragment
            stepFragment = (RecipeStepFragment) getSupportFragmentManager().findFragmentById(R.id.step_fragment);
            if(stepFragment==null) {
                stepFragment = RecipeStepFragment.newInstance(item.getSteps(), 0);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_fragment, stepFragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        // Play action is enable only for one pane view
        menu.findItem(R.id.action_play_recipe).setVisible(!twoPane);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_play_recipe) {
            // Start playing recipe with the first
            if(this.item.getSteps().size() > 0)
                stepItemOnClick(this.item.getSteps().get(0));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * If master detail flow view enable then update steps fragment. Else show steps in new screen
     * @param step
     */
    public void stepItemOnClick(Step step) {
        if(twoPane) {
            stepFragment.setViewPagerPosition(item.getSteps().indexOf(step));
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(StepsActivity.STEP_START_INDEX_EXTRA, item.getSteps().indexOf(step));
            intent.putParcelableArrayListExtra(StepsActivity.STEPS_EXTRA, new ArrayList<>(item.getSteps()));
            startActivity(intent);
        }
    }

    public void ingredientMeasureOnClick(Ingredient ingredient) {
        if(measureHintBar != null)
            measureHintBar.dismiss();
        measureHintBar = Snackbar.make(coordinatorLayout, ingredient.getMeasureDescription(this), Snackbar.LENGTH_LONG);
        TextView textView = measureHintBar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorTextPrimaryInverse));
        measureHintBar.show();
    }

    public void ingredientCheckedOnCLick(Ingredient ingredient) {
        // Update ingredient
        item.getIngredients().set(item.getIngredients().indexOf(ingredient), ingredient);
    }

    @Override
    public void onPageSelected(int position) {
    }
}
