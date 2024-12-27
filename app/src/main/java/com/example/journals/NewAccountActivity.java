package com.example.journals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewAccountActivity extends AppCompatActivity {

    EditText email;
    EditText pass;
    EditText username;
    Button btn1;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.passwd);
        username = findViewById(R.id.username);
        btn1 = findViewById(R.id.account);

        firebaseAuth = FirebaseAuth.getInstance();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }
    public void createAccount(){
        String em = email.getText().toString().trim();
        String passwd = pass.getText().toString().trim();
        String user = username.getText().toString().trim();

        if(!TextUtils.isEmpty(em) && !TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(user)){
            firebaseAuth.createUserWithEmailAndPassword(em,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(NewAccountActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(NewAccountActivity.this, "OOPS Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "All Field are Required", Toast.LENGTH_SHORT).show();
        }
    }
}