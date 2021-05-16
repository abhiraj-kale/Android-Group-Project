package com.example.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link view_task_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class view_task_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public view_task_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment view_task.
     */
    // TODO: Rename and change types and number of parameters
    public static view_task_fragment newInstance(String param1, String param2) {
        view_task_fragment fragment = new view_task_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_task, container, false);

        LinearLayout linearLayout = view.findViewById(R.id.task_list);

        DBHelper DB = new DBHelper(requireActivity());
        Cursor cursor = DB.getAllData(); // Get data from the database

        if(cursor.getCount()==0){
            Toast.makeText(requireActivity(), "There are no tasks present.", Toast.LENGTH_LONG).show();
        }else{

            cursor.moveToFirst();
           do{
               // Iterate through all the rows got from database
                String title = cursor.getString(cursor.getColumnIndex("Task"));
                String id = String.valueOf(cursor.getString(cursor.getColumnIndex("id")));

               MaterialCardView cardView = new MaterialCardView(requireActivity());
               LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               layoutParams.setMargins(18,5,18,5);
               cardView.setLayoutParams(layoutParams);

                // Create the textview of task
                TextView textView = new TextView(requireActivity());
                textView.setTag(id); // Set the id for the textview to be used when clicked
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setLayoutParams(params);
                textView.setText(title);
                textView.setTextSize(20);
                textView.setPadding(50,5,5,5);
                textView.setTextColor(Color.BLACK);

                // Set On click listener for the task
               textView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(requireActivity(), open_task.class);
                       intent.putExtra("id", id);
                       startActivity(intent);
                   }
               });

               // Set long click listener to delete the task
               textView.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       new AlertDialog.Builder(requireActivity())
                               .setCancelable(false)
                               .setTitle("Delete task?")
                               .setMessage("Do you want to permanently delete the task?")
                               .setPositiveButton("YES", (dialog, which) -> {
                                   DB.deleteUserData(id);
                                   setUserVisibleHint(true);
                                   Toast.makeText(requireActivity(), "Your task has been deleted.", Toast.LENGTH_SHORT).show();
                               })
                               .setNegativeButton("NO", (dialog, which) -> dialog.cancel())
                               .show();
                       return true;
                   }
               });

               // Add the task to card
                cardView.addView(textView);
                // Add the card to the linear layout
                linearLayout.addView(cardView);
            } while(cursor.moveToNext());
        }

        return view;
    }
}