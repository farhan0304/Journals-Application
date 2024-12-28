package com.example.journals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView email;
    EditText passwd;

    Button btn1;
    Button btn2;

    FirebaseAuth firebaseAuth;

    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        passwd = findViewById(R.id.passwd);
        btn1 = findViewById(R.id.signin);
        btn2 = findViewById(R.id.createaccount);
        String cloudName = BuildConfig.API_KEY;

        Map config = new HashMap();
        config.put("cloud_name",cloudName);
        MediaManagerProvider.initialize(this,config);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(firebaseAuth!=null && user!=null){
            Intent i = new Intent(MainActivity.this, JournalActivity.class);
            startActivity(i);
        }


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(firebaseAuth!=null && user!=null){
                    // signed in
                    Toast.makeText(MainActivity.this, "User signed in", Toast.LENGTH_SHORT).show();


                }else{
                    //signed out
                }
            }
        };



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSignIn();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(i);
            }
        });


    }
    public void getSignIn(){
        String emailAddress = email.getText().toString().trim();
        String pass = passwd.getText().toString().trim();
        if(!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(pass)){

            firebaseAuth.signInWithEmailAndPassword(emailAddress,pass).addOnSuccessListener(this,new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    user = authResult.getUser();
                    Intent i = new Intent(MainActivity.this,JournalActivity.class);
                    startActivity(i);
                }
            }).addOnFailureListener(this,new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(this, "All fields are Required", Toast.LENGTH_SHORT).show();
        }
    }
}