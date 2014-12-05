package com.tarks.transport.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarks.transport.R;

import java.util.List;

/**
 * Created by JHRunning on 10/25/14.
 */
public final class ListAdapter extends WearableListView.Adapter {
    private String[] mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private int selected_pos;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(Context context, String[] dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
        }

        public void Sizebig(){
            textView.setTextSize(21);
        }
    }



    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        // replace text contents
        view.setText(mDataset[position]);

//       if(int) view.setTextSize(21);
        //view.isFocusable()
        // replace list item's metadata
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        if (v == null) {
//            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = vi.inflate(R.layout.profile_list, null);
//        }
//        final List p = items.get(position);
//        if (p != null) {
//            TextView tt = (TextView) v.findViewById(R.id.titre);
//            TextView bt = (TextView) v.findViewById(R.id.description);
//            ImageView image = (ImageView) v.findViewById(R.id.img);
//
//            if (tt != null) {
//                tt.setText(p.getTitle());
//                // Log.i("test", p.getStatus() + "kk" +p.getTitle() );
//                // Status not public
//                if (p.getStatus() > 1) {
//                    tt.setTextColor(Color.GRAY);
//                } else {
//                    tt.setTextColor(Color.BLACK);
//                }
//
//            }
//
//
//        }
//        return v;
//    }
}

