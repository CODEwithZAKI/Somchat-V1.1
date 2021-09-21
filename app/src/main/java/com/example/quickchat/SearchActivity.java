package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickchat.Activity.HomeActivity;
import com.example.quickchat.Adapter.SearchAdapter;
import com.example.quickchat.Adapter.UserAdapter;
import com.example.quickchat.ModelClass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ImageView imgsearch;
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    SearchAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    EditText edtsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();
        edtsearch = findViewById(R.id.edtsearch);

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgsearch = findViewById(R.id.imgsearch);
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edtsearch.getText())){
                    Toast.makeText(SearchActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                    usersArrayList.clear();
                }else{
                    String s = edtsearch.getText().toString();
                    usersArrayList.clear();
                    Query reference = database.getReference().child("user").orderByChild("name").startAt(s).endAt(s+"\uf88f");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            usersArrayList.clear();
                            for(DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Users users = dataSnapshot.getValue(Users.class);
                                usersArrayList.add(users);
                                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                                    if(users.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        usersArrayList.remove(users);

                                    }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


                adapter = new SearchAdapter(SearchActivity.this,usersArrayList);
                mainUserRecyclerView.setAdapter(adapter);
            }
        });




    }
}