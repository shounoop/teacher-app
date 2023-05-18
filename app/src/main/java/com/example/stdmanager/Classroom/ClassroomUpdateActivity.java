package com.example.stdmanager.Classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stdmanager.App;
import com.example.stdmanager.R;
import com.example.stdmanager.models.Student;
import com.example.stdmanager.models.Teacher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ClassroomUpdateActivity extends AppCompatActivity {

    EditText familyName, firstName, birthday;
    RadioButton male, female;

    ImageButton buttonBirthday;
    AppCompatButton buttonConfirm, buttonCancel;

    private final Calendar cal = Calendar.getInstance();
    private final int currentYear = cal.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_update);
        setControl();
        setScreen();
        setEvent();
    }

    private void setControl() {
        buttonBirthday = findViewById(R.id.classroomUpdateButtonBirthday);
        buttonConfirm = findViewById(R.id.classroomUpdateButtonConfirm);
        buttonCancel = findViewById(R.id.classroomUpdateButtonGoBack);

        familyName = findViewById(R.id.classroomUpdateFamilyName);
        firstName = findViewById(R.id.classroomUpdateFirstName);
        birthday = findViewById(R.id.classroomUpdateBirthday);

        male = findViewById(R.id.classroomUpdateRadioButtonMale);
        female = findViewById(R.id.classroomUpdateRadioButtonFemale);
    }


    private void setScreen() {
        Student student = (Student) getIntent().getSerializableExtra("updatedStudent");

        familyName.setText(student.getFamilyName());
        firstName.setText(student.getFirstName());

        if (student.getGender() == 0) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        birthday.setText(student.getBirthday());
    }

    public void openDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;

            Date date = new Date(year - 1900, month, day);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String birthdayValue = formatter.format(date);
            birthday.setText(birthdayValue);
        };

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style;
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        } else {
            style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        }

        DatePickerDialog datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.show();
    }

    private void setEvent() {
        Student student = (Student) getIntent().getSerializableExtra("updatedStudent");

        Student newStudent = new Student();

        // You can use this method reference as an argument to pass the method
        // as a callback or event handler
        this.birthday.setOnClickListener(this::openDatePicker);

        this.buttonConfirm.setOnClickListener(view -> {
            int id = student.getId();
            int gradeId = student.getGradeId();
            String gradeName = student.getGradeName();
            String family = familyName.getText().toString();
            String first = firstName.getText().toString();
            int gender = male.isChecked() ? 0 : 1;
            String birth = birthday.getText().toString();

            newStudent.setId(id);
            newStudent.setFamilyName(family);
            newStudent.setFirstName(first);
            newStudent.setGender(gender);
            newStudent.setBirthday(birth);
            newStudent.setGradeId(gradeId);
            newStudent.setGradeName(gradeName);

            boolean isValid = validateStudentInformation(newStudent);
            if (!isValid) return;

            ClassroomActivity.getmInstanceActivity().updateStudent(newStudent);
            ClassroomIndividualActivity.getmInstanceActivity().updateStudent(newStudent);
        });

        this.buttonCancel.setOnClickListener(view -> finish());
    }

    private boolean validateStudentInformation(Student student) {
        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ" + "áảấẩắẳóỏốổớởíỉýỷéẻếểạậặọộợịỵẹệãẫẵõỗỡĩỹẽễàầằòồờìỳèềaâăoôơiyeêùừụựúứủửũữuư";

        // It creates a Pattern object using the regular expression pattern
        Pattern pattern = Pattern.compile("(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[a-zA-Z])++");

        boolean flagFamilyName = pattern.matcher(student.getFamilyName()).matches();
        boolean flagFirstName = pattern.matcher(student.getFirstName()).matches();

        if (!flagFamilyName || !flagFirstName) {
            Toast.makeText(ClassroomUpdateActivity.this, "Nội dung nhập vào không hợp lệ", Toast.LENGTH_LONG).show();
            return false;
        }

        int yearBirthday = Integer.parseInt(student.getBirthday().substring(6));
        int age = this.currentYear - yearBirthday;
        if (age < 18) {
            Toast.makeText(ClassroomUpdateActivity.this, "Tuổi không nhỏ hơn 18", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}