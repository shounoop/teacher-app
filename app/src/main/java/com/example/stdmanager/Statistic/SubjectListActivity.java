package com.example.stdmanager.Statistic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.stdmanager.DB.SubjectDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.SubjectAdapter;
import com.example.stdmanager.listViewModels.SubjectListAdapter;
import com.example.stdmanager.models.Statistic;
import com.example.stdmanager.models.Subject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SubjectListActivity extends AppCompatActivity {
    public static WeakReference<SubjectListActivity> weakActivity;
    private Statistic statistic;
    private ListView listView;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private SubjectListAdapter subjectListAdapter;
    private SubjectDBHelper subjectDB = new SubjectDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        weakActivity = new WeakReference<>(SubjectListActivity.this);

        statistic = (Statistic) getIntent().getSerializableExtra("detail");

        subjects = subjectDB.getAllSubjects();

        setControl();
        setEvent();
        inItSearchWidgets();
    }

    private void setControl() {
        this.listView = findViewById(R.id.subjectListView);
    }

    private void setEvent() {
        this.subjectListAdapter = new SubjectListAdapter(this, R.layout.statistic_subject_element, subjects, statistic);
        this.listView.setAdapter(this.subjectListAdapter);
    }

    private void inItSearchWidgets() {
        SearchView searchView = findViewById(R.id.searchSubject);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        SubjectListAdapter subjectAdapter = new SubjectListAdapter(this, R.layout.statistic_subject_element, filtered, statistic);
        this.listView.setAdapter(subjectAdapter);
    }

    public static SubjectListActivity getmInstanceActivity() {
        return weakActivity.get();
    }
}