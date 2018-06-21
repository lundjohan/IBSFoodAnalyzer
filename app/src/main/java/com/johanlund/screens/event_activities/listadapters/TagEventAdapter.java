package com.johanlund.screens.event_activities.listadapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.johanlund.base_classes.Tag;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.util.Util;

import java.util.List;

/**
 * Created by Johan on 2017-05-13.
 */

public class TagEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<TagWithoutTime> tagsList;
    TagEventAdapter.Listener listener;
    Context context;

    public interface Listener{
        void onTagItemDeleteClicked(View v, int position);
    }
    public TagEventAdapter(List<TagWithoutTime> tagsList, TagEventAdapter.Listener listener, Context context ) {
        this.tagsList = tagsList;
        this.listener = listener;
        this.context = context;
    }

    //s. 355
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TagWithoutTime t = tagsList.get(position);
        viewHolder.tagName.setText(t.getName());
        final TextView quantity = viewHolder.quantity;
        viewHolder.quantity.setText(Double.toString(t.getSize()));
        viewHolder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useNumberPickerDialogForTag(context, quantity, tagsList.get(position));
            }
        });
        viewHolder.deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTagItemDeleteClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quantity;
        public TextView tagName;
        public ImageView deleteTag;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.tag_quantity);
            tagName = (TextView) itemView.findViewById(R.id.stattagname);
            deleteTag = (ImageView) itemView.findViewById(R.id.deleteTag);
        }
    }
    private static void useNumberPickerDialogForTag(Context context, final TextView textWithNrToChange, final TagWithoutTime tag) {
        View v = LayoutInflater.from(context).inflate(R.layout.decimal_number_picker, null);
        final NumberPicker np1 = (NumberPicker) v.findViewById(R.id.numberPicker1);
        Util.setNrsForNumberPicker(np1, true);
        final NumberPicker np2 = (NumberPicker) v.findViewById(R.id.numberPicker2);
        Util.setNrsForNumberPicker(np2, false);

        //for conversion to numbers on both sides of decimal point note that double is inexakt =>
        // 4.5 can become 4.4999999999, so it's not good idea to simply truncate with (int)
        Double originalNr = Double.parseDouble((String) textWithNrToChange.getText());
        int intPart = originalNr.intValue();
        int decPart = (int) Math.round((originalNr.doubleValue() - (double) intPart) * 10.);
        np1.setValue(intPart);
        np2.setValue(decPart);
        new AlertDialog.Builder(context)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double value;
                        value = np1.getValue() + ((double) np2.getValue()) / 10;
                        textWithNrToChange.setText(Double.toString(value));
                        tag.setSize(value);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
