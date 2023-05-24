package com.example.enjpquizapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SQLiteHelper(private val context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "EN-JB-DB.db"
        private const val DATABASE_VERSION = 1
    }

    private val dbPath: String = context.getDatabasePath(DATABASE_NAME).path

    init {
        copyDatabaseFromAssets(context)
    }

    //load database
    private fun copyDatabaseFromAssets(context: Context) {
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            try {
                val inputStream = context.assets.open(DATABASE_NAME)
                val outputStream = FileOutputStream(dbPath)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Database creation logic if needed
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Database upgrade logic if needed
    }

    fun getSegNumberNameData(): String {
        val db = readableDatabase
        val projection = arrayOf("segmentNumber", "segmentName")
        val cursor = db.query("DATA", projection, null, null, null, null, null)

        val stringBuilder = StringBuilder()
        while (cursor.moveToNext()) {
            val segmentNumber = cursor.getString(cursor.getColumnIndexOrThrow("segmentNumber"))
            val segmentName = cursor.getString(cursor.getColumnIndexOrThrow("segmentName"))

            val concatValue = "$segmentNumber - $segmentName"
            if (segmentNumber.isNotEmpty()) {
                if (!stringBuilder.contains(concatValue)) {

                    stringBuilder.append(concatValue).append("\n")
                }
            }


        }

        cursor.close()
        return stringBuilder.toString()
    }

    @SuppressLint("Range")
    fun getSegNameFromNumber(number: Int): String {
        val columnName = "segmentName"
        val tableName = "DATA"
        val columnNumber = "segmentNumber"
        val desiredNumber = number


        val db = readableDatabase
        val cursor = db.query(
            tableName,
            arrayOf(columnName),
            "$columnNumber = ?",
            arrayOf(desiredNumber.toString()),
            null,
            null,
            null
        )
        var retrievedName = ""
        if (cursor.moveToFirst()) {
            retrievedName = cursor.getString(cursor.getColumnIndex(columnName))

        }

        return retrievedName


    }

}
