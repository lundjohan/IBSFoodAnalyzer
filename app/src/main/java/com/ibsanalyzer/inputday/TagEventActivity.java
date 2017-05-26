package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.model.TagTemplate;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TAGS_TO_ADD;

/**
 * Created by Johan on 2017-05-13.
 */

public abstract class TagEventActivity extends EventActivity {
    private Button addTagsBtn;

    protected List<Tag> tagsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TagAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void notifyItemInserted(){
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
            TagTemplate tagTemplate = (TagTemplate) data.getExtras().getSerializable(RETURN_TAG_TEMPLATE_SERIALIZABLE);

            //create a new Tag
            Tag tag = new Tag(getLocalDateTime(), tagTemplate.get_tagname(), 1.0);
            tagsList.add(tag);
        }
        notifyItemInserted();
    }
    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }


}
