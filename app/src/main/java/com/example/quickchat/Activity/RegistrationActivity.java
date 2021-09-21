package com.example.quickchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.example.quickchat.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity {

    TextView txt_signin,btn_SignUp;
    ProgressDialog progressDialog;
    CircleImageView profile_image;
    EditText reg_name, reg_email, reg_pass,reg_cPass;
    Uri imageUri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String imageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_pass = findViewById(R.id.reg_pass);
        reg_cPass = findViewById(R.id.reg_cPass);
        btn_SignUp = findViewById(R.id.btn_SingUp);
        profile_image = findViewById(R.id.profile_image);

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_pass.getText().toString();
                String cPass = reg_cPass.getText().toString();
                String status = "Hey There! I'm using this Application";


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPass))
                {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    reg_email.setError("Please enter valid email");
                    Toast.makeText(RegistrationActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(cPass)){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password doesn't Match", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Not more than 6 chars", Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                DatabaseReference reference =database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if(imageUri!= null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(RegistrationActivity.this, "Error in Creating user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }

                                        }
                                    });
                                }else{
                                    String status = "Hey There! I'm using this Application";
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/quickchat-eb8db.appspot.com/o/profile_image.png?alt=media&token=4f9a1363-c99f-47ce-8820-8ff5c43c5803";
                                    Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "This Email Already Exists!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


        txt_signin = findViewById(R.id.txt_signin);
        profile_image = findViewById(R.id.profile_image);
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==10){
            if(data!= null){
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}