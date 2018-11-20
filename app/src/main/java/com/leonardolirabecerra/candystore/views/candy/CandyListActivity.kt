package com.leonardolirabecerra.candystore.views.candy

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.leonardolirabecerra.candystore.R

class CandyListActivity : AppCompatActivity() {
    private val SAVED_CANDY_CODE: Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candy_list)
        title = "Lista de dulces"

        val addCandyButton = findViewById<FloatingActionButton>(R.id.addCandyButton)

        addCandyButton.setOnClickListener {
            val intent = Intent(this, NewCandyActivity::class.java)
            startActivityForResult(intent, SAVED_CANDY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SAVED_CANDY_CODE && resultCode == Activity.RESULT_OK) {
            //TODO: Verificar que aparezca el dulce en la lista
        }
    }
}
