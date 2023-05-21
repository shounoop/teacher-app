package com.example.stdmanager.Score;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stdmanager.DB.ScoreDBHelper;
import com.example.stdmanager.DB.SubjectDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.ScoreSubjectAdapter;
import com.example.stdmanager.models.Session;
import com.example.stdmanager.models.Subject;

import java.util.ArrayList;

public class ScoreSubjectActivity extends AppCompatActivity {
    private SearchView scoreSearch;
    private ListView listView;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ScoreSubjectAdapter listViewModel;
    private SubjectDBHelper subjectDB = new SubjectDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_subject);

        subjects = subjectDB.getAllSubjects();

        setControl();
        setEvent();
        inItSearchWidgets();
    }

    private void setControl() {
        scoreSearch = findViewById(R.id.ScoreSearch);
        listView = findViewById(R.id.score_subject_list_view);
    }

    private void setEvent() {
        listViewModel = new ScoreSubjectAdapter(this, R.layout.activity_score_subject_element, subjects);
        listView.setAdapter(listViewModel);
    }

    private void inItSearchWidgets() {
        scoreSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Subject> filteredSubject = new ArrayList<Subject>();

                for (Subject subject : subjects) {
                    if (subject.getTenMH().toLowerCase().trim().contains(s.toLowerCase().trim())) {
                        filteredSubject.add(subject);
                    }
                }

                setFilteredSubject(filteredSubject);
                return false;
            }
        });
    }

    private void setFilteredSubject(ArrayList<Subject> filtered) {
        ScoreSubjectAdapter subjectAdapter = new ScoreSubjectAdapter(this, R.layout.activity_score_subject_element, filtered);
        listView.setAdapter(subjectAdapter);
    }
}
