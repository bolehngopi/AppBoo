package com.bolehngopi.pplgmobile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "studentDatabase.db"
        const val DATABASE_VERSION = 2
        const val TABLE_STUDENTS = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_HOBBIES = "hobbies"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_HOBBIES TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add the 'name' column if it's missing
            db?.execSQL("ALTER TABLE $TABLE_STUDENTS ADD COLUMN $COLUMN_NAME TEXT")
        }
        // Any other schema changes can go here
        onCreate(db)
    }


    // Update student data
    fun updateStudent(id: Int, name: String, gender: String, hobbies: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("gender", gender)
            put("hobbies", hobbies)
        }
        db.update(TABLE_STUDENTS, values, "id = ?", arrayOf(id.toString()))
    }

    // Delete student data
    fun deleteStudent(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_STUDENTS, "id = ?", arrayOf(id.toString()))
    }
}

