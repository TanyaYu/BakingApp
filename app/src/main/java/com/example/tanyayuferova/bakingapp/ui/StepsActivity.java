package com.example.tanyayuferova.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.entity.Step;

import java.util.List;

public class StepsActivity extends AppCompatActivity implements RecipeStepFragment.OnPageSelectedCallBack {

    public static final String STEPS_EXTRA = "extra.steps";
    public static final String STEP_START_INDEX_EXTRA = "extra.step_item";
    public static final String SELECTED_POSITION = "state.selected_position";
    protected List<Step> steps;
    protected int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        steps = getIntent().getParcelableArrayListExtra(STEPS_EXTRA);

        if(savedInstanceState == null)
            selectedPosition = getIntent().getIntExtra(STEP_START_INDEX_EXTRA, 0);
        else
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_fragment, RecipeStepFragment.newInstance(steps, selectedPosition))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(outState == null)
            outState = new Bundle();
        outState.putInt(SELECTED_POSITION, selectedPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPageSelected(int position) {
        selectedPosition = position;
        // Set Title for current step
        getSupportActionBar().setTitle(steps.get(position).getStepTitle(this));
    }
}
