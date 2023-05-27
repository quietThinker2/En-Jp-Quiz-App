package com.example.enjpquizapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.Locale

class SentenceQuizActivity : AppCompatActivity(), OnPopupDismissListener {

    private val values: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence_quiz)

        val segmentID = intent.getIntExtra("segmentID",0)

        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNameFromNumber(segmentID)

        val title = segmentID.toString() + "\n" +   data

        val textTitle: TextView = findViewById(R.id.textTitle)
        textTitle.text = title

        val values = dbHelper.extractVocabOrSentence("S",segmentID)


        val editText: EditText = findViewById(R.id.editText)


        val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
        toTranslateTextView.setText(values[1])



        val btnSegment: Button = findViewById(R.id.btnSegment)
        btnSegment.setOnClickListener{
            finish()
            //exitProcess(1)
        }
        val btnCheckText: Button = findViewById(R.id.btnCheckText)
        btnCheckText.setOnClickListener{
            val userText = editText.text.toString()
            showPopUp(userText,segmentID,values,this)



        }

        //trigger the Check Answer button by pressing the Enter key on the keyboard
        editText.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                btnCheckText.performClick()
                return@setOnKeyListener true
            }
            false


        }

    }

    override fun onDismiss(updatedList : ArrayList<String>){
        values.clear()
        values.addAll(updatedList)


    }

    @SuppressLint("SetTextI18n")
    private fun showPopUp(userText: String, segmentID: Int, values: ArrayList<String>, listener: OnPopupDismissListener){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_layout)

        dialog.window?.setBackgroundDrawableResource(android.R.color.white)


//
//        println(values[0])



        val textViewJapanese: TextView = dialog.findViewById(R.id.textViewJapanese)
        textViewJapanese.text = values[0]
        val textViewEnglish: TextView = dialog.findViewById(R.id.textViewEnglish)
        textViewEnglish.text = values[1]
        val textViewRomaji: TextView = dialog.findViewById(R.id.textViewRomaji)
        textViewRomaji.text = values[2]

        val textViewResults: TextView = dialog.findViewById(R.id.textViewResults)
        val userText2 = userText.lowercase()
        if ((userText2.trim() == values[0].lowercase()) or (userText2.trim() == values[1].lowercase())){
            textViewResults.text = "Correct!"
        } else {
            textViewResults.text = "Wrong!"
        }


        dialog.setOnDismissListener {
            val dbHelper = SQLiteHelper(this)
            val updatedList = dbHelper.extractVocabOrSentence("S", segmentID)
            //set translation based off of random 1 or 0
            val rnds = (0..1).random()
            //if 1 -> set english check for japanese
            if (rnds == 0) {
                val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
                toTranslateTextView.text = updatedList[1]

            } else {
                val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
                toTranslateTextView.text = updatedList[0]


            }

            val editText = findViewById<EditText>(R.id.editText)
            editText.setText(" ")

            values.clear()
            values.addAll(updatedList)
            listener.onDismiss(updatedList)


        }
        dialog.show()
    }
}