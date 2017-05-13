package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.model.TagTemplate;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.tag;
import static com.ibsanalyzer.constants.Constants.TAGS_TO_ADD;
import static com.ibsanalyzer.inputday.R.drawable.meal;

//see p. 124 'Beginning Android' for the inspiraiton for this class
//This Activity starts when user press Meal button, and a meal should be constructed
//It is called from DiaryFragment
public class MealActivity extends EventActivity {
    private Button addTagsBtn;

    private List<Tag> tagsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TagAdapter adapter;
    private TextView portionView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portionView = (TextView) findViewById(R.id.portions);


        addTagsBtn = (Button) findViewById(R.id.addTagsBtn);
        recyclerView = (RecyclerView) findViewById(R.id.addedTagsView);
        layoutManager = new LinearLayoutManager(this);
        tagsList = new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TagAdapter(tagsList, this);
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }

    @Override
    public void finish() {
        //create meal
        double portions = Double.parseDouble((String) portionView.getText());
        Meal meal = new Meal(datetime, tagsList, portions);

        //Put in database here (Android Studio Development Essentials [ASDE] p. 558, 559)


        //Return value to DiaryFragment here, so that it can be shown visually in list.
        //JSON
        Gson gson = new Gson();
        String mealAsJSON = gson.toJson(meal);
        Intent data = new Intent();
        data.putExtra("returnMealJSON", mealAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }


    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        Gson gson = new Gson();
        if (data.hasExtra("returnTagTemplateJSON")) {
            String tagJSONData = data.getExtras().getString("returnTagTemplateJSON");
            TagTemplate tagTemplate = gson.fromJson(tagJSONData, TagTemplate.class);

            //create a new
            Tag tag = new Tag(datetime, tagTemplate.get_tagname(), 1.0);
            tagsList.add(tag);
        }
        adapter.notifyItemInserted(tagsList.size() - 1);
    }
}
