package com.johanlund.ibsfoodanalyzer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import com.johanlund.constants.Constants;
import com.johanlund.database.DBHandler;
import com.johanlund.database.TagnameCursorAdapter;
import com.johanlund.info.ActivityInfoContent;
import com.johanlund.model.TagType;

import java.io.Serializable;

import static com.johanlund.constants.Constants.IDS_OF_EDITED_TAG_TEMPLATES;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.PUT_TAG_TEMPLATE;
import static com.johanlund.constants.Constants.TAGNAME_SEARCH_STRING;
import static com.johanlund.constants.Constants.TAGTEMPLATE_TO_ADD;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_ID;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_TO_EDIT;
import static com.johanlund.constants.Constants.TITLE_STRING;
import static com.johanlund.constants.Constants.WHICH_TYPE;

/**
 * This is the acticity that is seen when in Meal-, Other- or ExerciseActivity button "Add Tags"
 * is pressed
 */
public class TagAdderActivity extends AppCompatActivity implements SearchView
        .OnQueryTextListener, TagnameCursorAdapter.ChangingTagTemplate {
    final String TAG_TITLE = "Add Tag";
    SearchView tagSearch;
    ListView tagsList;
    DBHandler dbHandler;
    TagType chosenTagType = null;
    //if calling Activity is a TagTypeActivity that requests a "type-of" TagType, then
    // this int stores which of type_of is meant. Default is -1 (<0 means other request than above).
    int typeOf = -1;
    private TagnameCursorAdapter adapter;

    //used for backPress
    private boolean tagTemplateHasBeenEditedOrDeleted;
    private long[] idsOfChangedTagTemplates;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ActivityInfoContent.class);
                intent.putExtra(LAYOUT_RESOURCE, R.layout.info_add_tag);
                intent.putExtra(TITLE_STRING, TAG_TITLE);
                startActivity(intent);
                return true;
            }
        });
        inflater.inflate(R.menu.add_tagtemplate_menu, menu);
        menu.findItem(R.id.menu_add_new).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClickedNew();
                return true;
            }
        });


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_adder);
        setTitle(TAG_TITLE);
        //handle type of request
        dbHandler = new DBHandler(this);
        //only in developing mode
        /*dbHandler.deleteAllTagTemplates();
        dbHandler.createSomeTagTemplates();*/
        tagSearch = (SearchView) findViewById(R.id.searchTags);
        if (dbHandler.tagTypesTableIsEmpty()) {
            findViewById(R.id.infoAboutAddingNewTagType).setVisibility(View.VISIBLE);
            tagSearch.setVisibility(View.GONE);
        }
        //else, still initiate all of this. It will crash otherwise when returning from TagType
        // (adapter needs to be initiated)
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
        /**
         * short click on item in tagtemplate list => item is chosen.
         */
        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //good explanation: http://stackoverflow
            // .com/questions/8222229/what-is-the-difference-between-int-and-long-argument-in
            // -onitemclick-in-andro
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenTagType = dbHandler.findTagTemplate((int) id);
                returnTag();
            }
        });

    }

    private void returnTag() {
        Intent data = new Intent();
        data.putExtra(Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE, (Serializable) chosenTagType);
        if (typeOf >= 0) {
            data.putExtra(WHICH_TYPE, typeOf);
        }
        if (tagTemplateHasBeenEditedOrDeleted) {
            data.putExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED, true);
        }
        setResult(RESULT_OK, data);
        finish();
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

    public void doneClickedNew() {
        Intent intent = new Intent(this, TagTypeAdderActivity.class);
        String searchStr = tagSearch.getQuery().toString();
        intent.putExtra(TAGNAME_SEARCH_STRING, searchStr);
        startActivityForResult(intent, TAGTEMPLATE_TO_ADD);
    }

    /**
     * Has gotten result from clicked new (plus button) for adding new TagType
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        //code coming back from TagTypeEditActivity
        if (requestCode == TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED) {
            if (data.hasExtra(IDS_OF_EDITED_TAG_TEMPLATES)) {
                idsOfChangedTagTemplates = data.getLongArrayExtra(IDS_OF_EDITED_TAG_TEMPLATES);
            }
            tagTemplateHasBeenEditedOrDeleted = true;
            updateListView();
        }

        if (data.hasExtra(PUT_TAG_TEMPLATE)) {
            //update adapter to next time
            adapter.notifyDataSetChanged();

            //return the tagTemplate that has been added.
            chosenTagType = (TagType) data.getSerializableExtra(PUT_TAG_TEMPLATE);
            returnTag();
        }

    }

    //there is no notifyDataItemChanged for a CursorAdapter. One alternativ is to update the cursor:
    //see https://stackoverflow.com/questions/13953171/update-the-listview-after-inserting-a-new
    // -record-with-simplecursoradapter-requ/13953470#13953470
    //the only concern I have is that this is ineffective for big sets of TagTemplates. There is
    // only one TagType that needs to be updated so why update the whole set!
    //perhaps this is relevant? https://stackoverflow
    // .com/questions/3724874/how-can-i-update-a-single-row-in-a-listview
    private void updateListView() {
        Cursor updatedCursor = dbHandler.getCursorToTagTemplates();
        adapter.changeCursor(updatedCursor);

    }

    @Override
    public void editTagTemplate(long tagTemplateId) {
        Intent intent = new Intent(getApplicationContext(), TagTypeEditActivity.class);
        intent.putExtra(TAG_TEMPLATE_TO_EDIT, dbHandler.findTagTemplate(tagTemplateId));
        intent.putExtra(TAG_TEMPLATE_ID, tagTemplateId);
        intent.putExtra(IDS_OF_EDITED_TAG_TEMPLATES, idsOfChangedTagTemplates);
        startActivityForResult(intent, TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED);
    }

    @Override
    public void delTagTemplate(long tagTemplateId) {
        //Exercise needs to be removed first with tag that contain a tagTemplate that will no
        // longer exist.
        //for other tags they will be removed through cascading in database.
        dbHandler.removeExercisesWithTagTemplate(tagTemplateId);

        synchronized (this) {
            //Remove the TagType itself from database
            dbHandler.deleteTagTemplate(tagTemplateId);

            //remove TagType from list inside TagAdderView.
            updateListView();
            tagTemplateHasBeenEditedOrDeleted = true;

        }

    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED,
                tagTemplateHasBeenEditedOrDeleted);
        if (idsOfChangedTagTemplates != null) {
            bundle.putLongArray(IDS_OF_EDITED_TAG_TEMPLATES, idsOfChangedTagTemplates);
        }

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
