package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class open_task extends AppCompatActivity {
    EditText et_title, et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);

        String id = getIntent().getStringExtra("id");
        DBHelper DB = new DBHelper(open_task.this);
        et_title = findViewById(R.id.open_task_title);
        et_content = findViewById(R.id.open_task_content);

        Cursor cursor = DB.getInfo(id);
        if (cursor.getCount()==1){
            cursor.moveToFirst();
            et_title.setText(cursor.getString(cursor.getColumnIndex("Task")));
            et_content.setText(cursor.getString(cursor.getColumnIndex("Description")));
        }else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();;

        Button save_changes = findViewById(R.id.save_changes);

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_title = et_title.getText().toString();
                String new_content = et_content.getText().toString();

                if(new_title.trim().isEmpty() || new_content.trim().isEmpty())
                    Toast.makeText(open_task.this, "Fields can't be empty.", Toast.LENGTH_SHORT).show();
                else{
                    if(DB.updateUserTasks(new_title, new_content, id)){
                        new AlertDialog.Builder(open_task.this)
                                .setCancelable(false)
                                .setTitle("Task created.")
                                .setMessage("Your task has been updated successfully.")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    startActivity(new Intent(open_task.this, MainActivity.class));
                                    ActivityCompat.finishAffinity(open_task.this);
                                })
                                .show();
                    }
                }
            }
        });

        Button delete_task = findViewById(R.id.delete_task);
        delete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(open_task.this)
                        .setCancelable(false)
                        .setTitle("Delete task?")
                        .setMessage("Do you want to permanently delete the task?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            DB.deleteUserData(id);
                            Toast.makeText(open_task.this, "Your task has been deleted.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(open_task.this, MainActivity.class));
                            ActivityCompat.finishAffinity(open_task.this);
                        })
                        .setNegativeButton("NO", (dialog, which) -> dialog.cancel())
                        .show();
            }
        });

    }
}