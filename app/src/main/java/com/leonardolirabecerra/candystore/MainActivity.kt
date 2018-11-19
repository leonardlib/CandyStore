package com.leonardolirabecerra.candystore

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leonardolirabecerra.candystore.views.admin.AdminActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private val providers: List<AuthUI.IdpConfig> = Arrays.asList(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    private val RC_SIGN_IN: Int = 200
    private var user: FirebaseUser? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Todos los dulces"

        loginButton = findViewById(R.id.loginButton)

        checkIfUserIsAdmin()

        loginButton!!.setOnClickListener {
            openLoginActivity()
        }
    }

    private fun checkIfUserIsAdmin(): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null) {
            user = FirebaseAuth.getInstance().currentUser as FirebaseUser

            if (user!!.email == "twofacedmirror34@gmail.com") {
                val intent = Intent(this, AdminActivity::class.java)
                loginButton!!.visibility = View.INVISIBLE
                startActivity(intent)

                return true
            } else {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        loginButton!!.visibility = View.VISIBLE
                    }

                return false
            }
        } else {
            return false
        }
    }

    /**
     * Function to open login view
     * @author Leonardo Lira Becerra
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

    /**
     * Function to receive the login view result
     * @author Leonardo Lira Becerra
     * @date 19/11/2018
     */
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
}
