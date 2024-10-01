package com.example.dailytaskmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.dailytaskmanager.databinding.ActivityTaskListBinding;
import android.content.Intent;

import android.widget.TimePicker;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskListActivity extends AppCompatActivity {
    private ActivityTaskListBinding binding;
    private TaskAdapter adapter;
    private List<Task> tasks = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("TaskPrefs", MODE_PRIVATE);
        loadTasks();

        if (getIntent().hasExtra("TASK_NAME") && getIntent().hasExtra("TASK_TIME")) {
            String taskName = getIntent().getStringExtra("TASK_NAME");
            String taskTime = getIntent().getStringExtra("TASK_TIME");
            Task newTask = new Task(taskName, taskTime);

            if (isNewTask(taskName, taskTime)) {
                tasks.add(newTask);
                saveTasks();
            }
        }

        adapter = new TaskAdapter(tasks, this);
        binding.tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.tasksRecyclerView.setAdapter(adapter);

        binding.addTaskFab.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void saveTasks() {
        Set<String> taskSet = new HashSet<>();
        for (Task task : tasks) {
            taskSet.add(task.getTaskName() + "|" + task.getTaskTime());
        }
        sharedPreferences.edit().putStringSet("TASK_LIST", taskSet).apply();
    }

    private void loadTasks() {
        Set<String> taskSet = sharedPreferences.getStringSet("TASK_LIST", new HashSet<>());
        tasks.clear();
        for (String taskString : taskSet) {
            String[] taskParts = taskString.split("\\|");
            if (taskParts.length == 2) {
                tasks.add(new Task(taskParts[0], taskParts[1]));
            }
        }
    }

    private boolean isNewTask(String taskName, String taskTime) {
        for (Task task : tasks) {
            if (task.getTaskName().equals(taskName) && task.getTaskTime().equals(taskTime)) {
                return false;
            }
        }
        return true;
    }

    public void editTask(int position, String newName, String newTime) {
        Task task = tasks.get(position);
        task.setTaskName(newName);
        task.setTaskTime(newTime);
        adapter.notifyItemChanged(position);
        saveTasks();
    }

    public void deleteTask(int position) {
        tasks.remove(position);
        adapter.notifyItemRemoved(position);
        saveTasks();
    }
    public void showEditDialog(int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_task, null);
        final EditText inputName = viewInflated.findViewById(R.id.editTaskName);
        final TimePicker timePicker = viewInflated.findViewById(R.id.editTaskTimePicker);

        // Pre-fill the fields with the current task's details
        Task currentTask = tasks.get(position);
        inputName.setText(currentTask.getTaskName());

        // Extract the hour and minute from the current task's time (assuming format "HH:mm")
        String[] timeParts = currentTask.getTaskTime().split(":");
        if (timeParts.length == 2) {
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        builder.setView(viewInflated);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString();

            // Get the time from the TimePicker
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String newTime = String.format("%02d:%02d", hour, minute);

            editTask(position, newName, newTime);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}
