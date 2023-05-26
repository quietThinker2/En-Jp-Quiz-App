package com.example.enjpquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SegmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segment)

        val segmentID = intent.getIntExtra("segmentID",0)
        val segmentTitle = intent.getStringExtra("segmentTitle")

        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNameFromNumber(segmentID)

        val testTitle = segmentID.toString() + "\n" +   data



        val textTitle: TextView = findViewById(R.id.textTitle)
        textTitle.text = testTitle

        val btnMain: Button = findViewById(R.id.btnMain)
        btnMain.setOnClickListener{
            finish()
            //exitProcess(1)
        }


        val btnVocab: Button = findViewById(R.id.btnVocab)
        btnVocab.setOnClickListener{
            val intent = Intent(this,VocabActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            //intent.putExtra("segmentTitle",segmentTitle)

            startActivity(intent)
        }
        val btnSent: Button = findViewById(R.id.btnSent)
        btnSent.setOnClickListener{
            finish()
            //exitProcess(1)
        }
        val btnVocabQuiz: Button = findViewById(R.id.btnVocabQuiz)
        btnVocabQuiz.setOnClickListener{
            finish()
            //exitProcess(1)
        }
        val btnSentQuiz: Button = findViewById(R.id.btnSentQuiz)
        btnSentQuiz.setOnClickListener{
            finish()
            //exitProcess(1)
        }
        val btnAllQuiz: Button = findViewById(R.id.btnAllQuiz)
        btnAllQuiz.setOnClickListener{
            finish()
            //exitProcess(1)
        }
    }
}