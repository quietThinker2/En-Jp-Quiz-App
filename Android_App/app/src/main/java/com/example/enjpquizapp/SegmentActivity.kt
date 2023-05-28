package com.example.enjpquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class SegmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segment)

        //set up Segment Name and Number Title
        val segmentID = intent.getIntExtra("segmentID",0)
        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNameFromNumber(segmentID)
        val title = segmentID.toString() + "\n" +   data
        val textTitle: TextView = findViewById(R.id.textTitle)
        textTitle.text = title

        //Button to go back
        val btnMain: Button = findViewById(R.id.btnMain)
        btnMain.setOnClickListener{
            finish()
        }
        //Button to go to Vocab Activity
        val btnVocab: Button = findViewById(R.id.btnVocab)
        btnVocab.setOnClickListener{
            val intent = Intent(this,VocabActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            startActivity(intent)
        }
        //Button to go to Sentence Activity
        val btnSent: Button = findViewById(R.id.btnSent)
        btnSent.setOnClickListener{
            val intent = Intent(this,SentenceActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            startActivity(intent)
        }
        //Button to go to Vocab Quiz Activity
        val btnVocabQuiz: Button = findViewById(R.id.btnVocabQuiz)
        btnVocabQuiz.setOnClickListener{
            val intent = Intent(this,VocabQuizActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            startActivity(intent)
        }
        //Button to go to Sentence Quiz Activity
        val btnSentQuiz: Button = findViewById(R.id.btnSentQuiz)
        btnSentQuiz.setOnClickListener{
            val intent = Intent(this,SentenceQuizActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            startActivity(intent)
        }
        //Button to go to All Quiz Activity
        val btnAllQuiz: Button = findViewById(R.id.btnAllQuiz)
        btnAllQuiz.setOnClickListener{
            val intent = Intent(this,AllQuizActivity::class.java)
            intent.putExtra("segmentID",segmentID)
            startActivity(intent)
        }

        //Hide any Options that don't fit the segment
        if ((segmentID == 1) or (segmentID == 2)){
            btnSent.visibility = View.GONE
            btnSentQuiz.visibility = View.GONE
        }
        if (segmentID == 1){
            btnAllQuiz.visibility = View.GONE
        }
    }
}