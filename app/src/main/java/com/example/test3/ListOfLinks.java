package com.example.test3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView textView;
    LinkAdapter adapter;
    public ListOfLinks() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_of_links, container, false);

        textView = rootView.findViewById(R.id.placeholder);
        listView = rootView.findViewById(R.id.listlinks);
        databaseReference = FirebaseDatabase.getInstance().getReference("Links").child(uploadstdmaterial.sub_Id);
        itemList = new ArrayList<>();
        adapter = new LinkAdapter(getActivity(), itemList);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //textView.setVisibility(View.VISIBLE);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                adapter.clear();
                adapter.notifyDataSetChanged();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if(childSnapshot.exists() && childSnapshot.child("linktitle").getValue() != null && childSnapshot.child("link").getValue() != null) {
                            String title = childSnapshot.child("linktitle").getValue().toString();
                            String link = childSnapshot.child("link").getValue().toString();
                            itemList.add(title + ":" + link);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if(itemList.isEmpty())
                    {
                        textView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        textView.setVisibility(View.GONE);
                    }
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textView.setVisibility(View.VISIBLE);
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