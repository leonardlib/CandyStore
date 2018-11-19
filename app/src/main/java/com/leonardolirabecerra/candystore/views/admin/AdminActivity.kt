package com.leonardolirabecerra.candystore.views.admin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.views.candy.CandyListActivity
import com.firebase.ui.auth.AuthUI
import com.leonardolirabecerra.candystore.MainActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        title = "Panel de administrador"

        val candiesButton = findViewById<ImageView>(R.id.candiesButton)
        val scheduleButton = findViewById<ImageView>(R.id.scheduleButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        candiesButton.setOnClickListener {
            val intent = Intent(this, CandyListActivity::class.java)
            startActivity(intent)
        }

        scheduleButton.setOnClickListener {
            Toast.makeText(this, "Aún no funciona este módulo", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
        }
    }
}
