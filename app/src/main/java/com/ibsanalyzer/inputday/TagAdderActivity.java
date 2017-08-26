package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.Serializable;

import static com.ibsanalyzer.constants.Constants.CHANGED_EVENT;
import static com.ibsanalyzer.constants.Constants.PUT_TAG_TEMPLATE;
import static com.ibsanalyzer.constants.Constants.TAGTEMPLATE_TO_ADD;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE_OF;

public class TagAdderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView tagSearch;
    ListView tagsList;
    private TagnameCursorAdapter adapter;
    DBHandler dbHandler;
    TagTemplate chosenTagTemplate = null;

    //if calling Activity is a TagTemplateActivity that requests a "type-of" TagTemplate, then
    // this int stores which of type_of is meant. Default is -1 (<0 means other request than above).
    int typeOf  = -1;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_tagtemplate_menu, menu);
        menu.findItem(R.id.menu_add_new).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClickedNew(null);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_adder);

        //handle type of request
        Intent intent = getIntent();
        if (intent.hasExtra(WHICH_TYPE_OF)){
            typeOf = intent.getIntExtra(WHICH_TYPE_OF,-1);
        }

        dbHandler = new DBHandler(this);

        //only in developing mode
        /*dbHandler.deleteAllTagTemplates();
        dbHandler.createSomeTagTemplates();*/

        tagSearch = (SearchView) findViewById(R.id.searchTags);
        tagsList = (ListView) findViewById(R.id.listOfTags);
        Cursor cursor = dbHandler.getCursorToTagTemplates();
        adapter = new TagnameCursorAdapter(this,
                cursor);
        tagsList.setAdapter(adapter);
        tagsList.setTextFilterEnabled(true);
        setupSearchView();
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return dbHandler.fetchTagTemplatesByName(constraint.toString());
            }
        });

        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //good explanation: http://stackoverflow
            // .com/questions/8222229/what-is-the-difference-between-int-and-long-argument-in
            // -onitemclick-in-andro
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenTagTemplate = dbHandler.findTagTemplate((int) id);
                Log.d("Debug", "onItemClick position: " + position);
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
        Intent data = new Intent();
        data.putExtra( Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE, (Serializable)chosenTagTemplate);
        if (typeOf>=0){
            data.putExtra(WHICH_TYPE,typeOf);
        }
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void doneClickedNew(View view) {
        Intent intent = new Intent(this, TagTemplateAdderActivity.class);
        startActivityForResult(intent, TAGTEMPLATE_TO_ADD);

    }

    /**
     * Has gotten result from clicked new (plus button) for adding new TagTemplate
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.hasExtra(PUT_TAG_TEMPLATE)) {
            //update adapter to next time
            adapter.notifyDataSetChanged();

            //return the tagTemplate that has been added.
            chosenTagTemplate = (TagTemplate) data.getSerializableExtra(PUT_TAG_TEMPLATE);
            finish();
        }
    }
}
