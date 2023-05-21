package com.example.stdmanager.Classroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.stdmanager.App;
import com.example.stdmanager.DB.GradeOpenHelper;
import com.example.stdmanager.DB.StudentOpenHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.ClassroomListViewModel;
import com.example.stdmanager.models.Grade;
import com.example.stdmanager.models.Session;
import com.example.stdmanager.models.Student;
import com.example.stdmanager.models.Teacher;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class ClassroomActivity extends AppCompatActivity {
    public static WeakReference<ClassroomActivity> weakActivity;
    private Session session;
    private ClassroomListViewModel listViewModel;
    private ListView listView;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Grade> gradeObjects;
    private GradeOpenHelper gradeOpenHelper = new GradeOpenHelper(this);
    private StudentOpenHelper studentOpenHelper = new StudentOpenHelper(this);
    private AppCompatButton buttonCreation;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        this.weakActivity = new WeakReference<>(ClassroomActivity.this);
        this.session = new Session(ClassroomActivity.this);

        // get all grades
        this.gradeObjects = gradeOpenHelper.retrieveAllGrades();
        // get all students
        this.students = studentOpenHelper.retrieveAllStudents();

        setControl();

        setEvent();
        searchByKeyword();

        String teacherId = this.session.get("teacherId");
        String value = gradeOpenHelper.retriveIdByTeachId(teacherId);
        this.session.set("gradeId", value);
    }

    public static ClassroomActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    private void setControl() {
        this.listView = findViewById(R.id.classroomListView);
        this.buttonCreation = findViewById(R.id.classroomButtonCreation);
        this.searchView = findViewById(R.id.classroomSearchView);
    }

    private void setEvent() {
        /*Step 1*/
        this.listViewModel = new ClassroomListViewModel(this, R.layout.activity_classroom_element, students);
        this.listView.setAdapter(listViewModel);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student student = (Student) adapterView.getAdapter().getItem(position);

                Intent intent = new Intent(ClassroomActivity.this, ClassroomIndividualActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        /*Step 2*/
        this.buttonCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassroomActivity.this, ClassroomCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createStudent(Student student) {
        /* Temporary Solution so as to the Grade Name is null */
        student.setGradeName(this.students.get(0).getGradeName());

        this.students.add(student);

        this.studentOpenHelper.create(student);

        this.listViewModel.notifyDataSetChanged();

        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_LONG).show();
    }

    public void deleteStudent(Student student) {
        if (student.getId() == 0) {
            Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getId() == student.getId()) {
                this.students.remove(this.students.get(i));
            }
        }

        this.listViewModel.notifyDataSetChanged();

        this.studentOpenHelper.delete(student);

        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
    }

    public void updateStudent(Student student) {
        if (student.getId() == 0) {
            Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_LONG).show();
            return;
        }

        // update
        for (Student element : this.students) {
            if (element.getId() == student.getId()) {
                element.setFamilyName(student.getFamilyName());
                element.setFirstName(student.getFirstName());
                element.setGender(student.getGender());
                element.setBirthday(student.getBirthday());
                element.setGradeId(student.getGradeId());
                element.setGradeName(student.getGradeName());
            }
        }

        // re-render
        this.listViewModel.notifyDataSetChanged();
        this.studentOpenHelper.update(student);

        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
    }

    private void searchByKeyword() {
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Student> filteredStudents = new ArrayList<>();

                for (Student student : students) {
                    String firstName = student.getFirstName().toLowerCase(Locale.ROOT);
                    String keyword = s.toLowerCase(Locale.ROOT);

                    if (firstName.contains(keyword)) {
                        filteredStudents.add(student);
                    }
                }

                setListView(filteredStudents);

                return false;
            }
        });
    }

    private void setListView(ArrayList<Student> array) {
        this.listViewModel = new ClassroomListViewModel(this, R.layout.activity_classroom_element, array);
        this.listView.setAdapter(listViewModel);
        this.listViewModel.notifyDataSetChanged();
    }
}