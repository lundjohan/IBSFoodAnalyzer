package com.ibsanalyzer.inputday;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import com.ibsanalyzer.constants.Constants;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.database.TagnameCursorAdapter;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TagAdderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    SearchView tagSearch;
    ListView tagsList;
    private TagnameCursorAdapter adapter;
    private List<String> tagNames = new ArrayList<>();
    DBHandler dbHandler;
    TagTemplate chosenTagTemplate =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_adder);
        dbHandler = new DBHandler(this);

        //only in developing mode
        /*dbHandler.deleteAllTagTemplates();
        dbHandler.createSomeTagTemplates();*/

        tagSearch = (SearchView) findViewById(R.id.searchTags);
        tagsList = (ListView)findViewById(R.id.listOfTags);
        Cursor cursor = dbHandler.getCursorToTagTemplates();
        adapter = new TagnameCursorAdapter(this,
                cursor);
        tagsList.setAdapter(adapter);
        tagsList.setTextFilterEnabled(true);
        setupSearchView();
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHandler.fetchTagTemplatesByName(constraint.toString());
            }
        });

        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            //good explanation: http://stackoverflow.com/questions/8222229/what-is-the-difference-between-int-and-long-argument-in-onitemclick-in-andro
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenTagTemplate = dbHandler.findTagTemplate((int)id);
                Log.d("Debug","onItemClick position: "+position);
                finish();
            }
        });

    }

    private void setupSearchView() {
        tagSearch.setIconifiedByDefault(false);
        tagSearch.setOnQueryTextListener(this);
        tagSearch.setSubmitButtonEnabled(false);
        //tagSearch.setQueryHint(getString(R.string.cheese_hunt_hint));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText.toString());
        return true;
    }
    @Override
    public void finish() {
        TagTemplate tagTemplate = chosenTagTemplate;
        Util.returnNewEvent(tagTemplate, Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE, this);
        super.finish();
    }

}
