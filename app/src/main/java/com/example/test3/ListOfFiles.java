package com.example.test3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class ListOfFiles extends Fragment {


    ListView listView;
    public ListOfFiles() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_of_files, container, false);

        listView = view.findViewById(R.id.listres);

        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");
        itemList.add("Item 4");
        itemList.add("Item 5");
        itemList.add("Item 6");
        itemList.add("Item 7");
        itemList.add("Item 8");
        itemList.add("Item 9");
        itemList.add("Item 10");
        itemList.add("Item 12");
        itemList.add("Item 13");
        itemList.add("Item 14");
        itemList.add("Item 15");
        itemList.add("Item 16");
        itemList.add("Item 17");
        itemList.add("Item 18");
        itemList.add("Item 19");
        itemList.add("Item 20");
        itemList.add("Item 21");
        itemList.add("Item 22");
        itemList.add("Item 23");
        itemList.add("Item 24");
        itemList.add("Item 25");
        itemList.add("Item 26");
        itemList.add("Item 27");
        itemList.add("Item 28");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.res,R.id.item, itemList);
        listView.setAdapter(adapter);



        return view;
    }
}