package com.johanlund.event_activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.johanlund.adapters.TagAdapter;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.ibsfoodanalyzer.TagAdderActivity;
import com.johanlund.model.TagType;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.IDS_OF_EDITED_TAG_TEMPLATES;
import static com.johanlund.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.johanlund.constants.Constants.TAGS_TO_ADD;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;

/**
 * Created by Johan on 2017-05-13.
 */

public abstract class TagEventActivity extends EventActivity {
    protected List<Tag> tagsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TagAdapter adapter;
    private boolean tagTemplateSeemsToHaveBeenBeenEditedOrDeleted;
    private long [] editedTagTemplatesIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.addedTagsView);
        layoutManager = new LinearLayoutManager(this);
        tagsList = new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TagAdapter(tagsList, this);
        recyclerView.setAdapter(adapter);


        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        //is the event meant to be changed (as opposition to new event to be created)?
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)) {
            InputEvent e = (InputEvent) intent.getSerializableExtra(EVENT_TO_CHANGE);
            tagsList.addAll(e.getTags());
            adapter.notifyDataSetChanged();
        }
        /*if (intent.hasExtra(IDS_OF_EDITED_TAG_TEMPLATES)){
            editedTagTemplatesIds = intent.getLongArrayExtra(IDS_OF_EDITED_TAG_TEMPLATES);
        }*/
    }

    protected void notifyItemInserted() {
        adapter.notifyItemInserted(tagsList.size() - 1);
    }

    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data.hasExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED)) {
            if (data.getBooleanExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED, true)) {
                tagTemplateSeemsToHaveBeenBeenEditedOrDeleted = true;
                if (data.hasExtra(IDS_OF_EDITED_TAG_TEMPLATES)){
                    editedTagTemplatesIds = data.getLongArrayExtra(IDS_OF_EDITED_TAG_TEMPLATES);
                }



                updateTagsList();
            }
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagType tagType = (TagType) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);

            //create a new Tag
            Tag tag = new Tag(getLocalDateTime(), tagType.get_tagname(), 1.0);
            tagsList.add(tag);
            notifyItemInserted();
        }
    }

    /**
     * is called after TagType(s) has been deleted or edited.
     * This function should be as simple as possible,
     * simply match this Activity's TagList and TagType in database,
     * if no match => remove from tagList (it is not so terrible if too many lines become removed)
     */
    private void updateTagsList() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        for (int i = 0; i < tagsList.size(); i++) {
            String tagName = tagsList.get(i).getName();
            if (dbHandler.getTagTemplateId(tagName) == -1) {
                tagsList.remove(i);
            }

        }
        if (editedTagTemplatesIds != null) {
            for (long idOfTagTemplate : editedTagTemplatesIds) {
                tagsList.add(new Tag(getLocalDateTime(),dbHandler.getTagTemplateName(idOfTagTemplate), 1.0));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }


    public void onTagItemDeleteClicked(View v, final int position) {
        String nameOfTag = tagsList.get(position).getName();
        //here. Should be a pop up ("Removed item okra")
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm remove");
        builder.setMessage("Remove item " + nameOfTag + "?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagsList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,tagsList.size());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    public void onBackPressed() {
        if (tagTemplateSeemsToHaveBeenBeenEditedOrDeleted) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED,
                    tagTemplateSeemsToHaveBeenBeenEditedOrDeleted);

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

}
