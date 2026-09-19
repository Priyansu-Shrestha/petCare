package com.example.petcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.petcare.R;
import com.example.petcare.db.AppDatabase;
import com.example.petcare.db.Task;
import com.example.petcare.db.TaskDao;
import java.util.List;

public class TaskListFragment extends Fragment {

    private TaskAdapter adapter;
    private TaskDao taskDao;
    private long petId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        petId = getArguments().getLong("petId", -1);
        RecyclerView rvTasks = view.findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));

        taskDao = AppDatabase.get(requireContext()).taskDao();
        adapter = new TaskAdapter(requireContext(), task -> {
            // Mark done
            task.done = true;
            taskDao.update(task);
            adapter.submitList(getTasks());
        }, task -> {
            // Edit task
            Intent i = new Intent(requireContext(), TaskFormActivity.class);
            i.putExtra("taskId", task.id);
            i.putExtra("petId", petId);
            i.putExtra("taskTitle", task.title);
            i.putExtra("taskDesc", task.description);
            i.putExtra("dueMillis", task.dueMillis);
            i.putExtra("recurring", task.recurring);
            i.putExtra("recurrence", task.recurrence);
            startActivity(i);
        }, task -> {
            // Delete task
            taskDao.delete(task);
            adapter.submitList(getTasks());
        });

        rvTasks.setAdapter(adapter);

        if (petId != -1) {
            taskDao.getByPet(petId).observe(getViewLifecycleOwner(), adapter::submitList);
        }

        view.findViewById(R.id.fabAddTask).setOnClick(v -> {
            Intent i = new Intent(requireContext(), TaskFormActivity.class);
            i.putExtra("petId", petId);
            i.putExtra("isNew", true);
            startActivity(i);
        });

        return view;
    }

    private List<Task> getTasks() {
        return petId != -1 ? taskDao.getByPet(petId).getValue() : null;
    }
}