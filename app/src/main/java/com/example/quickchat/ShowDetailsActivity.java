package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.ModelClass.Like;
import com.example.quickchat.ModelClass.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.quickchat.Activity.ChatActivity.Rid;

public class ShowDetailsActivity extends AppCompatActivity {
    CircleImageView show_image;
    TextView show_name, show_status,likeBtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView close;
    boolean liked=false;
    ArrayList<Like> likeArrayList;
    int likedcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        show_image = findViewById(R.id.show_image);
        show_name = findViewById(R.id.show_name);
        show_status = findViewById(R.id.show_status);
        likeBtn = findViewById(R.id.likeBtn);
        close=findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String sID = getIntent().getStringExtra("id");
        DatabaseReference reference = database.getReference().child("user").child(sID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // email = snapshot.child("email").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String imageUri = snapshot.child("imageUri").getValue().toString();

                show_name.setText(name);
                show_status.setText(status);

                Picasso.get().load(imageUri).into(show_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatReference = database.getReference().child("like").child(Rid);

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String id = dataSnapshot.getKey().toString();
                    if(id.equals(auth.getUid()) )
                    {
                        likeBtn.setText("Already Liked");

                        liked   = true;
                        ///getting value of liked...
//                        DatabaseReference chatReference = database.getReference().child("like").child(Rid);
//                        chatReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                                {
//                                    String id = dataSnapshot.getValue().toString();
//
////                                    if(Integer.parseInt(id) > likedcount )
////                                    {
//                                       // likedcount = Integer.parseInt(id);
//                                        likeBtn.setText(id + " Liked");
//
//
////                                    }else{
////                                        likeBtn.setText(likedcount + " Liked");
////
////                                    }
//
//
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });








                        //ends here

                    }
                    else{
                        likeBtn.setText("Like");
                        liked   = false;
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ////////.............................................

        DatabaseReference Reference = database.getReference().child("like");
                        Reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(snapshot.hasChildren())
                                    likedcount = (int) snapshot.child(Rid).getChildrenCount();
                                if(snapshot.child(Rid).hasChild(user.getUid())){

                                   // likeBtn.setText(String.valueOf(likedcount + " Liked"));
                                    if(likedcount == 0)
                                    {
                                        likeBtn.setText(String.valueOf( " Like"));
                                    }else{
                                        likeBtn.setText(String.valueOf(likedcount + " Liked"));
                                    }
                                    liked = true;
                                }else{
                                    likeBtn.setText(String.valueOf( " Like"));
                                    liked = false;
                                }

//                                for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                                {
//                                    likedcount = likedcount + 1;
//
////                                    if(Integer.parseInt(id) > likedcount )
////                                    {
//                                       // likedcount = Integer.parseInt(id);
//                                       // likeBtn.setText(id + " Liked");
////
////
////                                    }else{
////                                        likeBtn.setText(likedcount + " Liked");
////
////                                    }
//
//
//
//                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });







       // ..............................................................


        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!liked)
                {
                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("like")
                            .child(Rid)
                            .child(auth.getUid())
                            .push()
                            .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ShowDetailsActivity.this, "Successfully liked", Toast.LENGTH_SHORT).show();

                                //////starts here



                                //////////
                                liked = true;
                            }
                            else{
                                liked = false;
                                Toast.makeText(ShowDetailsActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });




    }
}