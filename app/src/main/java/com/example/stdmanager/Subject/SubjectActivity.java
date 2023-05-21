package com.example.stdmanager.Subject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.stdmanager.Classroom.ClassroomActivity;
import com.example.stdmanager.DB.GradeOpenHelper;
import com.example.stdmanager.DB.StudentOpenHelper;
import com.example.stdmanager.DB.SubjectDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.ClassroomListViewModel;
import com.example.stdmanager.listViewModels.SubjectAdapter;
import com.example.stdmanager.models.Grade;
import com.example.stdmanager.models.Session;
import com.example.stdmanager.models.Student;
import com.example.stdmanager.models.Subject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {
    public static WeakReference<SubjectActivity> weakActivity;

    private ListView listView;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private SubjectAdapter listViewModel;
    private SubjectDBHelper subjectDB = new SubjectDBHelper(this);
    private AppCompatButton Btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        this.subjectDB.open();
        this.weakActivity = new WeakReference<>(SubjectActivity.this);

        this.subjects = this.subjectDB.getAllSubjects();

        this.setControl();

        this.setEvent();

        this.initSearchWidgets();
    }

    private void setControl() {
        this.listView = findViewById(R.id.subjectListView);
        this.Btn_add = findViewById(R.id.subjectButtonCreation);
    }

    private void setEvent() {
        this.listViewModel = new SubjectAdapter(this, R.layout.activity_subject_element, this.subjects);
        this.listView.setAdapter(this.listViewModel);

        this.Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubjectAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSearchWidgets() {
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
        SubjectAdapter subjectAdapter = new SubjectAdapter(this, R.layout.activity_subject_element, filtered);
        this.listView.setAdapter(subjectAdapter);
    }

    public void addSubject(Subject subject) {
        if (this.subjectDB.AddSubject(subject)) {
            this.subjects.add(subject);
            this.listViewModel.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    public void updateSubject(Subject subject) {
        if (this.subjectDB.update(subject)) {
            for (Subject item : this.subjects) {
                if (item.getMaMH() == subject.getMaMH()) {
                    item.setTenMH(subject.getTenMH());
                    item.setHocKy(subject.getHocKy());
                    item.setHeSo(subject.getHeSo());
                    item.setNamHoc(subject.getNamHoc());
                }
            }

            this.listViewModel.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    public void delSubject(Subject subject) {
        if (this.subjectDB.deleteSubject(subject)) {
            this.subjects.remove(subject);
            this.listViewModel.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();

    }

    public static SubjectActivity getmInstanceActivity() {
        return weakActivity.get();
    }
}