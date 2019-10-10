package com.example.splitshare;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.example.splitshare.ContactsAdapter;

public class FirstActivity extends AppCompatActivity {

    /*Add names of all participants*/

    private static final int REQUEST_PERMISSION = 1;

    private FloatingActionButton next;
    private ContactsAdapter contactsAdapter;
    private SearchView searchView;
    private ListView contactsList;
    private Toolbar toolbar, searchtollbar;
    private TextView tv;
    private EditText namee;
    private ListView nameList;
    public static ArrayList<Person> personList;
    private FirstActivityCustomAdapter customAdapter;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Person p;
    private ArrayList<String> listKeys;
    private Menu search_menu;
    private MenuItem item_search;
    private TextView toolbar_title;
    //settings and feedback and about us in overflow menu.

    //listview dosent update while the search thingy is active.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final Context context = this;
        ref=null;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactsList = findViewById(R.id.contact_list);
        next=(FloatingActionButton)findViewById(R.id.next);
        tv=(TextView)findViewById(R.id.textView);
        namee=(EditText)findViewById(R.id.enter_name);     //what is thissssssssssssss????????????/
        nameList=(ListView)findViewById(R.id.list);
        toolbar_title=(TextView) toolbar.findViewById(R.id.toolbar_title);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_PERMISSION);
        }

        setSearchtollbar();

        personList =new ArrayList<Person>();
        listKeys = new ArrayList<String>();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        customAdapter = new FirstActivityCustomAdapter(getApplicationContext(), personList);
        nameList.setAdapter(customAdapter);
        nameList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        nameList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int deletePosition = position;

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        FirstActivity.this);

                alert.setTitle("Delete");
                alert.setMessage("Do you want delete this item?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TOD O Auto-generated method stub

                        // main code on after clicking yes
                        personList.remove(deletePosition);
                        customAdapter.notifyDataSetChanged();
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


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to firebase
                String x=toolbar_title.getText().toString().replace(" ", "_");
                ref=FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/group/"+ x +"/persons");
                if(x=="Group_Name")
                    Toast.makeText(getApplicationContext(),"Enter a valid name",Toast.LENGTH_LONG).show();
                else{
                    for(Person p : personList)
                    {
                        String key = ref.push().getKey();
                        ref.child(key).setValue(p);
                    }
                }

                Intent intent=new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.alert_dialogue_grpname, null);

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
                                        String name= userInput.getText().toString();
                                        toolbar_title.setText(name);
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
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_CONTACTS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission was granted.
        } else{
            Toast.makeText(FirstActivity.this,"READ_CONTACT permission not granted", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        // Handle item selection
        switch (item.getItemId())
        {

            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar,1,true,true);
                else
                    searchtollbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    public void setSearchtollbar()
    {
        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);

        if (searchtollbar != null) {

            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu=searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar,1,true,false);
                    else
                        searchtollbar.setVisibility(View.GONE);

                }

            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {

                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar,1,true,false);
                    }
                    else
                        searchtollbar.setVisibility(View.GONE);

                    return true;

                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();

        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }


    public void initSearchView()
    {

        final SearchView searchView = (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard
        searchView.setSubmitButtonEnabled(false);

        // Change search close button image
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);

        // set hint and the text colors
        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setTextColor(getResources().getColor(R.color.off_white_opaque));

        // set the cursor
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background

        } catch (Exception e) {
            e.printStackTrace();
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Cursor contacts = getListOfContactNames(query);
                String[] cursorColumns = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY, ContactsContract.CommonDataKinds.Phone.NUMBER};
                int[] viewIds = {R.id.tv_name, R.id.tv_phone};

                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                        FirstActivity.this,
                        R.layout.contact_item_layout,
                        contacts,
                        cursorColumns,
                        viewIds,
                        0);

                contactsList.setAdapter(simpleCursorAdapter);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor contacts = getListOfContactNames(newText);
                ContactsAdapter cursorAdapter = new ContactsAdapter
                        (FirstActivity.this, contacts, searchView);
                searchView.setSuggestionsAdapter(cursorAdapter);
                return true;
            }


        });

    }


    public Cursor getListOfContactNames(String searchText) {

        Cursor cur = null;

        /*String[] mProjection = {ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};*/
        String[] mProjection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        cur = getContentResolver().query(uri, mProjection, selection, selectionArgs, null);

        return cur;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {

        final View myView = findViewById(viewID);
        int width=myView.getWidth();


        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);

        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);


        int cx=width;
        int cy=myView.getHeight()/2;


        Animator anim;

        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }

            }

        });


        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();

    }


}
