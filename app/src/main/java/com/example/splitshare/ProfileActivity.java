package com.example.splitshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE =101 ;
    private TextInputLayout unamelayout;
    private ImageView iv;
    private EditText et;
    private Button saveButton;
    private Uri uriProfileImg;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private DatabaseReference ref;
    private User u;
    private Map<String, Object > map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        u=new User();
        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ref = FirebaseDatabase.getInstance().getReference("users");
        user=auth.getCurrentUser();

        unamelayout=(TextInputLayout) findViewById(R.id.unamelayout);
        et=(EditText) findViewById(R.id.editText);
        iv=(ImageView) findViewById(R.id.imageView);
        saveButton=(Button) findViewById(R.id.buttonSave);
        progressBar= (ProgressBar) findViewById(R.id.p);

        loadUserInfo();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                saveUserInfo();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        if(auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent( this, OtherActivity.class));
        }
    }
    public  void loadUserInfo(){

        if(user!=null)
        {
            if(user.getPhotoUrl() !=null)
            {
                progressBar.bringToFront();
                progressBar.setVisibility(View.VISIBLE);

                Glide.with(this).load(user.getPhotoUrl().toString()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (e != null) {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).into(iv); //fetches and displays prof image in imageview
            }

            if(user.getDisplayName()!=null)
                et.setText(user.getDisplayName());
        }

    }

    public  void saveUserInfo(){
        String displayname=et.getText().toString();
        u.setName(displayname);
        if(displayname.isEmpty())
        {
            unamelayout.setError("Name required");
            return;
        }
        if(user!=null && uriProfileImg!=null){
            UserProfileChangeRequest profile =new UserProfileChangeRequest.Builder().setDisplayName(displayname).setPhotoUri(uriProfileImg).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        map.put("username", u.getName());
                        map.put("photourl", uriProfileImg.toString());
                        ref.child(user.getUid()).updateChildren(map);
                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriProfileImg=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImg);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void  uploadImage(){

        if(uriProfileImg!=null){

            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);

            StorageReference ref = storageReference.child("profilepics/"+ user.getUid());
            ref.putFile(uriProfileImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            u.setPhotourl(uriProfileImg);
                         }
                     })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
}
