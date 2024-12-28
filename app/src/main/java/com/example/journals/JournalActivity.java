package com.example.journals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class JournalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btn;
    MaterialToolbar toolbar;

    MyAdapter adapter;

    FirebaseAuth firebaseAuth;

    FirebaseUser user;
    ArrayList<JournalModel> arr = new ArrayList<>();

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        recyclerView = findViewById(R.id.recyclerView);
        btn = findViewById(R.id.addbtn);
        toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter(this,arr);
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddJournalActivity();
            }
        });



        db.collection("journals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(JournalActivity.this, "Task is successful", Toast.LENGTH_SHORT).show();
                            for(QueryDocumentSnapshot doc: task.getResult()){

                                JournalModel model = doc.toObject(JournalModel.class);
                                arr.add(model);
                            }
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(JournalActivity.this, "Unable to fetch journals", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void startAddJournalActivity(){
        Intent i = new Intent(JournalActivity.this,AddJournalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_add){
            startAddJournalActivity();
        }
        else if(id==R.id.signoutbtn){
            firebaseAuth.signOut();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
}