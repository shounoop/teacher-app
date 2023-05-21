package com.example.stdmanager.Settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.stdmanager.DB.TeacherDBHelper;
import com.example.stdmanager.R;
import com.example.stdmanager.TopBarMenuIconFragment;
import com.example.stdmanager.helpers.InterfaceHelper;
import com.example.stdmanager.models.Session;
import com.example.stdmanager.models.Teacher;

public class SettingsAccountActivity extends AppCompatActivity {
    private LinearLayout nameLinearLayout, passwordLinearLayout;
    private Session session;
    private TeacherDBHelper teacherOpenHelper;
    private Teacher teacher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);

        session = new Session(SettingsAccountActivity.this);
        teacherOpenHelper = new TeacherDBHelper(this);

        int id = Integer.parseInt(session.get("teacherId"));
        teacher = teacherOpenHelper.getTeacher(id);

        setControl();
        setEvent();
    }

    private void setControl() {
        nameLinearLayout = findViewById(R.id.linearLayoutName);
        passwordLinearLayout = findViewById(R.id.linearLayoutPassword);
    }

    private void setEvent() {
        nameLinearLayout.setOnClickListener(view -> {
            View popup = inflatePopupWindow(view, R.layout.activity_settings_account_edit_name);
            handleEventNameLayout(popup);
        });

        passwordLinearLayout.setOnClickListener(view -> {
            View popup = inflatePopupWindow(view, R.layout.activity_settings_account_edit_password);
            handleEventPasswordLayout(popup);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private View inflatePopupWindow(View view, int layout) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        InterfaceHelper.blurCurrentScreen(popupWindow);

        return popupView;
    }

    private void handleEventNameLayout(View view) {
        /*Step 1*/
        Button saveButton = view.findViewById(R.id.settingsAccountButtonSaveName);
        EditText teacherNameEdit = view.findViewById(R.id.settingsAccountEditTextName);
        teacherNameEdit.setText(teacher.getName());

        /*Step 2*/
        saveButton.setOnClickListener(view1 -> {
            String fullName = teacherNameEdit.getText().toString();

            teacher.setName(fullName);
            teacherOpenHelper.updateTeacher(teacher);

            Toast.makeText(SettingsAccountActivity.this, "Đổi tên thành công !", Toast.LENGTH_SHORT).show();
            TopBarMenuIconFragment.getmInstanceActivity().setData(teacher);
        });
    }

    private void handleEventPasswordLayout(View view) {
        /*Step 1*/
        Button savePassword = view.findViewById(R.id.settingsAccountButtonSavePassword);
        EditText password = view.findViewById(R.id.settingsAccountPassword);
        EditText newPassword = view.findViewById(R.id.settingsAccountNewPassword);
        EditText confirmationPassword = view.findViewById(R.id.settingsAccountConfirmationPassword);

        /*Step 2*/
        savePassword.setOnClickListener(view1 -> {
            String pass = password.getText().toString();
            String newPass = newPassword.getText().toString();
            String confirmationPass = confirmationPassword.getText().toString();

            boolean isValid = checkInputData(pass, newPass, confirmationPass);
            if (!isValid) return;

            teacher.setPassword(newPass);
            teacherOpenHelper.updateTeacher(teacher);
            Toast.makeText(SettingsAccountActivity.this, "Đổi tên mật khẩu thành công !", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkInputData(String pass, String newPass, String confirmationPass) {
        if (pass.equals("") || newPass.equals("") || confirmationPass.equals("")) {
            Toast.makeText(SettingsAccountActivity.this, "Điền đầy đủ các trường dữ liệu !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(teacher.getPassword())) {
            Toast.makeText(SettingsAccountActivity.this, "Mật khẩu nhập vào chưa chính xác !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPass.equals(pass)) {
            Toast.makeText(SettingsAccountActivity.this, "Mật khẩu nhập mới giống mật khẩu hiện tại  !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPass.equals(confirmationPass)) {
            Toast.makeText(SettingsAccountActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}