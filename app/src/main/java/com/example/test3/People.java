package com.example.test3;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class People extends Fragment {
    ListView listView;
    ArrayList<String> ls;
    ArrayList<String> sName;
    ArrayList<String> list;
    PersonAdapter personAdapter;
    TextView textView;
    //ArrayAdapter<String> adapter;
    public People(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        imageView = view.findViewById(R.id.profileImageteach);

        listView = view.findViewById(R.id.Listview);
        list = new ArrayList<>();
        textView = view.findViewById(R.id.teacher);
        //adapter = new ArrayAdapter<String>(requireContext() ,R.layout.list_item, list);
        sName = new ArrayList<>();
        personAdapter = new PersonAdapter(requireContext(),sName);
        listView.setAdapter(personAdapter);

        ls = new ArrayList<>();

        //processStudentData();

        //Toast.makeText(requireContext(),"showing student started",Toast.LENGTH_SHORT).show();

        return view;
    }

    ImageView imageView;
    @Override
    public void onResume() {
        super.onResume();
        getStudentId();

        DatabaseReference teacher = FirebaseDatabase.getInstance().getReference("Subject");
        DatabaseReference tinfo = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("photouris");
        teacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (!childSnapshot.child("teacher_id").getValue(String.class).isEmpty() && childSnapshot.child("subject_id").getValue().toString().equals(mngtchclass.subId)) {
                        String teacher_id = childSnapshot.child("teacher_id").getValue(String.class);

                        tinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (Objects.equals(childSnapshot.child("id").getValue(String.class), teacher_id)) {
                                        String name = childSnapshot.child("name").getValue(String.class);
                                        textView.setText(name);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                            }
                        });


                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if(childSnapshot.child("id").getValue(String.class).equals(teacher_id)) {
                                        String uri = childSnapshot.child("uri").getValue(String.class);

                                        Uri photoUrl = Uri.parse(uri);
                                        Glide.with(getContext())
                                                .load(photoUrl)
                                                .placeholder(R.drawable.baseline_person_24)
                                                .error(R.drawable.baseline_person_24)
                                                .circleCrop()
                                                .into(imageView);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /*public void loadFromDB(){
        if(!isAdded()){
            return;
        }
        // Get a reference to the Firebase Storage service
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //Toast.makeText(requireContext(),"ok" + ls.size(),Toast.LENGTH_SHORT).show();

        for(int i = 0; i < ls.size(); i++) {

            String filename = ls.get(i) + ".jpg";
            //Toast.makeText(requireContext(),filename,Toast.LENGTH_SHORT).show();
            // Create a reference to the image file in Firebase Storage
            StorageReference imageRef = storageRef.child("user_profile_images/" + filename); // Replace "filename" with the name of the image file you want to download

            // Download the image from Firebase Storage into a byte array
            final long ONE_MEGABYTE = 1024 * 1024;
            final int[] finalI = {i};
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    if(!isAdded()){
                        return;
                    }
                    // Image downloaded successfully, convert the byte array to a Bitmap and display it in an ImageView
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Toast.makeText(requireContext(),"send to person adapter",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(requireContext(),sName.get(finalI[0]) + " - " + bitmap,Toast.LENGTH_SHORT).show();
                    Person p = new Person();
                    p.setName(sName.get(finalI[0]));
                    p.setImage(bitmap);
                    personAdapter.add(p);
                    finalI[0]++;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error downloading image from Firebase Storage: " + e.getMessage());
                }
            });
        }
        personAdapter.notifyDataSetChanged();
    }*/
    private ValueEventListener valueEventListener;
    public void getStudentId(){
        if(!isAdded()){
            return;
        }
        /*sName.clear();
        personAdapter.notifyDataSetChanged();*/
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users");

        DatabaseReference r = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");

        //Query query =

        //Log.d("Firebase", "Query parameter: " + r.orderByChild("subject_id").equalTo(subject_id).toString());

        //r.orderByChild("subject_id").equalTo(mngtchclass.subId);
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot dataSnapshot){
                    if (!isAdded()) {
                        return;
                    }
                    //r.orderByChild("subject_id").equalTo(mngtchclass.subId);
                    list.clear();
                    sName.clear();
                    personAdapter.notifyDataSetChanged();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if(childSnapshot.child("subject_id").getValue().toString().equals(mngtchclass.subId)) {
                            String student_id = childSnapshot.child("student_id").getValue().toString();
                            ls.add(student_id);
                            Log.w("Firebase", "" + student_id);


                            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!isAdded()) {
                                        return;
                                    }
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        if (Objects.equals(childSnapshot.child("id").getValue(String.class), student_id)) {
                                            String name = childSnapshot.child("name").getValue(String.class);
                                            sName.add(name + "/" + childSnapshot.child("id").getValue(String.class));
                                        }
                                    }
                                    personAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        // Do something with the sub ID
                    }

                    /*for (int i = 0; i < ls.size(); i++) {
                        int finalI = i;

                    }*/
                }
                @Override
                public void onCancelled (DatabaseError error){
                    // Handle error
                }
            };
            r.addValueEventListener(valueEventListener);
    }

    /*public void getStudentIdAndLoadFromDB() {
        // Create a new Runnable to gather the data
        Runnable dataRunnable = new Runnable() {
            @Override
            public void run() {
                // Gather data for the UI
                getStudentId();
                // When the data gathering is complete, post a Runnable on the main thread to update the UI
                //String studentId = "12345"; // Replace with your actual data
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadFromDB();
                        // Update the UI based on the gathered data
                        // For example, set text of TextView or setImageDrawable of ImageView
                    }
                });
            }
        };

        // Create a new thread to run the Runnable
        Thread dataThread = new Thread(dataRunnable);
        dataThread.start();
    }*/

    /*private Handler mHandler = new Handler();

    private Runnable getStudentIdAndLoadFromDB = new Runnable() {
        @Override
        public void run() {
            getStudentId();
            *//*mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadFromDB();
                }
            }, 1000); *//*// delay for 1000 milliseconds
        }
    };

    private void processStudentData() {
        new Thread(getStudentIdAndLoadFromDB).start();
    }*/
}