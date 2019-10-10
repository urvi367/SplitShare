package com.example.splitshare;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FirstActivityCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Person> p;

    static class ViewHolder {
        public TextView personname;
        public ImageButton delete;
    }

    public FirstActivityCustomAdapter(Context context, ArrayList<Person> p) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView==null){
            LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=mLayoutInflater.inflate(R.layout.activity_first_listview, null);

            holder = new ViewHolder();
            holder.personname =(TextView) convertView.findViewById(R.id.name);
            holder.delete =(ImageButton) convertView.findViewById(R.id.delete);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person= (Person) getItem(position);
        holder.personname.setText(person.getName());

        /*holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.remove(position);
                notifyDataSetChanged();
            }
        });*/

        return convertView;
    }
}