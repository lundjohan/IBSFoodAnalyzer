package com.ibsanalyzer.diary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ibsanalyzer.adapters.TagAdapter;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.model.TagTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;
import static com.ibsanalyzer.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TAGS_TO_ADD;
import static com.ibsanalyzer.constants.Constants.TAG_LIST_CALLBACK;

/**
 * Created by Johan on 2017-05-13.
 */

public abstract class TagEventActivity extends EventActivity implements TagAdderActivity.TagListCallback {
    protected List<Tag> tagsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TagAdapter adapter;

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
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagTemplate tagTemplate = (TagTemplate) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);

            //create a new Tag
            Tag tag = new Tag(getLocalDateTime(), tagTemplate.get_tagname(), 1.0);
            tagsList.add(tag);
        }
        notifyItemInserted();
    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
       //uncommenting this line, cause it is an error. It had to do with editing the eventactivities tags when a tagtemplate been changed. But it didn't work properly anyway.
        // intent.putExtra(TAG_LIST_CALLBACK, this);
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
    public  void updateListViewAfterDeletionOfTagTemplate(String oldName){
        for (int i = 0;i<tagsList.size(); i++){
            if (tagsList.get(i).getName().equals(oldName)) {
                tagsList.remove(i);
                adapter.notifyItemChanged(i);
            }

        }
    }
    @Override
    public void updateListViewAfterEditingOfTagTemplate(String oldName, String newName){
        for (int i = 0;i<tagsList.size(); i++){
            Tag oldTag = tagsList.get(i);
            if (oldTag.getName().equals(oldName)){
                oldTag.setName(newName);
                adapter.notifyItemChanged(i);
            }
        }
    }
}
