package com.example.stdmanager.Classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stdmanager.R;
import com.example.stdmanager.Score.ScoreStudentActivity;
import com.example.stdmanager.helpers.Alert;
import com.example.stdmanager.models.Student;

import java.lang.ref.WeakReference;

public class ClassroomIndividualActivity extends AppCompatActivity {

    public static WeakReference<ClassroomIndividualActivity> weakActivity;

    TextView studentFamilyName, studentFirstName, studentGradeName, studentBirthday, studentGender, contentAlert;
    Button buttonGoBack, buttonUpdate, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_individual);

        this.weakActivity = new WeakReference<>(ClassroomIndividualActivity.this);
        setControl();

        setScreen();
        setEvent();
    }

    public static ClassroomIndividualActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    private void setControl() {
        this.studentFamilyName = findViewById(R.id.studentFamilyName);
        this.studentFirstName = findViewById(R.id.studentFirstName);

        this.studentGradeName = findViewById(R.id.gradeName);
        this.studentBirthday = findViewById(R.id.birthday);

        this.buttonGoBack = findViewById(R.id.individualButtonGoBack);
        this.buttonUpdate = findViewById(R.id.individualButtonUpdate);
        this.buttonDelete = findViewById(R.id.individualButtonDelete);

        this.studentGender = findViewById(R.id.gender);
    }

    private void setScreen() {
        /*Step 1*/
        Student student = (Student) getIntent().getSerializableExtra("student");

        String familyName = student.getFamilyName();
        String firstName = student.getFirstName();
        String birthday = student.getBirthday();
        String gradeName = student.getGradeName();

        /*Step 2*/
        this.studentFamilyName.setText(familyName);
        this.studentFirstName.setText(firstName);
        this.studentBirthday.setText(birthday);
        this.studentGradeName.setText(gradeName);

        if (student.getGender() == 0) {
            this.studentGender.setText("Nam");
        } else {
            this.studentGender.setText("Nữ");
        }
    }

    private void setEvent() {
        Student student = (Student) getIntent().getSerializableExtra("student");

        this.buttonDelete.setOnClickListener(view -> triggerPopupWindow(view, student));
        this.buttonUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(ClassroomIndividualActivity.this, ClassroomUpdateActivity.class);
            intent.putExtra("updatedStudent", student);
            startActivity((intent));
        });
        this.buttonGoBack.setOnClickListener(view -> {
            finish();
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void triggerPopupWindow(View view, Student student) {
        Alert alert = new Alert(ClassroomIndividualActivity.this);
        alert.confirm();
        alert.showAlert(R.string.deleteStudent, R.drawable.info_icon);

        alert.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassroomActivity.getmInstanceActivity().deleteStudent(student);
                finish();
            }
        });

        alert.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }

    public void updateStudent(Student student) {
        this.studentFamilyName.setText(student.getFamilyName());
        this.studentFirstName.setText(student.getFirstName());
        this.studentBirthday.setText(student.getBirthday());
        this.studentGradeName.setText(student.getGradeName());

        if (student.getGender() == 0) {
            this.studentGender.setText("Nam");
        } else {
            this.studentGender.setText("Nữ");
        }
    }
}