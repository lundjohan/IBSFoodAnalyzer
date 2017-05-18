package com.ibsanalyzer.inputday;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.model.EventsTemplate;

import java.util.List;

import static android.R.attr.name;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_NAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.ibsanalyzer.database.TablesAndStrings.NO_INHERITANCE;
import static com.ibsanalyzer.inputday.R.id.inherits;
import static com.ibsanalyzer.inputday.R.id.tagName;

/**
 * Created by Johan on 2017-05-17.
 *
 *
 * The RecyclerAdapter is wrapping a CursorAdapter
 * heavily inspired by nbtk's answer in http://stackoverflow.com/questions/26517855/using-the-recyclerview-with-a-database
 */
public class EventsTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;

    Context mContext;

    public EventsTemplateAdapter(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eventstemplate, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView nameOfTemplate = (TextView) view.findViewById(R.id.template_title);
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                nameOfTemplate.setText(name);
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
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameOfTemplate;
        public ViewHolder(View itemView) {
            super(itemView);
            nameOfTemplate = (TextView) itemView.findViewById(R.id.template_title);
        }
    }
}
