package com.example.splitshare;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private View navHeader;
    private TextView tv;
    private Button delete;
    private ImageButton add;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout dl;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nv;
    private  Menu menu;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private ArrayList<String> groupList;
    private ViewPager viewPager;
    private PageAdapterMainActivity adapter;
    private TextView name, email;
    private CircleImageView img;

    //add delete item/group functionality
    //how to get fragments to show mera dataaaaaaaaaaaaaaaaaaaaaaaaaaaa?????
    //HOW? HOW?? HOWWWWWWWWWWWWWWWWWW????????????????????////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group");
        groupList = new ArrayList<String>();


        tv = (TextView) findViewById(R.id.textView2);
        delete = (Button) findViewById(R.id.delete);
        add = (ImageButton) findViewById(R.id.addgroup);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        nv = (NavigationView) findViewById(R.id.navigationView);
        navHeader = nv.getHeaderView(0);
        name= navHeader.findViewById(R.id.name);
        email= navHeader.findViewById(R.id.email);
        img= navHeader.findViewById(R.id.img_profile);

        addChildEventListener();

        setSupportActionBar(toolbar);

        nv.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.drawer_open,  R.string.drawer_close);
        dl.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menu=nv.getMenu();
        //menu.removeItem(1);

        if(user!=null)
        {
            if(user.getPhotoUrl() !=null)
            {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(img);
            }

            if(user.getDisplayName()!=null)
                name.setText(user.getDisplayName());

            if(user.getEmail()!=null)
                email.setText(user.getEmail());
        }

        tabLayout.addTab(tabLayout.newTab().setText("Expenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Settle"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new PageAdapterMainActivity(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String title = item.getTitle().toString();
                Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                tv.setText(title);
                adapter.setRef(ref+"/"+title.replace(" ", "_"));
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff here
            }
        });

        //while(groupList.size()==0);
       /* adapter.setRef(ref+"/"+groupList.get(0));
        adapter.notifyDataSetChanged();*/

    }


    private void addChildEventListener()
    {
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                menu.add(key.replace("_", " "));
                groupList.add(key);
                nv.invalidate();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                /*String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if (index != -1) {
                    personList.remove(index);
                    listKeys.remove(index);
                    customAdapter.notifyDataSetChanged();
                }*/
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return dl != null && dl.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (dl != null) {
            dl.closeDrawer(GravityCompat.START);
        }
    }
}
