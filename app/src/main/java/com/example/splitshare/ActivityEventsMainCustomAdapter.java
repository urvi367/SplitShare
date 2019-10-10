package com.example.splitshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityEventsMainCustomAdapter extends BaseAdapter {

    ArrayList<Event> e;
    Context context;

    static class ViewHolder {
        public TextView eventname;
        public TextView people;
        public TextView total;
        public ImageButton delete;
    }


    public ActivityEventsMainCustomAdapter(Context context, ArrayList<Event> e) {
        this.context = context;
        this.e = e;
    }

    @Override
    public int getCount() {
        return e.size();
    }

    @Override
    public Object getItem(int position) {
        return e.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        ViewHolder viewHolder;
        // reuse views
        if (convertView == null) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.activity_events_main_listview, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.eventname = (TextView) convertView.findViewById(R.id.eventname);
            viewHolder.people = (TextView) convertView.findViewById(R.id.people);
            viewHolder.total = (TextView) convertView.findViewById(R.id.total);
            viewHolder.delete= (ImageButton) convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // fill data
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Event event = e.get(position);
        holder.eventname.setText(event.getEventname());
        event.ToString();
        holder.people.setText(event.getName());
        holder.total.setText(String.valueOf(event.getTotal()));

        return convertView;

    }
}
