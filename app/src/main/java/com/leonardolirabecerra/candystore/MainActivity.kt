package com.leonardolirabecerra.candystore

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService


class MainActivity : AppCompatActivity() {
    private var usersRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usersRef = FirebaseDatabase.getInstance().getReference("users")
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            // This is hardcoding, just for testing Android with Firebase. This would be modified.
            val candy = Candy()
            val candyService = CandyService()

            candy.name = "Churritos"
            candy.description = "Tiras de maíz con chile sabor mango."
            candy.price = 15.00
            candy.stock = 10

            candyService.create(candy).addOnSuccessListener {
                Toast.makeText(this, "Candy saved!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error while saving candy, try again.", Toast.LENGTH_LONG).show()
            }

            /*
            val intent = Intent(this, AuthenticationActivity::class.java)

            // To pass data to next activity
            //intent.putExtra("user_id", "etc")

            startActivity(intent)
            */
        }
    }
}
