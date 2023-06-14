package com.example.test3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class stud_register extends AppCompatActivity {
    GoogleSignInOptions gso1;
    GoogleSignInClient gsc1;
    TextView tt1,tt2,tt3,tt4, btnout;
    Button btn;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_register);

        tt1 = findViewById(R.id.FullName);
        tt2 = findViewById(R.id.Eno);
        tt3 = findViewById(R.id.MobileNo);
        tt4 = findViewById(R.id.emailID);
        btn = findViewById(R.id.RegisterButton);
        gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc1 = GoogleSignIn.getClient(this,gso1);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            String s1 = acct.getEmail();
            tt4.setText(s1);
        }



        btnout = findViewById(R.id.btnout);
        btnout.setPaintFlags(btnout.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnout.setOnClickListener(view -> signOut());

        btn.setOnClickListener(view -> nextPg());
       /* bt.setOnClickListener(view -> signOut());*/
    }


    public void nextPg()
    {
        String name = tt1.getText().toString().trim();
        String enr_no = tt2.getText().toString().trim();
        String phone = tt3.getText().toString().trim();
        String email = tt4.getText().toString().trim();

        if(!name.isEmpty() && !email.isEmpty() && isValidMobileNumber(phone) && !enr_no.isEmpty()) {
            Intent intent = getIntent();
            String id = intent.getStringExtra("id").toString();
            UserInfo u = new UserInfo(id,name,phone,email,enr_no,id +"_0");

            String[] key = {"id", "name","enr_no","phone","email","identity"};
            String s = u.getIdentity();
            String[] value = {u.getId(), u.getName(), u.getEnr_no(), u.getPhone(), u.getEmail(), s};

            Toast.makeText(stud_register.this,u.toString(),Toast.LENGTH_SHORT).show();

            for(int i = 0; i < 6; i++){
                databaseReference.child("Users").child(id).child(key[i]).setValue(value[i]);
            }

            Toast.makeText(stud_register.this,"stored in database",Toast.LENGTH_SHORT).show();


            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            assert signInAccount != null;
            Uri photoUrl = signInAccount.getPhotoUrl();

            Toast.makeText(stud_register.this,signInAccount.getId(),Toast.LENGTH_SHORT).show();
            Toast.makeText(stud_register.this,photoUrl + "",Toast.LENGTH_SHORT).show();

            if(photoUrl != null){
                Glide.with(this)
                        .load(photoUrl)
                        .circleCrop()
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                // Convert the Drawable to a Bitmap
                                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

                                // Create a storage reference for the user's photo
                                String filename = signInAccount.getId() + ".jpg"; // Replace ".jpg" with the actual file type of the image
                                StorageReference storageRef = reference.child("user_profile_images/" + filename);

                                // Upload the photo to Firebase Storage
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = storageRef.putBytes(data);

                                Toast.makeText(stud_register.this,"uploading started",Toast.LENGTH_SHORT).show();
                                // Listen for upload success or failure
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Photo upload successful
                                        Toast.makeText(stud_register.this,"Profile photo uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Photo upload failed
                                        Toast.makeText(stud_register.this,"Failed to upload profile photo",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
            else{
                Toast.makeText(stud_register.this,"No profile photo found",Toast.LENGTH_SHORT).show();

                Drawable vectorDrawable = getResources().getDrawable(R.drawable.baseline_person_24);
                Bitmap defaultBitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(defaultBitmap);
                vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                vectorDrawable.draw(canvas);

                // Create a storage reference for the user's photo
                String filename = signInAccount.getId() + ".jpg"; // Replace ".jpg" with the actual file type of the image
                StorageReference storageRef = reference.child("user_profile_images/" + filename);


                // Upload the default image to Firebase Storage
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                defaultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = storageRef.putBytes(data);


                Toast.makeText(stud_register.this, "Uploading default image", Toast.LENGTH_SHORT).show();
                // Listen for upload success or failure
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(stud_register.this, "Default image uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(stud_register.this, "Failed to upload default image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Intent i = new Intent(stud_register.this, studui.class);
            i.putExtra("id",u.getId());
            startActivity(i);
            finish();

        } else if (!isValidMobileNumber(phone)) {
            tt3.setError("Please enter a valid phone number");
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(stud_register.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidMobileNumber(String mobileNumber) {
        String mobilePattern = "^[1-9]\\d{9}$";
        return mobileNumber.matches(mobilePattern);
    }
    void signOut()
    {
        gsc1.signOut().addOnCompleteListener(task -> {
            finish();
            startActivity(new Intent(stud_register.this,page2.class));
        });
    }
}