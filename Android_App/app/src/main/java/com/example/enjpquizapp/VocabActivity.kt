package com.example.enjpquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VocabActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab)

        val segmentID = intent.getIntExtra("segmentID",0)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = SQLiteHelper(this)

        // Call the fetchDataFromDatabase function from MyDataModel
        val typeValue = "V"
        val numberValue = segmentID

        val dataList = dbHelper.getSentenceOrVocab(typeValue,numberValue)

        adapter = TableAdapter(dataList)
        recyclerView.adapter = adapter



        val btnSegment: Button = findViewById(R.id.btnSegment)
        btnSegment.setOnClickListener{
            finish()
        }


    }
}