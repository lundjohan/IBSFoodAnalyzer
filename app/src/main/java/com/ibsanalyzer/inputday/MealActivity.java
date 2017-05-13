package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.model.TagTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.TAGS_TO_ADD;

//see p. 124 'Beginning Android' for the inspiraiton for this class
//This Activity starts when user press Meal button, and a meal should be constructed
//It is called from DiaryFragment
public class MealActivity extends EventActivity {
    private Button addTagsBtn;
    private ListView tagNamesList;

    //TODO CHANGE TO=> List<Tag>  -> TagAdderActivity should return a full Tag in json-format (performance-reasons for this, so that there is no need to look up parent twice)
    private List<String>tagNamesAdded;
    private ArrayAdapter<String>adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTagsBtn = (Button)findViewById(R.id.addTagsBtn);
        tagNamesList = (ListView)findViewById(R.id.tagname_listview);
        tagNamesAdded = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagNamesAdded);
        tagNamesList.setAdapter(adapter);


    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(datetime, "green_leaves", 1.7));
        tags.add(new Tag(datetime, "lactiplus", 1.));
        Meal meal = new Meal(datetime, tags, 2.3);

        //Put in database here (Android Studio Development Essentials [ASDE] p. 558, 559)


        //Return value to DiaryFragment here, so that it can be shown visually in list.
        //JSON
        Gson gson = new Gson();
        String mealAsJSON = gson.toJson(meal);
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
        if (requestCode!= TAGS_TO_ADD) {
            return;
        }
        Gson gson = new Gson();
        if (data.hasExtra("returnTagTemplateJSON")) {
            String tagJSONData = data.getExtras().getString("returnTagTemplateJSON");
            TagTemplate tagTemplate = gson.fromJson(tagJSONData, TagTemplate.class);
            tagNamesAdded.add(tagTemplate.get_tagname());
        }
        adapter.notifyDataSetChanged(); // change to RecyclerView.adapter and notifyItemInserted(tagNamesAdded.size() - 1);
    }
}
