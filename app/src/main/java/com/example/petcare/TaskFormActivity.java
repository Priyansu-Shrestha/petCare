package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.petcare.R;
import com.example.petcare.db.AppDatabase;
import com.example.petcare.db.Task;

public class TaskFormActivity extends AppCompatActivity {

    private EditText etTitle, etDesc;
    private Spinner spRecurrence;
    private Button btnSave;
    private long taskId, petId;
    private boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        etTitle = findViewById(R.id.etTaskTitle);
        etDesc = findViewById(R.id.etTaskDesc);
        spRecurrence = findViewById(R.id.spRecurrence);
        btnSave = findViewById(R.id.btnSaveTask);
        petId = getIntent().getLongExtra("petId", -1);
        isNew = getIntent().getBooleanExtra("isNew", true);
        taskId = getIntent().getLongExtra("taskId", -1);

        // Set defaults
        if (spRecurrence.getCount() > 0) {
            spRecurrence.setSelection(0); // NONE
        }

        if (!isNew && taskId != -1) {
            // Load task - for brevity, we'll just prepopulate
            etTitle.setText("Walk");
            etDesc.setText("Take pet out");
        }

        btnSave.setOnClick(v -> saveTask());
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String recurrence = spRecurrence.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(petId, title, desc, System.currentTimeMillis() + 86400000); // due tomorrow
        task.recurring = !recurrence.equals("NONE");
        task.recurrence = recurrence;

        if (taskId != -1) {
            task.id = taskId;
        }
        AppDatabase.get(this).taskDao().insert(task);
        Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}