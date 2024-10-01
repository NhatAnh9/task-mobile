package com.example.dailytaskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.TimePicker;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private TaskListActivity activity;

    // Constructor
    public TaskAdapter(List<Task> tasks, TaskListActivity activity) {
        this.tasks = tasks;
        this.activity = activity;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskNameTextView.setText(task.getTaskName());
        holder.taskTimeTextView.setText(task.getTaskTime());

        // Set up delete button functionality
        holder.deleteTaskButton.setOnClickListener(v -> activity.deleteTask(position));

        // Set up edit button functionality
        holder.editTaskButton.setOnClickListener(v -> {
            // Show a dialog to edit the task
            showEditDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Edit Task");

        View viewInflated = LayoutInflater.from(activity).inflate(R.layout.dialog_edit_task, null);
        final EditText inputName = viewInflated.findViewById(R.id.editTaskName);
        final TimePicker timePicker = viewInflated.findViewById(R.id.editTaskTimePicker);

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

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = inputName.getText().toString();

                // Get the time from the TimePicker
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String newTime = String.format("%02d:%02d", hour, minute);

                activity.editTask(position, newName, newTime);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView, taskTimeTextView;
        View deleteTaskButton, editTaskButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskTimeTextView = itemView.findViewById(R.id.taskTimeTextView);
            deleteTaskButton = itemView.findViewById(R.id.deleteTaskButton);
            editTaskButton = itemView.findViewById(R.id.editTaskButton);
        }
    }
}
