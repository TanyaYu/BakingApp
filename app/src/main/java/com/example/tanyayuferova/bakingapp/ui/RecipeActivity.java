package com.example.tanyayuferova.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Ingredient;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.entity.Step;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements RecipeStepFragment.OnPageSelectedCallBack {

    protected Toast measureHintToast;
    public static final String RECIPE_ITEM_EXTRA = "extra.recipe_item";
    public static final String SELECTED_POSITION = "state.selected_position";
    /* True if master detail flow */
    private boolean twoPane = false;
    protected Recipe item;
    /* Selected step (if steps fragment included) */
    protected int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        item = getIntent().getParcelableExtra(RECIPE_ITEM_EXTRA);
        getSupportActionBar().setTitle(item.getName());

        // Master fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.master_list_fragment, RecipeMasterFragment.newInstance(item))
                .commit();

        if(findViewById(R.id.step_fragment) != null) {
            twoPane = true;

            if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_POSITION)) {
                selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);
            }
            // Steps fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment, RecipeStepFragment.newInstance(item.getSteps(), selectedPosition))
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(outState == null)
            outState = new Bundle();
        outState.putInt(SELECTED_POSITION, selectedPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
            selectedPosition = item.getSteps().indexOf(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment, RecipeStepFragment.newInstance(item.getSteps(), selectedPosition))
                    .commit();
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            intent.putExtra(StepsActivity.STEP_START_INDEX_EXTRA, item.getSteps().indexOf(step));
            intent.putParcelableArrayListExtra(StepsActivity.STEPS_EXTRA, new ArrayList<>(item.getSteps()));
            startActivity(intent);
        }
    }

    public void ingredientMeasureOnClick(Ingredient ingredient) {
        if(measureHintToast != null) {
            measureHintToast.cancel();
        }
        measureHintToast = Toast.makeText(this, ingredient.getMeasureDescription(this), Toast.LENGTH_LONG);
        measureHintToast.show();
    }

    public void ingredientCheckedOnCLick(Ingredient ingredient) {
        // Update ingredient
        item.getIngredients().set(item.getIngredients().indexOf(ingredient), ingredient);
    }

    @Override
    public void onPageSelected(int position) {
        selectedPosition = position;
    }
}
