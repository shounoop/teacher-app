package com.example.stdmanager.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stdmanager.models.Teacher;
import com.example.stdmanager.models.Grade;

import java.util.ArrayList;
import java.util.List;

public class GradeOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "GRADE";
    private static final String REFERENCED_TABLE_NAME = "TEACHER";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TEACHER_ID = "teacherId";

    public GradeOpenHelper(@Nullable Context context) {
        super(context, DBConfig.getDatabaseName(), null, DBConfig.getDatabaseVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("" + "CREATE TABLE %s(" + "%s INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT, " + "%s INTEGER REFERENCES [" + REFERENCED_TABLE_NAME + "](id) ON DELETE CASCADE NOT NULL  )", TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_TEACHER_ID);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void create(Grade grade) {
        /*Step 1*/
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        /*Step 2*/
        String name = grade.getName();
        int teacherId = grade.getTeacherId();


        /*Step 3*/
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_TEACHER_ID, teacherId);

        /*Step 4*/
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void delete(Grade grade) {
        /*Step 1*/
        int id = grade.getId();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        /*Step 2*/
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public void update(Grade grade) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        int id = grade.getId();
        String name = grade.getName();
        int teacherId = grade.getTeacherId();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TEACHER_ID, teacherId);

        // updating row
        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Grade> retrieveAllGrades() {
        ArrayList<Grade> objects = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM grade";
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Grade grade = new Grade();
                grade.setId(Integer.parseInt(cursor.getString(0)));
                grade.setName(cursor.getString(1));
                grade.setTeacherId(Integer.parseInt(cursor.getString(2)));

                objects.add(grade);
            } while (cursor.moveToNext());
        }

        return objects;
    }

    public String retrieveNameById(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT name FROM grade";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        String name = "";

        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        return name;
    }

    public String retriveIdByTeachId(String teacherId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = String.format("SELECT g.id FROM grade g WHERE g.teacherId = %s", teacherId);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        String id = "";
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        return id;
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

    public void createDefaultRecords() {
        /*Step 1*/
        int gradeQuantity = this.count();
        if (gradeQuantity != 0) return;

        /*Step 2*/
        Grade grade1 = new Grade(1, "D19CQCN01", 1);
        Grade grade2 = new Grade(2, "D19CQCN02", 2);
        Grade grade3 = new Grade(3, "D19CQCN03", 3);
        Grade grade4 = new Grade(4, "D19CQCN04", 4);
        Grade grade5 = new Grade(5, "D19CQCN05", 5);
        Grade grade6 = new Grade(6, "D19CQCN06", 6);
        Grade grade7 = new Grade(7, "D19CQCN07", 7);
        Grade grade8 = new Grade(8, "D19CQCN08", 8);
        Grade grade9 = new Grade(9, "D19CQCN09", 9);

        this.create(grade1);
        this.create(grade2);
        this.create(grade3);
        this.create(grade4);
        this.create(grade5);
        this.create(grade6);
        this.create(grade7);
        this.create(grade8);
        this.create(grade9);
    }

    public void deleteAndCreatTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        createDefaultRecords();
    }
}
