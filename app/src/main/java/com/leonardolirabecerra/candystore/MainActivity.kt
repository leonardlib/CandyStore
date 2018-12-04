package com.leonardolirabecerra.candystore

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.adapters.CandyAdapter
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService
import com.leonardolirabecerra.candystore.views.admin.AdminActivity
import com.leonardolirabecerra.candystore.views.pricerange.PriceRangeActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private val providers: List<AuthUI.IdpConfig> = Arrays.asList(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    private val RC_SIGN_IN: Int = 200
    private var user: FirebaseUser? = null
    private val candyService: CandyService = CandyService()
    private var candiesListView: ListView? = null
    private var candiesList = ArrayList<Candy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        candiesListView = findViewById(R.id.candiesList)
        val actionBar = supportActionBar

        actionBar!!.title = "Todos los dulces"

        checkIfUserIsAdmin()

        // Get candies
        getCandies()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        val logoutButton = menu.findItem(R.id.menu_2)
        logoutButton.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_1 -> {
                openLoginActivity()
                return true
            }
            R.id.menu_3 -> {
                val intent = Intent(this, PriceRangeActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Check if there's an admin user logged
     * @author Leonardo Lira Becerra
     * @author Alyson Joselyn
     * @date 26/11/2018
     */
    private fun checkIfUserIsAdmin(): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null) {
            user = FirebaseAuth.getInstance().currentUser as FirebaseUser

            if (user!!.email == "candy.store.mobile@gmail.com") {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                return true
            } else {
                AuthUI.getInstance().signOut(this)
                return false
            }
        } else {
            return false
        }
    }

    /**
     * Function to open login view
     * @author Leonardo Lira Becerra
     * @author Alyson Joselyn
     * @date 19/11/2018
     */
    private fun openLoginActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                if (!checkIfUserIsAdmin()) {
                    Toast.makeText(this, "No eres administrador", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Get a list of candies from database
     * @author Leonardo Lira Becerra
     * @author Alyson Joselyn
     * @date 26/11/2018
     */
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
