package com.example.splitshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ActivityEventsMain extends AppCompatActivity {

    /*show all events
      fab to create new events
     */
    private Button nextt;
    private ImageButton setting;
    private ListView lv;
    private FloatingActionButton fab;
    private ArrayList<Event> e;
    private ActivityEventsMainCustomAdapter CustomAdapter;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private ArrayList<String> listKeys;
    private Event event;
    private ArrayList<String> eventpplname;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        nextt=(Button)findViewById(R.id.next);
        setting=(ImageButton)findViewById(R.id.settings);
        lv=(ListView)findViewById(R.id.listview_with_fab);
        fab=(FloatingActionButton) findViewById(R.id.floating_action_button);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group/events");
        e=new ArrayList<Event>();
        listKeys = new ArrayList<String>();
        CustomAdapter=new ActivityEventsMainCustomAdapter(getApplicationContext(), e);
        lv.setAdapter(CustomAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int deletePosition = position;

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        ActivityEventsMain.this);

                alert.setTitle("Delete");
                alert.setMessage("Do you want delete this item?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TOD O Auto-generated method stub

                        ref.child(listKeys.get(deletePosition)).removeValue();

                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                alert.show();

                return false;
            }
        });

        addChildEventListener();

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEventsMain.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityEventsMain.this, OtherActivity.class);
                startActivity(intent);
            }
        });

        nextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityEventsMain.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

    }


    public void addChildEventListener()
    {
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                event=new Event((String) dataSnapshot.child("eventname").getValue());
                event.setPplname((String) dataSnapshot.child("pplname").getValue());
                event.setTotal( Integer.valueOf(dataSnapshot.child("total").getValue().toString()));
                e.add(event);
                listKeys.add(dataSnapshot.getKey());
                CustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if (index != -1) {
                    e.remove(index);
                    listKeys.remove(index);
                    CustomAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(childEventListener);
    }
}
