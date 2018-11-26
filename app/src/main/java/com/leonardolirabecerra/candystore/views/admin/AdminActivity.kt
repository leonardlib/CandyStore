package com.leonardolirabecerra.candystore.views.admin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        candiesButton.setOnClickListener {
            val intent = Intent(this, CandyListActivity::class.java)
            startActivity(intent)
        }

        scheduleButton.setOnClickListener {
            Toast.makeText(this, "Aún no funciona este módulo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        val loginButton = menu.findItem(R.id.menu_1)
        val priceButton = menu.findItem(R.id.menu_3)
        loginButton.isVisible = false
        priceButton.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_2 -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
