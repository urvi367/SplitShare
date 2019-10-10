package com.example.splitshare;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity {

    /*
    creation page for each event
     */

    private DatabaseReference ref;
    private DatabaseReference eventref;
    private FirebaseUser user;
    private Button addd;
    private ImageButton backk;
    private TextView tv;
    private ArrayList<Person> personList;
    private ArrayList<String> keyList;
    private EditText event;
    private ListView lv;
    private SecondActivityCustomAdapter CustomAdapter;
    private Person person;
    private Event cur_event;
    private int total=0, numberOfPpl=0; //event share per person update in db
    private float share=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final Context context = this;

        addd=(Button)findViewById(R.id.add);
        backk=(ImageButton)findViewById(R.id.settings);
        tv=(TextView)findViewById(R.id.textView);
        event=(EditText)findViewById(R.id.enter_name);
        lv=(ListView)findViewById(R.id.listView);

        cur_event=new Event();
        keyList=new ArrayList<String>();
        personList=new ArrayList<Person>();

        user= FirebaseAuth.getInstance().getCurrentUser();
        eventref= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group/events");
        ref= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group/persons");

        CustomAdapter=new SecondActivityCustomAdapter(getApplicationContext(), personList);
        lv.setAdapter(CustomAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                person = (Person) parent.getItemAtPosition(position);
                if(!person.isCheck())
                {
                    numberOfPpl++;
                    person.setCheck(true);
                    cur_event.setPplname(person.getName());
                    CustomAdapter.notifyDataSetChanged();


                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.alert_dialogue, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            int paid= Integer.parseInt(userInput.getText().toString());
                                            total=total+paid;
                                            person.setCurPaid(paid);
                                            person.setPaid(person.getPaid()+paid);
                                            //ref.child(keyList.get(position)).child("paid").setValue(person.getPaid());
                                            CustomAdapter.notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else
                {
                    numberOfPpl--;
                    person.setCheck(false);
                    total=total-person.getCurPaid();
                    person.setPaid(person.getPaid()-person.getCurPaid());
                    person.setCurPaid(0);
                    ref.child(keyList.get(position)).child("paid").setValue(person.getPaid());
                    cur_event.removePplname(person.getName());
                    CustomAdapter.notifyDataSetChanged();
                }
            }
        });

        addChildEventListener();

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //do something
                //add event
                String x=event.getText().toString();
                if(x==null || x.equals(""))
                    Toast.makeText(getApplicationContext(),"Enter a valid event name",Toast.LENGTH_LONG).show();
                else{
                    share=total/Float.valueOf(numberOfPpl);
                    for (String s : keyList) {

                        if(personList.get(keyList.indexOf(s)).isCheck())
                            ref.child(s).child("share").setValue(personList.get(keyList.indexOf(s)).getShare()+share);
                        ref.child(s).child("paid").setValue(personList.get(keyList.indexOf(s)).getPaid());
                    }
                    cur_event.setEventname(x);
                    event.setText("");
                    cur_event.setTotal(total);
                    cur_event.ToString();
                    String key = eventref.push().getKey();
                    eventref.child(key).setValue(cur_event);
                    finish();
                }

            }
        });

    }

    private void addChildEventListener()
    {
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Person p=new Person((String) dataSnapshot.child("name").getValue());
                p.setShare(Float.valueOf( dataSnapshot.child("share").getValue().toString()));
                p.setPaid(Integer.valueOf( dataSnapshot.child("paid").getValue().toString()));
                personList.add(p);
                keyList.add(dataSnapshot.getKey());
                CustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
