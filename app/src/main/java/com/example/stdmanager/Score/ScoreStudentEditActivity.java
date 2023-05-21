package com.example.stdmanager.Score;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.stdmanager.DB.ScoreDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.helpers.Alert;
import com.example.stdmanager.helpers.InputFilterMinMax;
import com.example.stdmanager.models.ScoreInfo;
import com.example.stdmanager.models.Session;

public class ScoreStudentEditActivity extends AppCompatActivity {
    private Session session;
    private ScoreInfo score;
    private ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
    private TextView tvSubject, tvStudentID, tvStudentName;
    private EditText editScore;
    private AppCompatButton buttonSave;
    private Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_student_edit);

        session = new Session(ScoreStudentEditActivity.this);
        score = (ScoreInfo) getIntent().getSerializableExtra("score");

        alert = new Alert(ScoreStudentEditActivity.this);
        alert.normal();

        this.setControl();
        this.setEvent();
    }

    @SuppressLint("SetTextI18n")
    private void setControl() {
        tvSubject = findViewById(R.id.tv_score_student_edit_subject);
        tvStudentID = findViewById(R.id.tv_score_student_edit_student_id);
        tvStudentName = findViewById(R.id.tv_score_student_edit_student_name);
        editScore = findViewById(R.id.edit_text_score_student_edit_score);
        buttonSave = findViewById(R.id.button_scoreave_student_s);
        editScore.setFilters(new InputFilter[]{new InputFilterMinMax("0", "10")});

        tvSubject.setText(score.getSubjectName());
        tvStudentID.setText(String.valueOf(score.getStudentID()));
        editScore.setText(String.valueOf(score.getScore()));
        tvStudentName.setText(score.getStudentFullName());
    }

    private void setEvent() {
        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double scoreValue = Double.parseDouble(editScore.getText().toString());

                if (scoreValue > 10 || scoreValue < 0) {
                    alert.showAlert("Điểm nhập vào không hợp lệ", R.drawable.info_icon);
                } else {
                    try {
                        score.setScore(scoreValue);

                        scoreDBHelper.update(score.getStudentID(), score.getSubjectID(), score.getScore());
                        alert.showAlert("Thay đổi điểm thành công", R.drawable.check_icon);
                    } catch (Exception ex) {
                        alert.showAlert(ex.toString(), R.drawable.info_icon);
                    }
                }

            }
        });

        this.alert.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });
            }
        });
    }
}
