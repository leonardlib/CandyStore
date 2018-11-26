package com.leonardolirabecerra.candystore.views.candy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService

class EditCandyActivity : AppCompatActivity() {
    private val candyService: CandyService = CandyService()
    private var uuid: String? = null
    private var candy: Candy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_candy)

        uuid = intent.getStringExtra("candy_uuid")
        getCandy()
    }

    private fun getCandy() {
        val context = this

        val candyListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                candy = dataSnapshot.getValue(Candy::class.java)
                Toast.makeText(context, candy!!.name, Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(dataError: DatabaseError) {
                println("showCandyError: ${dataError.toException()}")
            }
        }

        candyService.show(uuid!!).addListenerForSingleValueEvent(candyListener)
    }
}
