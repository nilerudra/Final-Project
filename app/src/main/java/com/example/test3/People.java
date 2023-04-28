package com.example.test3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class People extends Fragment {
    ListView listView;
    ArrayList<String> ls;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
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
        listView = view.findViewById(R.id.Listview);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(requireContext() ,R.layout.list_item, list);
        listView.setAdapter(adapter);

        ls = new ArrayList();
        getStudentId();
        return view;
    }

    public void getStudentId(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");

        //Query query =

        //Log.d("Firebase", "Query parameter: " + r.orderByChild("subject_id").equalTo(subject_id).toString());

        r.orderByChild("subject_id").equalTo(mngtchclass.subId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String student_id = childSnapshot.child("student_id").getValue(String.class);
                    ls.add(student_id);
                    Log.w("Firebase", "" + student_id);
                    // Do something with the sub ID
                }

                for(int i = 0; i < ls.size(); i++){
                    int finalI = i;
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                if(Objects.equals(childSnapshot.child("id").getValue(String.class), ls.get(finalI))){
                                    String name = childSnapshot.child("name").getValue(String.class);
                                    list.add(name);
                                }
                            }
                            adapter.notifyDataSetChanged(); // update the ListView
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }
}