package com.ibsanalyzer.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.io.Serializable;

import static com.ibsanalyzer.constants.Constants.PUT_TAG_TEMPLATE;
import static com.ibsanalyzer.constants.Constants.TAGTEMPLATE_TO_ADD;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_ID;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_POS;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_TO_EDIT;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE_OF;

/**
 * This is the acticity that is seen when in Meal-, Other- or ExerciseActivity button "Add Tags" is pressed
 */
public class TagAdderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView tagSearch;
    ListView tagsList;
    DBHandler dbHandler;
    TagTemplate chosenTagTemplate = null;
    //if calling Activity is a TagTemplateActivity that requests a "type-of" TagTemplate, then
    // this int stores which of type_of is meant. Default is -1 (<0 means other request than above).
    int typeOf = -1;
    private TagnameCursorAdapter adapter;

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
        if (intent.hasExtra(WHICH_TYPE_OF)) {
            typeOf = intent.getIntExtra(WHICH_TYPE_OF, -1);
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
        /**
         * short click on item in tagtemplate list => item is chosen.
         */
        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //good explanation: http://stackoverflow
            // .com/questions/8222229/what-is-the-difference-between-int-and-long-argument-in
            // -onitemclick-in-andro
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenTagTemplate = dbHandler.findTagTemplate((int) id);
                returnTag();
            }
        });

        /**
         * If longclick is made on item, user should be able to delete or change the TagTemplate.
         * To cancel, point outside dialog.
         *
         * NB => long click should probably be replaced by three dots on each recycleritem instead (much clearer).
         */
        final Context context = this;
        tagsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            /*notice that "id" actually DO return the row_id from database of element.
            This is because the tagsList adapter is a CursorAdapter and it knows the _id of the elements.
            See https://stackoverflow.com/questions/3184672/what-does-adapterview-mean-in-the-onitemclick-method-what-is-the-use-of-ot/25622142#25622142
             */
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long tagTemplateId) {
                final TagTemplate tt = dbHandler.findTagTemplate(tagTemplateId);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Handle Item");
                builder.setMessage("Edit or remove item "+ tt.get_tagname()+"?");
                builder.setPositiveButton("Edit",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                               //TODO: 1. Go to screen TagTemplateEditActivity
                                Intent intent = new Intent(context, TagTemplateEditActivity.class);
                                intent.putExtra(TAG_TEMPLATE_TO_EDIT, tt);
                                intent.putExtra(TAG_TEMPLATE_ID, tagTemplateId);
                                startActivityForResult(intent, TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED);
                                //TODO 2. (After user has changed or deleted item inside TagTemplateEditActivity) => Update tagsList view I guess.
                                //probably by using startActivityForResults
                            }
                        });



                builder.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //TODO: 1. Set null on all TagTemplates references (inherits) to this TagTemplate
                                //TODO: 2. For all Meal, Other and Exercise (special case! How to do here?) events, remove Tag with this TagTemplate.
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                //return true to not allow also short click to fire
                return true;
            }
        });
    }
    private void returnTag(){
        Intent data = new Intent();
        data.putExtra(Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE, (Serializable) chosenTagTemplate);
        if (typeOf >= 0) {
            data.putExtra(WHICH_TYPE, typeOf);
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

    public void doneClickedNew(View view) {
        Intent intent = new Intent(this, TagTemplateAdderActivity.class);
        startActivityForResult(intent, TAGTEMPLATE_TO_ADD);

    }

    /**
     * Has gotten result from clicked new (plus button) for adding new TagTemplate
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
        if (data.hasExtra(PUT_TAG_TEMPLATE)) {
            //update adapter to next time
            adapter.notifyDataSetChanged();

            //return the tagTemplate that has been added.
            chosenTagTemplate = (TagTemplate) data.getSerializableExtra(PUT_TAG_TEMPLATE);
            returnTag();
        }
        if (requestCode == TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED){
            Cursor updatedCursor = dbHandler.getCursorToTagTemplates();
            //there is no notifyDataItemChanged for a CursorAdapter. One alternativ is to update the cursor:
            //see https://stackoverflow.com/questions/13953171/update-the-listview-after-inserting-a-new-record-with-simplecursoradapter-requ/13953470#13953470
            //the only concern I have is that this is ineffective for big sets of TagTemplates. There is only one TagTemplate that needs to be updated so why update the whole set!
            //perhaps this is relevant? https://stackoverflow.com/questions/3724874/how-can-i-update-a-single-row-in-a-listview
            adapter.changeCursor(updatedCursor);
        }
    }
}
