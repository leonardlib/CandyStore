package com.leonardolirabecerra.candystore.views.candy

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.AdapterView
import com.leonardolirabecerra.candystore.R
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
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
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candy_list)
        title = "Lista de dulces"
        val addCandyButton = findViewById<FloatingActionButton>(R.id.addCandyButton)
        candiesListView = findViewById(R.id.candiesList)
        progressDialog = ProgressDialog(this)

        // Get candies
        getCandies()

        addCandyButton.setOnClickListener {
            val intent = Intent(this, NewCandyActivity::class.java)
            startActivityForResult(intent, SAVED_CANDY_CODE)
        }

        candiesListView!!.onItemClickListener = AdapterView.OnItemClickListener {
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long ->

            val intent = Intent(this, EditCandyActivity::class.java)
            intent.putExtra("candy_uuid", candiesList[position].uuid)
            startActivityForResult(intent, SAVED_CANDY_CODE)
        }

        candiesListView!!.onItemLongClickListener = AdapterView.OnItemLongClickListener {
                parent,
                view,
                position,
                id ->

            val builder = AlertDialog.Builder(this)
            builder.setTitle("¿Estás seguro de eliminar el dulce?")
            builder.setMessage("Una vez eliminado no se podrá recuperar.")

            builder.setPositiveButton("Sí, estoy seguro") { dialog, which ->
                dialog.dismiss()
                progressDialog!!.setTitle("Eliminando...")
                progressDialog!!.show()

                candyService.delete(candiesList[position].uuid).addOnSuccessListener {
                    progressDialog!!.dismiss()
                    Toast.makeText(this, "Se ha eliminado el dulce", Toast.LENGTH_LONG).show()
                    getCandies()
                }.addOnFailureListener {
                    progressDialog!!.dismiss()
                    Toast.makeText(this, "No se ha podido eliminar el dulce", Toast.LENGTH_LONG).show()
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            return@OnItemLongClickListener true
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
