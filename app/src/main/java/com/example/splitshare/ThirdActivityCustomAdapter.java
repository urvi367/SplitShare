package com.example.splitshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ThirdActivityCustomAdapter extends BaseAdapter {

    ArrayList<Settlement> settlements;
    Context context;

    static class ViewHolder {
        public TextView person1, person2, amt;
        public ImageView arrow;
    }

    public ThirdActivityCustomAdapter(Context context, ArrayList<Settlement> settlements) {
        this.context = context;
        this.settlements = settlements;
    }

    @Override
    public int getCount() {
        return settlements.size();
    }

    @Override
    public Object getItem(int i) {
        return settlements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // reuse views
        if (convertView == null) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.activity_final_listview, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.person1 = (TextView) convertView.findViewById(R.id.person1);
            viewHolder.person2 = (TextView) convertView.findViewById(R.id.person2);
            viewHolder.amt = (TextView) convertView.findViewById(R.id.amt);
            viewHolder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // fill data
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Settlement settlement = settlements.get(position);
        holder.person2.setText(settlement.getPerson2Name());
        holder.person1.setText(settlement.getPerson1Name());
        holder.amt.setText(Float.toString(settlement.getAmount()));

        return convertView;

    }

}
