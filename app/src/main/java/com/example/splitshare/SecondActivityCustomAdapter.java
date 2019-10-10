package com.example.splitshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class SecondActivityCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Person> p;

    static class ViewHolder {
        public CheckBox check;
        public TextView name;
        public TextView inr;
    }

    public SecondActivityCustomAdapter(Context context, ArrayList<Person> p) {
        this.context = context;
        this.p = p;
    }

    @Override
    public int getCount() {
        return p.size();
    }

    @Override
    public Object getItem(int position) {
        return p.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView=convertView;
        ViewHolder viewHolder = new ViewHolder();
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            rowView = inflater.inflate(R.layout.activity_second_listview, null);
            // configure view holder
            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.inr = (TextView) rowView.findViewById(R.id.pay);
            viewHolder.check = (CheckBox) rowView.findViewById(R.id.checkbox);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // fill data
        final Person person = p.get(position);
        viewHolder.name.setText(person.getName());
        viewHolder.inr.setText(Integer.toString(person.getCurPaid()));
        viewHolder.check.setChecked(person.isCheck());

        return rowView;
    }

}
