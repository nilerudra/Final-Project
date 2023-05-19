package com.example.test3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListOfLinks extends Fragment {

    ListView listView;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    ArrayList<String> itemList;
    ArrayAdapter<String> adapter;
    public ListOfLinks() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_of_links, container, false);

        listView = rootView.findViewById(R.id.listlinks);
        databaseReference = FirebaseDatabase.getInstance().getReference("Links").child(mngtchclass.subId);
        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.res,R.id.item, itemList);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                adapter.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String link = childSnapshot.child("link").getValue().toString();
                    itemList.add(link);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    public void onPause() {
        super.onPause();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}