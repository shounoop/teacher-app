package com.example.stdmanager.Score;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.stdmanager.DB.GradeOpenHelper;
import com.example.stdmanager.DB.ScoreDBHelper;
import com.example.stdmanager.DB.StudentOpenHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.listViewModels.ScoreStudentAdapter;
import com.example.stdmanager.models.Score;
import com.example.stdmanager.models.ScoreInfo;
import com.example.stdmanager.models.Session;
import com.example.stdmanager.models.Student;
import com.example.stdmanager.models.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ScoreStudentActivity extends AppCompatActivity {
    Session session;
    private int pageHeight = 1120;
    private int pagewidth = 792;

    private ListView listView;
    private Subject subject;
    private ArrayList<Score> objects = new ArrayList<>();
    private ArrayList<ScoreInfo> scoreInfoArrayList = new ArrayList<>();
    private ScoreStudentAdapter listViewModel;

    private GradeOpenHelper gradeOpenHelper = new GradeOpenHelper(this);
    private ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
    private StudentOpenHelper studentDB = new StudentOpenHelper(this);

    private TextView tvSubject;
    private SearchView searchView;
    private String teacher;
    private String grade;
    private String gradeName;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_student);

        session = new Session(ScoreStudentActivity.this);
        teacher = session.get("teacherId");

        grade = gradeOpenHelper.retriveIdByTeachId(teacher);
        gradeName = gradeOpenHelper.retrieveNameById(Integer.parseInt(grade));

        subject = (Subject) getIntent().getSerializableExtra("subject");

        objects = scoreDBHelper.getAll();

        setControl();
        setEvent();
    }

    @SuppressLint("SetTextI18n")
    private void setControl() {
        this.listView = findViewById(R.id.score_student_list_view);
        this.searchView = findViewById(R.id.score_student_search_bar);
        this.tvSubject = findViewById(R.id.score_student_subject_name);

        this.tvSubject.setText("Môn học: " + subject.getTenMH());
    }

    private void setEvent() {
        addStudentScore();

        try {
            scoreInfoArrayList = getData();
            listViewModel = new ScoreStudentAdapter(this, R.layout.activity_score_student_element, scoreInfoArrayList);
            listView.setAdapter(listViewModel);
        } catch (NullPointerException ex) {
            finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ScoreStudentActivity.this.listViewModel.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ScoreInfo> filtered = new ArrayList<>();
                for (ScoreInfo score : scoreInfoArrayList) {
                    if (score.getStudentFullName().toLowerCase().trim().contains(newText.toLowerCase().trim()) || String.valueOf(score.getStudentID()).toLowerCase().trim().contains(newText.toLowerCase().trim())) {
                        filtered.add(score);
                    }
                }

                filteredScore(filtered);
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // nếu một hs nào đó chưa có điểm của môn học này thì thêm điểm 0 vào db
    private void addStudentScore() {
        ArrayList<Student> students = studentDB.getStudentInGrade(grade);

        for (Student student : students) {
            ArrayList<Score> scoresByStudentIdAndSubjectId = scoreDBHelper.getStudentAndSubject(String.valueOf(student.getId()), String.valueOf(subject.getMaMH()));

            if (scoresByStudentIdAndSubjectId.size() > 0) continue;

            Score score = new Score(student.getId(), subject.getMaMH(), 0);
            scoreDBHelper.add(score);
        }
    }

    private ArrayList<ScoreInfo> getData() {
        ArrayList<ScoreInfo> scoreInfoArrayList = new ArrayList<>();
        ArrayList<Student> students = studentDB.getStudentInGrade(grade);

        for (Student student : students) {
            ArrayList<Score> scoresByStudentIdAndSubjectId = scoreDBHelper.getStudentAndSubject(String.valueOf(student.getId()), String.valueOf(subject.getMaMH()));
            if (scoresByStudentIdAndSubjectId.size() <= 0) {
                return null;
            }

            // lấy điểm đầu tiên
            Score i = scoresByStudentIdAndSubjectId.get(0);
            ScoreInfo sci = new ScoreInfo(i.getMaHS(), i.getMaMH(), i.getDiem(), student.getFamilyName() + " " + student.getFirstName(), subject.getTenMH());
            scoreInfoArrayList.add(sci);
        }
        return scoreInfoArrayList;
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(17);
        title.setColor(ContextCompat.getColor(this, R.color.black));
        canvas.drawText("Danh sách điểm sinh viên.", 100, 50, title);

        title.setTextSize(15);
        canvas.drawText("Môn:", 100, 75, title);
        canvas.drawText("Lớp:", 100, 100, title);
        canvas.drawText(subject.getTenMH(), 200, 75, title);
        canvas.drawText(gradeName, 200, 100, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);

        int y = 130;
        for (ScoreInfo score : scoreInfoArrayList) {
            if (y >= 1000) {
                y = 130;
            }
            y += 30;
            canvas.drawText(String.valueOf(score.getStudentID()), 100, y, title);
            canvas.drawText(String.valueOf(score.getStudentFullName()), 200, y, title);
            canvas.drawText(String.valueOf(score.getScore()), 700, y, title);
        }
        pdfDocument.finishPage(myPage);

        File file = new File(getApplicationContext().getFilesDir(), subject.getMaMH() + "-" + subject.getTenMH() + "-" + gradeName + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(ScoreStudentActivity.this, "PDF file generated successfully." + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("GeneratePDF", "PDF file generated successfully" + e.toString());
            Toast.makeText(ScoreStudentActivity.this, "PDF file generated Failed.", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }

    private void filteredScore(ArrayList<ScoreInfo> filtered) {
        listViewModel = new ScoreStudentAdapter(this, R.layout.activity_score_student_element, filtered);
        listView.setAdapter(listViewModel);
    }
}
