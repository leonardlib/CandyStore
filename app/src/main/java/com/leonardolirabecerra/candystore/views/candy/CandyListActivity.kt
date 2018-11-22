package com.leonardolirabecerra.candystore.views.candy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import com.leonardolirabecerra.candystore.R
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.adapters.CandyAdapter
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService


class CandyListActivity : AppCompatActivity() {
    private val SAVED_CANDY_CODE: Int = 200
    private val candyService: CandyService = CandyService()
    private var candiesListView: ListView? = null
    private var candiesList = ArrayList<Candy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candy_list)
        title = "Lista de dulces"
        val addCandyButton = findViewById<FloatingActionButton>(R.id.addCandyButton)
        candiesListView = findViewById(R.id.candiesList)

        // Get candies
        getCandies()

        addCandyButton.setOnClickListener {
            val intent = Intent(this, NewCandyActivity::class.java)
            startActivityForResult(intent, SAVED_CANDY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SAVED_CANDY_CODE && resultCode == Activity.RESULT_OK) {
            getCandies()
        }
    }

    private fun getCandies() {
        val context = this

        val candiesListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                candiesList.clear()
                dataSnapshot.children.mapNotNullTo(candiesList) {
                    it.getValue<Candy>(Candy::class.java)
                }
                val adapter = CandyAdapter(context, candiesList)
                candiesListView!!.adapter = adapter
            }

            override fun onCancelled(dataError: DatabaseError) {
                println("loadingCandiesError: ${dataError.toException()}")
            }
        }

        candyService.index().addListenerForSingleValueEvent(candiesListener)
    }
}
