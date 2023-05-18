package com.example.stdmanager.listViewModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stdmanager.Classroom.ClassroomActivity;
import com.example.stdmanager.Classroom.ClassroomIndividualActivity;
import com.example.stdmanager.R;
import com.example.stdmanager.models.Student;

import java.util.ArrayList;

public class ClassroomListViewModel extends ArrayAdapter<Student> {
    private Context context;
    private int resource;
    private ArrayList<Student> objects;

    public ClassroomListViewModel(@NonNull Context context, int resource, @NonNull ArrayList<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public int count() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // ImageView avatar = convertView.findViewById(R.id.studentAvatar);
        TextView name = convertView.findViewById(R.id.studentName);
        TextView grade = convertView.findViewById(R.id.studentGrade);

        Student student = objects.get(position);
        String studentName = student.getFamilyName() + " " + student.getFirstName();
        String studentGrade = student.getGradeName();

        name.setText(studentName);
        grade.setText(studentGrade);

        return convertView;
    }
}
