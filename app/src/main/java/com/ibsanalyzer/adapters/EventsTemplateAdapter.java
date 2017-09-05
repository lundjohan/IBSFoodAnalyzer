package com.ibsanalyzer.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibsanalyzer.inputday.R;

import static com.ibsanalyzer.constants.Constants.UPDATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_NAME;

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

    Context mContext;
    // public final int width;

    public EventsTemplateAdapter(Context context, Cursor c, int width2) {

        mContext = context;
        final int width = width2;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

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
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        viewHolder.three_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPopupMenu(v);


            }
        });
    }


    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
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
    public void doPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.events_template_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_edit){

                }
                else if (item.getItemId() == R.id.menu_delete){

                }
                return true;
            }
        });
        popup.show();
    }
}
