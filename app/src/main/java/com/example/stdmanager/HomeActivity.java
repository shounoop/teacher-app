package com.example.stdmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.stdmanager.Classroom.ClassroomActivity;
import com.example.stdmanager.Event.EventActivity;
import com.example.stdmanager.Score.ScoreSubjectActivity;
import com.example.stdmanager.Settings.SettingsAccountActivity;
import com.example.stdmanager.Statistic.StatisticActivity;
import com.example.stdmanager.Subject.SubjectActivity;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    ImageButton btnStatistic, btnClassroom, btnSubject, btnEvent, btnScore, btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setControl();
        setEvent();
    }

    private void setControl() {
        this.btnStatistic = findViewById(R.id.btnHomeStatistic);
        this.btnClassroom = findViewById(R.id.btnHomeClassroom);
        this.btnSubject = findViewById(R.id.btnHomeSubject);
        this.btnEvent = findViewById(R.id.btnHomeEvent);
        this.btnScore = findViewById(R.id.btnHomeScore);
        this.btnAccount = findViewById(R.id.btnHomeAccount);
    }

    @SuppressLint("RestrictedApi")
    private void setEvent() {
        // 1. Classroom
        this.btnClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ClassroomActivity.class);
                startActivity(intent);
            }
        });

        // 2. Subject
        this.btnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });

        // 3. Event
        this.btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        // 4. Score
        this.btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ScoreSubjectActivity.class);
                startActivity(intent);
            }
        });

        // 5. Statistic
        this.btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });

        // 6. Account
        this.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingsAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}