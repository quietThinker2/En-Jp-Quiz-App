package com.example.enjpquizapp

import android.annotation.SuppressLint
import android.app.Dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SentenceQuizActivity : AppCompatActivity(), OnPopupDismissListener {

    private val values: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence_quiz)

        //Set Activity Title
        val segmentID = intent.getIntExtra("segmentID",0)
        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNameFromNumber(segmentID)
        val title = segmentID.toString() + "\n" +   data
        val textTitle: TextView = findViewById(R.id.textTitle)
        textTitle.text = title

        //collect random 1 row from current and all previous segments
        val values = dbHelper.extractVocabOrSentence("S",segmentID)

        //first round translation prompt set to english
        val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
        toTranslateTextView.text = values[1]

        //Button to back to the Segment Options Page
        val btnSegment: Button = findViewById(R.id.btnSegment)
        btnSegment.setOnClickListener{
            finish()
        }

        //Button to check user text and display results on a PopUp
        val editText: EditText = findViewById(R.id.editText)
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

    //store the new values when popup is dismissed
    override fun onDismiss(updatedList : ArrayList<String>){
        values.clear()
        values.addAll(updatedList)
    }

    @SuppressLint("SetTextI18n")
    private fun showPopUp(userText: String, segmentID: Int, values: ArrayList<String>, listener: OnPopupDismissListener){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_layout)

        //popup background color
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)

        //Set answer values
        val textViewJapanese: TextView = dialog.findViewById(R.id.textViewJapanese)
        textViewJapanese.text = values[0]
        val textViewEnglish: TextView = dialog.findViewById(R.id.textViewEnglish)
        textViewEnglish.text = values[1]
        val textViewRomaji: TextView = dialog.findViewById(R.id.textViewRomaji)
        textViewRomaji.text = values[2]

        //check user input and give result
        val textViewResults: TextView = dialog.findViewById(R.id.textViewResults)
        val userText2 = userText.lowercase()
        if ((userText2.trim() == values[0].lowercase()) or (userText2.trim() == values[1].lowercase())){
            textViewResults.text = "Correct!"
        } else {
            textViewResults.text = "Wrong!"
        }

        //when the popup is dismissed update values and text
        dialog.setOnDismissListener {
            val dbHelper = SQLiteHelper(this)
            val updatedList = dbHelper.extractVocabOrSentence("S", segmentID)
            //randomly switch language translation
            val rnds = (0..1).random()
            //set english
            if (rnds == 0) {
                val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
                toTranslateTextView.text = updatedList[1]
            //set japanese
            } else {
                val toTranslateTextView: TextView = findViewById(R.id.toTranslateTextView)
                toTranslateTextView.text = updatedList[0]
            }
            //clear the user input
            val editText = findViewById<EditText>(R.id.editText)
            editText.setText(" ")
            //get new values
            values.clear()
            values.addAll(updatedList)
            listener.onDismiss(updatedList)
        }
        dialog.show()
    }
}