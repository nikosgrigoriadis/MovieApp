package com.example.movieapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDatabase(
    context: Context?,
    //name: String?,
    //factory: SQLiteDatabase.CursorFactory?,
    //version: Int
) : SQLiteOpenHelper(context, "localmovie.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTablestatment = "CREATE TABLE MOVIE_TABLE(ID INTEGER PRIMARY KEY AUTOINCREMENT , COVER INTEGER)";
        db?.execSQL(createTablestatment);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addOne(moviecover: MovieCover) {

    }
}
