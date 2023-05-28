package com.example.enjpquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SentenceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence)

        //Set title
        val segmentID = intent.getIntExtra("segmentID",0)
        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNameFromNumber(segmentID)
        val title = segmentID.toString() + "\n" +   data
        val textTitle: TextView = findViewById(R.id.textTitle)
        textTitle.text = title

        // Retrieve the correct data
        val typeValue = "S"
        val dataList = dbHelper.getSentenceOrVocab(typeValue, segmentID)

        //bind the data from an adapter and display it in a recycle view
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TableAdapter(dataList)
        recyclerView.adapter = adapter

        //button to go back to Segment Options
        val btnSegment: Button = findViewById(R.id.btnSegment)
        btnSegment.setOnClickListener{
            finish()
        }
    }
}