package com.example.journals;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class AddJournalActivity extends AppCompatActivity {

    ImageView postImage;
    ImageView addImage;
    EditText editText1;
    EditText editText2;
    ProgressBar progressBar;
    Button btn;

    Uri imageUri;
    ActivityResultLauncher<String> mTakePhoto;

    FirebaseFirestore db;

    String imageUrl;

    private String cloudName = BuildConfig.API_KEY;
    private String uploadPreset = BuildConfig.UPLOAD_PRESET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        postImage = findViewById(R.id.postImage);
        addImage = findViewById(R.id.addImage);
        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progress);
        btn = findViewById(R.id.addJournalBtn);
        progressBar.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();


        Map config = new HashMap();
        config.put("cloud_name",cloudName);
        MediaManager.init(this,config);



        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        postImage.setImageURI(o);
                        imageUri = o;

                    }
                });



        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                MediaManager.get().upload(imageUri).unsigned(uploadPreset).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        addNewPost();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(AddJournalActivity.this, "Upload Failed in cloudinary", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();


            }
        });
    }

    public void addNewPost(){

        String postTitle = editText1.getText().toString();
        String postContent = editText2.getText().toString();
        if(!TextUtils.isEmpty(postTitle) && !TextUtils.isEmpty(postContent) && !TextUtils.isEmpty(imageUrl)){
            JournalModel myjournal = new JournalModel(postTitle,postContent,imageUrl,Timestamp.now());
            db.collection("journals")
                    .add(myjournal).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(getApplicationContext(), JournalActivity.class);
                                startActivity(i);

                            }else{
                                Toast.makeText(AddJournalActivity.this, "OOPS Post Uploading Failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }


    }
}