package com.example.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public create_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static create_fragment newInstance(String param1, String param2) {
        create_fragment fragment = new create_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_fragment, container, false);

        EditText et_title = (EditText)view.findViewById(R.id.create_task_name);
        EditText et_content = (EditText)view.findViewById(R.id.create_task_content);

        Button create_task = view.findViewById(R.id.create_task_btn);
        create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                if(title.trim().isEmpty() || content.trim().isEmpty())
                    Toast.makeText(requireActivity(), "Fields can't be empty.", Toast.LENGTH_SHORT).show();
                else{
                    DBHelper DB = new DBHelper(requireActivity());
                    if(DB.insertUserTasks(title, content)){
                        et_title.setText("");
                        et_content.setText("");
                        new AlertDialog.Builder(requireActivity())
                                .setCancelable(false)
                                .setTitle("Task created.")
                                .setMessage("Your task has been created successfully.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                                .show();
                    }
                }
            }
        });
        return view;
    }
}