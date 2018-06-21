package com.johanlund.screens.event_activities.mvcviews;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.event_activities.listadapters.TagEventAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class TagEventViewMvcAbstract extends EventViewMvcAbstract implements
        TagEventAdapter.Listener {
    protected List<TagWithoutTime> tagsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TagEventAdapter adapter;
    private Button addTagsBtn;

    public TagEventViewMvcAbstract(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected void initializeSpecViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.addedTagsView);
        addTagsBtn = (Button) rootView.findViewById(R.id.addTagsBtn);
        addTagsBtn.setText(getTextForAddTagsBtn());
        layoutManager = new LinearLayoutManager(context);
        tagsList = new ArrayList<>();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TagEventAdapter(tagsList, this, context);
        recyclerView.setAdapter(adapter);


        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    protected void bindEventSpecsToView(Event e) {
        tagsList.addAll(((InputEvent) e).getTagsWithoutTime());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void bindAddedTagToView(String tagName) {
        TagWithoutTime tag = new TagWithoutTime(tagName, 1.0);
        tagsList.add(tag);
        adapter.notifyItemInserted(tagsList.size() - 1);
    }


    @Override
    public void removeTagFromView(String tagName) {
        for (int i = 0; i < tagsList.size(); i++) {
            if (tagsList.get(i).getName().equals(tagName)) {
                tagsList.remove(i);
            }
            //break: should be here, but first has to implement that tags can not have dublettes
            // in tagsList
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public List<String> getTagNames() {
        List<String> tagNames = new ArrayList<>();
        for (TagWithoutTime t : tagsList) {
            tagNames.add(t.getName());
        }
        return tagNames;
    }

    /**
     * Get the text for the button that adds tags. ("Tags" is not intuitive for a new user).
     *
     * @return
     */
    protected abstract String getTextForAddTagsBtn();

    public void onTagItemDeleteClicked(View v, final int position) {
        String nameOfTag = tagsList.get(position).getName();
        //here. Should be a pop up ("Removed item okra")
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Confirm remove");
        builder.setMessage("Remove item " + nameOfTag + "?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagsList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, tagsList.size());
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
}
