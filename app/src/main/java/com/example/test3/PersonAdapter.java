package com.example.test3;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PersonAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int mResource;

    public PersonAdapter(@NonNull Context context,  @NonNull ArrayList<String> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mResource = 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        final String[] raw = getItem(position).split("/",2);
        final String person = raw[0];
        final String id =  raw[1];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        // Lookup view for data population
        ImageView imageView = (ImageView) convertView.findViewById(R.id.profileImage1);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.student_name);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("photouris");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(childSnapshot.child("id").getValue(String.class).equals(id)) {
                        String uri = childSnapshot.child("uri").getValue(String.class);

                        Uri photoUrl = Uri.parse(uri);
                        Glide.with(mContext)
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

        // Check if an existing view is being reused, otherwise inflate the view


        //imageView.setImageBitmap(person.getImage());
        nameTextView.setText(person);

        return convertView;
    }
}
