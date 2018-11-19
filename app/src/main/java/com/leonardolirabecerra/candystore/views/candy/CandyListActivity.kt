package com.leonardolirabecerra.candystore.views.candy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.leonardolirabecerra.candystore.R

class CandyListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candy_list)
        title = "Lista de dulces"

        val addCandyButton = findViewById<FloatingActionButton>(R.id.addCandyButton)

        addCandyButton.setOnClickListener {
            val intent = Intent(this, NewCandyActivity::class.java)
            startActivity(intent)
        }
    }
}
