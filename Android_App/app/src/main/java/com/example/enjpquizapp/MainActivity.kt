package com.example.enjpquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnExit: Button = findViewById(R.id.btnExit)
        btnExit.setOnClickListener{
            //finish()
            exitProcess(1)
        }

        val dbHelper = SQLiteHelper(this)
        val data = dbHelper.getAData()

        createButtonsFromDataBase(data)

        val textView: TextView = findViewById(R.id.textViewTest)
        textView.text = data

        dbHelper.close()

    }

    fun createButtonsFromDataBase(data: String) {
        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)

        val values = data.split("\n") // Split the data into individual values
        for (value in values) {

            //remove last empty button
            if (value.isEmpty()){
                break
            }
            val button = Button(this)
            button.text = value

            // Set any other properties or listeners for the button as needed

            //set button id
            val generatedId = View.generateViewId()
            button.id = generatedId


            //set button text size
            val textSizePx = resources.getDimensionPixelSize(R.dimen.button_text_size)
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSizePx.toFloat())


            //action on clicked
            button.setOnClickListener{
                val clickedButtonId = it.id
                println("Clicked Button ID: $clickedButtonId")
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