package com.johanlund.screens.events_templates.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johanlund.database.DBHandler;
import com.johanlund.screens.events_container_classes.EditEventsTemplateActivity;
import com.johanlund.screens.events_container_classes.LoadEventsTemplateActivity;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.events_templates.TemplateFragment;
import com.johanlund.model.EventsTemplate;

import java.io.Serializable;

import static com.johanlund.constants.Constants.EVENTSTEMPLATE_TO_CHANGE;
import static com.johanlund.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.johanlund.constants.Constants.ID_OF_EVENTSTEMPLATE;
import static com.johanlund.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.johanlund.database.TablesAndStrings.COLUMN_ID;
import static com.johanlund.database.TablesAndStrings.COLUMN_NAME;

/**
 * Created by Johan on 2017-05-17.
 * <p>
 * <p>
 * The RecyclerAdapter is wrapping a CursorAdapter
 * heavily inspired by nbtk's answer in http://stackoverflow
 * .com/questions/26517855/using-the-recyclerview-with-a-database
 */
public class EventsTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;

    TemplateFragment usingFragment;
    // public final int width;

    public EventsTemplateAdapter(TemplateFragment usingFragment, Cursor c, int width2) {

        this.usingFragment = usingFragment;
        final int width = width2;

        mCursorAdapter = new CursorAdapter(usingFragment.getContext(), c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_eventstemplate, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView nameOfTemplate = (TextView) view.findViewById(R.id.template_title);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                nameOfTemplate.setText(name);

                //set alternating colors
                int pos = cursor.getPosition();
                if (pos % 5 == 0) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else if (pos % 5 == 1) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkRed));
                } else if (pos % 5 == 2) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color
                            .colorDarkGreen));
                } else if (pos % 5 == 3) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color
                            .colorDarkPurple));
                } else if (pos % 5 == 4) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color
                            .colorDarkBrown));
                }

                //set width and height
                view.setLayoutParams(new RecyclerView.LayoutParams(width, (int) (width * 0.75)));
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(usingFragment.getContext(), mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, usingFragment.getContext(), mCursorAdapter.getCursor());
        viewHolder.three_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPopupMenu(v, position);


            }
        });
    }


    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }


    //also used in copy function in Diary
    public static void startLoadEventsTemplate(EventsTemplate et, Activity activity){
        Intent intent = new Intent(activity, LoadEventsTemplateActivity.class);
        intent.putExtra(EVENTSTEMPLATE_TO_LOAD, et);
        activity.startActivityForResult(intent, LOAD_EVENTS_FROM_EVENTSTEMPLATE);
    }

    public void doPopupMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(usingFragment.getContext(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.events_template_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Cursor c = mCursorAdapter.getCursor();
                c.moveToPosition(position);
                final long eventsTemplateId = c.getLong(c.getColumnIndex(COLUMN_ID));
                if (item.getItemId() == R.id.menu_load) {
                    EventsTemplate et = retrieveEventsTemplate(eventsTemplateId);
                    startLoadEventsTemplate(et, usingFragment.getActivity());
                } else if (item.getItemId() == R.id.menu_edit) {

                    EventsTemplate et = retrieveEventsTemplate(eventsTemplateId);

                    Intent intent = new Intent(usingFragment.getContext(), EditEventsTemplateActivity.class);
                    intent.putExtra(EVENTSTEMPLATE_TO_CHANGE, (Serializable) et);
                    intent.putExtra(ID_OF_EVENTSTEMPLATE, eventsTemplateId);

                    //all changes occur in database, therefore no response is needed
                    usingFragment.getContext().startActivity(intent);

                    //is this ever reached
                    mCursorAdapter.notifyDataSetChanged();

                } else if (item.getItemId() == R.id.menu_delete) {
                    String nameOfTemplate = c.getString(c.getColumnIndex(COLUMN_NAME));

                    //this code is very similar to delete pop up in other places. Place in Util?
                    AlertDialog.Builder builder = new AlertDialog.Builder(usingFragment.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Confirm remove");
                    builder.setMessage("Remove template " + nameOfTemplate + "?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBHandler dbHandler = new DBHandler(usingFragment.getContext());
                                    dbHandler.deleteEventsTemplate(eventsTemplateId);
                                    mCursorAdapter.notifyDataSetChanged();
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface
                            .OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    mCursorAdapter.notifyDataSetChanged();

                    //these doesn't seem to have much effect on renewal of recyclerView
                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                }
                return true;
            }
        });
        popup.show();
    }

    private EventsTemplate retrieveEventsTemplate(long id) {
        DBHandler dbHandler = new DBHandler(usingFragment.getContext());
        return dbHandler.getEventsTemplate(id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameOfTemplate;
        public ImageView three_dots;

        public ViewHolder(View itemView) {
            super(itemView);
            nameOfTemplate = (TextView) itemView.findViewById(R.id.template_title);
            three_dots = (ImageView) itemView.findViewById(R.id.three_dots);
        }
    }
}
