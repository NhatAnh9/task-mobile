package com.example.dailytaskmanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dailytaskmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addTaskButton.setOnClickListener(v -> {
            String taskName = binding.taskNameEditText.getText().toString();
            int hour = binding.taskTimePicker.getHour();
            int minute = binding.taskTimePicker.getMinute();
            String taskTime = String.format("%02d:%02d", hour, minute);

            Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
            intent.putExtra("TASK_NAME", taskName);
            intent.putExtra("TASK_TIME", taskTime);
            startActivity(intent);
        });
    }
}
