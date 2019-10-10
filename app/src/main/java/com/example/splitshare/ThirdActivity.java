package com.example.splitshare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

//settle gives error coz recursion fuckedddddddddd

public class ThirdActivity extends AppCompatActivity {

    /*
    final settlements compute
    and display
     */

    private static final String TAG = "MyActivity";

    private FirebaseUser user;
    private ArrayList<Person> personList;
    private ArrayList<String> keyList;
    private DatabaseReference ref;
    private ThirdActivityCustomAdapter CustomAdapter;
    private ArrayList<Settlement> settlements;
    private ListView lv;
    private TextView t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        keyList=new ArrayList<String>();
        personList=new ArrayList<Person>();
        settlements=new ArrayList<Settlement>();

        lv=(ListView)findViewById(R.id.lv);
        t=(TextView) findViewById(R.id.tvv);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group/persons");

        CustomAdapter=new ThirdActivityCustomAdapter(getApplicationContext(), settlements);
        lv.setAdapter(CustomAdapter);

        ValueEventListener queryValueListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                    DataSnapshot next = (DataSnapshot) i.next();

                    Person p=new Person((String) next.child("name").getValue());
                    p.setShare(Float.valueOf( next.child("share").getValue().toString()));
                    p.setPaid(Integer.valueOf( next.child("paid").getValue().toString()));
                    String key=next.getKey();
                    personList.add(p);
                    keyList.add(key);
                }

                settle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        Query query=ref.orderByKey();
        query.addListenerForSingleValueEvent(queryValueListener);

    }

    private void calculateBalance()
    {
        //Log.i(TAG, "Value = inside calcbal" );
        Person p;
        for (int i=0; i<personList.size(); i++)
        {
            p=personList.get(i);
            p.setBalance(p.getPaid()-p.getShare());
            personList.set(i, p);
            //Log.i(TAG, personList.get(i).getName() + " = " + personList.get(i).getBalance() );
        }
    }

    private void settle()
    {
        //Log.i(TAG, "Value = inside settle" );
        calculateBalance();
        minCashFlowRec();
        CustomAdapter.notifyDataSetChanged();
    }

    int getMin()
    {
        int minInd = 0;
        for (int i = 1; i < personList.size(); i++)
            if (personList.get(i).getBalance() < personList.get(minInd).getBalance())
                minInd = i;
        return minInd;
    }

    int getMax()
    {
        int maxInd = 0;
        for (int i = 1; i < personList.size(); i++)
            if (personList.get(i).getBalance() > personList.get(maxInd).getBalance())
                maxInd = i;
        return maxInd;
    }

    float minOf2(float x, float y)
    {
        return (x < y) ? x: y;
    }

    void minCashFlowRec()
    {
        //Log.i(TAG, "Value = inside mincashflowrec" );
        int mxCredit = getMax(), mxDebit = getMin();

        if (personList.get(mxCredit).getBalance() == 0.0 && personList.get(mxDebit).getBalance() == 0.0)
            return;

        float min = minOf2(-personList.get(mxDebit).getBalance(), personList.get(mxCredit).getBalance());

        Person p=personList.get(mxCredit);
        p.setBalance(p.getBalance()-min);
        personList.set(mxCredit, p);

        p=personList.get(mxDebit);
        p.setBalance(p.getBalance()+min);
        personList.set(mxDebit, p);

        Settlement s=new Settlement(personList.get(mxDebit),personList.get(mxCredit),min);
        settlements.add(s);
        //Log.i(TAG, "Person " + mxDebit + " pays " + min + " to " + "Person " + mxCredit);

        minCashFlowRec();
    }


}
