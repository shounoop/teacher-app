package com.example.stdmanager.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stdmanager.models.Grade;
import com.example.stdmanager.models.ReportTotal;
import com.example.stdmanager.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "STUDENT";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FAMILY_NAME = "familyName";

    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_GENDER = "gender";

    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_GRADE_ID = "gradeId";

    public StudentOpenHelper(@Nullable Context context) {
        super(context, DBConfig.getDatabaseName(), null, DBConfig.getDatabaseVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = String.format("CREATE TABLE %s (" + "%s INTEGER PRIMARY KEY AUTOINCREMENT," + "%s TEXT, " + "%s TEXT, " + "%s INTEGER, " + "%s TEXT, " + "%s INTEGER)", TABLE_NAME, COLUMN_ID, COLUMN_FAMILY_NAME, COLUMN_FIRST_NAME, COLUMN_GENDER, COLUMN_BIRTHDAY, COLUMN_GRADE_ID);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void create(Student student) {
        /*Step 1*/
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*Step 2*/
        String familyName = student.getFamilyName();
        String firstName = student.getFirstName();

        int gender = student.getGender();
        String birthday = student.getBirthday();
        int gradeId = student.getGradeId();

        /*Step 3*/
        contentValues.put(COLUMN_FAMILY_NAME, familyName);
        contentValues.put(COLUMN_FIRST_NAME, firstName);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_BIRTHDAY, birthday);
        contentValues.put(COLUMN_GRADE_ID, gradeId);

        /*Step 4*/
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void delete(Student student) {
        /*Step 1*/
        int id = student.getId();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        /*Step 2*/
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public void update(Student student) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        int id = student.getId();
        String familyName = student.getFamilyName();
        String firstName = student.getFirstName();

        int gender = student.getGender();
        String birthday = student.getBirthday();
        int gradeId = student.getGradeId();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FAMILY_NAME, familyName);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_BIRTHDAY, birthday);
        values.put(COLUMN_GRADE_ID, gradeId);

        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Student> retrieveAllStudents() {
        ArrayList<Student> objects = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT s.*, g.name FROM student s INNER JOIN grade g ON s.gradeId = g.id";
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();

                student.setId(Integer.parseInt(cursor.getString(0)));

                student.setFamilyName(cursor.getString(1));
                student.setFirstName(cursor.getString(2));

                student.setGender(Integer.parseInt(cursor.getString(3)));
                student.setBirthday(cursor.getString(4));

                student.setGradeId(Integer.parseInt(cursor.getString(5)));
                student.setGradeName(cursor.getString(6));

                objects.add(student);
            } while (cursor.moveToNext());
        }

        return objects;
    }

    public ArrayList<Student> getStudentInGrade(String grade) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Student> objects = new ArrayList<>();

        String query = "SELECT s.*" + "FROM student s " + "WHERE s.gradeid =  " + grade;
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();

                student.setId(Integer.parseInt(cursor.getString(0)));

                student.setFamilyName(cursor.getString(1));
                student.setFirstName(cursor.getString(2));

                student.setGender(Integer.parseInt(cursor.getString(3)));
                student.setBirthday(cursor.getString(4));

                student.setGradeId(Integer.parseInt(cursor.getString(5)));

                objects.add(student);
            } while (cursor.moveToNext());
        }

        return objects;
    }

    public ArrayList<Student> retrieveStudentWithKeyword(String keyword) {
        /*Step 1*/
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Student> objects = new ArrayList<>();

        /*Step 2*/
        String query = "SELECT s.*, g.name " + "FROM student s " + "INNER JOIN grade g " + "ON s.gradeId = g.id " + "WHERE s.familyName LIKE '" + keyword + "%' " + "OR s.firstName LIKE '" + keyword + "%' ";
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        /*Step 3*/
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();

                student.setId(Integer.parseInt(cursor.getString(0)));

                student.setFamilyName(cursor.getString(1));
                student.setFirstName(cursor.getString(2));

                student.setGender(Integer.parseInt(cursor.getString(3)));
                student.setBirthday(cursor.getString(4));

                student.setGradeId(Integer.parseInt(cursor.getString(5)));
                student.setGradeName(cursor.getString(6));

                objects.add(student);
            } while (cursor.moveToNext());
        }

        return objects;
    }


    private int count() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int quantity = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        return quantity;
    }

    public ArrayList<ReportTotal> countByGender() {

        ArrayList<ReportTotal> reportTotals = new ArrayList<>();

        String query = String.format("SELECT %s, count(id) FROM %s GROUP BY %s", COLUMN_GENDER, TABLE_NAME, COLUMN_GENDER);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Integer gender = cursor.getInt(0);
                Double value = cursor.getDouble(1);

                reportTotals.add(new ReportTotal(gender == 0 ? "Nam" : "Nữ", value));
            } while (cursor.moveToNext());
        }

        return reportTotals;
    }

    private void createDefaultRecords() {
        /*Step 1*/
        int studentQuantity = this.count();
        if (studentQuantity != 0) return;

        /*Step 2*/
        Student student1 = new Student("Thanh", "Phong", 0, "01/05/2001", 1);
        Student student2 = new Student("Dang", "Hau", 0, "20/01/2001", 1);
        Student student3 = new Student("Dinh", "Khang", 0, "24/03/2001", 1);
        Student student4 = new Student("Duc", "Thuan", 0, "12/03/20001", 1);
        Student student5 = new Student("Van", "Chung", 0, "21/07/20001", 1);
        Student student6 = new Student("Ngoc", "Thanh", 1, "12/01/2001", 1);
        Student student7 = new Student("Thu", "Ha", 1, "30/08/2001", 1);
        Student student8 = new Student("Truc", "Thy", 1, "04/12/2001", 1);

        Student student9 = new Student("Thanh", "Phong", 0, "01/05/2001", 4);
        Student student10 = new Student("Dang", "Hau", 0, "20/01/2001", 4);
        Student student11 = new Student("Dinh", "Khang", 0, "24/03/2001", 4);
        Student student12 = new Student("Duc", "Thuan", 0, "12/03/20001", 4);
        Student student13 = new Student("Van", "Chung", 0, "21/07/20001", 4);
        Student student14 = new Student("Ngoc", "Thanh", 1, "12/01/2001", 4);
        Student student15 = new Student("Thu", "Ha", 1, "30/08/2001", 4);
        Student student16 = new Student("Truc", "Thy", 1, "04/12/2001", 4);

        /*Step 3*/
        this.create(student1);
        this.create(student2);
        this.create(student3);
        this.create(student4);
        this.create(student5);
        this.create(student6);
        this.create(student7);
        this.create(student8);

        this.create(student9);
        this.create(student10);
        this.create(student11);
        this.create(student12);
        this.create(student13);
        this.create(student14);
        this.create(student15);
        this.create(student16);
    }

    public void deleteAndCreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        createDefaultRecords();
    }
}
