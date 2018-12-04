package com.leonardolirabecerra.candystore.views.pricerange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.adapters.CandyAdapter
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService
import java.util.ArrayList

class PriceRangeActivity : AppCompatActivity() {
    private val candyService: CandyService = CandyService()
    private var candiesListView: ListView? = null
    private var candiesList = ArrayList<Candy>()
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_range)
        title = "Rango de precio"

        candiesListView = findViewById(R.id.candiesList)
        editText = findViewById(R.id.priceEditInput)

        editText!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                getCandies(s.toString())
            }
        })
    }

    /**
     * Get filter candies by price
     * @author Leonardo Lira Becerra
     * @author Alyson Joselyn
     * @date 26/11/2018
     */
    private fun getCandies(price: String) {
        val context = this
        var newPrice = price

        if (newPrice.isEmpty()) {
            newPrice = "0"
        }

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

        candyService.filterByPrice(newPrice.toDouble()).addListenerForSingleValueEvent(candiesListener)
    }
}
