package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

//see p. 124 'Beginning Android' for the inspiraiton for this class
public class MealActivity extends AppCompatActivity implements TextWatcher {
    private AutoCompleteTextView inputTag;
    private TextView suggestions;
    private Button doneBtn;
    private static final String[] tagsFromDB = {"milk", "yoghurt", "spinach", "oats"};
    private Meal meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        inputTag = (AutoCompleteTextView) findViewById(R.id.inputTag);
        suggestions = (TextView)findViewById(R.id.mealTagSuggestions);
        doneBtn = (Button)findViewById(R.id.doneBtn);
        inputTag.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,tagsFromDB));

    }

    public void doneClicked(View view){
        //ev samla ihop data här.

        finish();
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        LocalDateTime time = LocalDateTime.now();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(time, "green_leaves", 1.7));
        Meal meal = new Meal(time, tags, 2.3);

        //JSON
        Gson gson = new Gson();
        String mealAsJSON = gson.toJson(meal);
        data.putExtra("returnMealJSON", mealAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
    private Meal inputsToMeal(){
        return null;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //inputTag.getText is more complex than what it seems -> it effectively uses the tagsFromDB array (I think)
        suggestions.setText(inputTag.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
