package com.ibsanalyzer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Johan on 2017-08-09.
 */

public class EventAdapter2 extends RecyclerView.Adapter<EventAdapter2.ViewHolder> {
    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;

    Context mContext;

    public EventAdapter2(Context context, Cursor c) {

        mContext = context;

     /*   mCursorAdapter = new CursorAdapter(usingFragment, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here;
                return ;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
            }
        };*/
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as
        // suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;

        public ViewHolder(View itemView) {
            super(itemView);
            //  v1 = itemView.findViewById(R.id.v1);
        }
    }
}
