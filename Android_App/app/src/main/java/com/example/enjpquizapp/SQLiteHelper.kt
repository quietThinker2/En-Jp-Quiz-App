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

    //Setup - load database
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    //Return a Formatted String of each Segment Number and Name
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
        cursor?.close()
        db.close()
        return stringBuilder.toString()
    }

    //Get the Segment Name from the Given Number
    @SuppressLint("Range")
    fun getSegNameFromNumber(number: Int): String {
        val columnName = "segmentName"
        val tableName = "DATA"
        val columnNumber = "segmentNumber"

        val db = readableDatabase
        val cursor = db.query(
            tableName,
            arrayOf(columnName),
            "$columnNumber = ?",
            arrayOf(number.toString()),
            null,
            null,
            null
        )
        var retrievedName = ""
        if (cursor.moveToFirst()) {
            retrievedName = cursor.getString(cursor.getColumnIndex(columnName))

        }
        cursor?.close()
        db.close()
        return retrievedName
    }

    //Get a list of the wanted Vocab or Sentences
    @SuppressLint("Range")
    fun getSentenceOrVocab(typeValue: String, segmentNumberValue: Int): List<TableItem> {
        val db = readableDatabase

        val projection = arrayOf("japanese", "english", "romaji")
        val selection = "(type IS NULL OR type = ?) AND segmentNumber = ?"
        val selectionArgs = if ((segmentNumberValue == 1) or (segmentNumberValue == 2)){
            arrayOf("",segmentNumberValue.toString())
        } else {
            arrayOf(typeValue,segmentNumberValue.toString())

        }
        val cursor = db.query("DATA", projection, selection, selectionArgs, null, null, null)

        val items = mutableListOf<TableItem>()

        while (cursor.moveToNext()) {
            val c1 = cursor.getString(cursor.getColumnIndexOrThrow("japanese"))
            val c2 = cursor.getString(cursor.getColumnIndexOrThrow("english"))
            var c3 = cursor.getString(cursor.getColumnIndexOrThrow("romaji"))
            if (c3 == null) {
                c3 = ""
            }
            val item = TableItem(c1, c2, c3)
            items.add(item)
        }
        cursor.close()
        db.close()

        return items
    }


    //Get a Random Segment Sentence or Vocab -> extract the values -> and return them in a ArrayList
    @SuppressLint("Range")
    fun extractVocabOrSentence(typeValue: String, segmentNumberValue: Int): ArrayList<String> {
        val db = readableDatabase

        val selection = "(type IS NULL OR type = ?) AND segmentNumber = ?"
        val selectionArgs = if ((segmentNumberValue == 1) or (segmentNumberValue == 2)) {
            arrayOf("", segmentNumberValue.toString())
        } else {
            arrayOf(typeValue, segmentNumberValue.toString())
        }
        val orderBy = "RANDOM()"
        val limit = "1"

        val cursor = db.query(
            "DATA",
            null,
            selection,
            selectionArgs,
            null,
            null,
            orderBy,
            limit
        )
        val values = arrayListOf<String>()
        if (cursor.moveToFirst()) {
            val english = cursor.getString(cursor.getColumnIndex("english"))
            val japanese = cursor.getString(cursor.getColumnIndex("japanese"))
            var romaji = cursor.getString(cursor.getColumnIndex("romaji"))
            if (romaji == null) {
                romaji = ""
            }
            values.add(japanese)
            values.add(english)
            values.add(romaji)
        }
        cursor?.close()
        db.close()
        return values
    }

    //Get a random row from all the Segments up to the given Segment Number
    @SuppressLint("Range")
    fun extractAllPrevious(segmentNumberValue: Int): ArrayList<String> {
        val db = readableDatabase

        val projection = arrayOf("japanese", "english", "romaji") // Replace with your actual column names
        val selection = "segmentNumber <= ?"
        val selectionArgs = arrayOf(segmentNumberValue.toString())
        val sortOrder = "RANDOM()"
        val limit = "1"

        val cursor = db.query("DATA", projection, selection, selectionArgs, null, null, sortOrder, limit)

        val values = arrayListOf<String>()
// Retrieve the data from the cursor if a row is found
        if (cursor != null && cursor.moveToFirst()) {
            val japanese = cursor.getString(cursor.getColumnIndex("japanese"))
            val english = cursor.getString(cursor.getColumnIndex("english"))
            var romaji = cursor.getString(cursor.getColumnIndex("romaji"))

            if (romaji == null) {
                romaji = ""
            }
            values.add(japanese)
            values.add(english)
            values.add(romaji)
        }
        cursor?.close()
        db.close()
        return values
    }
}

