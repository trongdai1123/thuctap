package com.example.mthshop.datalocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class SQLite extends SQLiteOpenHelper {
    private static String NAME_TABLE = "Notification";
    private static String NAME_ID = "id";
    private static String NAME_CONTENT = "content";
    public SQLite(@Nullable Context context) {
        super(context, "MTH-APP", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + NAME_TABLE + " (" + NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_CONTENT + " TEXT)";
        sqLiteDatabase.execSQL(sql);

        sql = "INSERT INTO " + NAME_TABLE + " VALUES(NULL, 'Oder thành công sản phẩm')";
        sqLiteDatabase.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public static List<String> getAllNotification(Context context) {
        List<String> listContent = new ArrayList<>();
        SQLite sqLite = new SQLite(context);
        SQLiteDatabase db = sqLite.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME_TABLE, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                int content = cursor.getColumnIndex(NAME_CONTENT);
                listContent.add(cursor.getString(content));
            }while (cursor.moveToNext());
        }
        return listContent;
    }

    public static boolean addNotification(Context context, String value) {
        List<String> listContent = new ArrayList<>();
        SQLite sqLite = new SQLite(context);
        SQLiteDatabase db = sqLite.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_CONTENT, value);
        return db.insert(NAME_TABLE, null, contentValues) > 0;
    }
}
