package com.leonardolirabecerra.candystore.views.candy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService

class NewCandyActivity : AppCompatActivity() {
    private var nameInput: EditText? = null
    private var descriptionInput: EditText? = null
    private var priceInput: EditText? = null
    private var stockInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_candy)
        title = "Nuevo dulce"

        val saveButton = findViewById<Button>(R.id.saveButton)
        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        priceInput = findViewById(R.id.priceInput)
        stockInput = findViewById(R.id.stockInput)

        saveButton.setOnClickListener {
            if (checkFillData()) {
                Log.i("tag", "Si pasó")
                saveNewCandy()
            } else {
                Toast.makeText(this, "Completa la información", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkFillData(): Boolean {
        return (
            nameInput!!.text.toString().trim().isNotEmpty()
            && descriptionInput!!.text.toString().trim().isNotEmpty()
            && priceInput!!.text.toString().trim().isNotEmpty()
            && stockInput!!.text.toString().trim().isNotEmpty()
        )
    }

    private fun saveNewCandy() {
        val candy = Candy()
        val candyService = CandyService()

        candy.name = nameInput!!.text.toString()
        candy.description = descriptionInput!!.text.toString()
        candy.price = priceInput!!.text.toString().toDouble()
        candy.stock = stockInput!!.text.toString().toInt()

        candyService.create(candy).addOnSuccessListener {
            Toast.makeText(this, "Se ha guardado el dulce", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al guardar el dulce, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }
}
