package com.example.enjpquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Button to end the application
        val btnExit: Button = findViewById(R.id.btnExit)
        btnExit.setOnClickListener{
            exitProcess(1)
        }

        //Open the database and collect each Segment Name and Number
        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getSegNumberNameData()

        //Get Names and Numbers formatted into a buttons
        createButtonsFromDataBase(data)

        dbHelper.close()
    }

    //Create a button for each Name and Number
    private fun createButtonsFromDataBase(data: String) {
        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)

        val values = data.split("\n") // Split the data into individual values
        for (value in values) {

            //remove last empty button
            if (value.isEmpty()){
                break
            }
            val button = Button(this)
            button.text = value

            //set button id
            val generatedId = View.generateViewId()
            button.id = generatedId

            //set button text size
            val textSizePx = resources.getDimensionPixelSize(R.dimen.button_text_size)
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSizePx.toFloat())

            //Move to a new Activity when a button is clicked
            button.setOnClickListener{
                val segmentID = button.id
                val intent = Intent(this,SegmentActivity::class.java)

                //pass the button Segment Number
                intent.putExtra("segmentID",segmentID)

                startActivity(intent)
            }
            // Add the buttons to the button container
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0,0,0,16)

            buttonContainer.addView(button, layoutParams)
        }
    }
}