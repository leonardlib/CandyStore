package com.leonardolirabecerra.candystore.views.candy

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService
import com.leonardolirabecerra.candystore.services.Utils
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class EditCandyActivity : AppCompatActivity() {
    private val candyService: CandyService = CandyService()
    private var uuid: String? = null
    private var candy: Candy? = null
    private var nameInput: EditText? = null
    private var descriptionInput: EditText? = null
    private var priceInput: EditText? = null
    private var stockInput: EditText? = null
    private var imageView: ImageView? = null
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_candy)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        progressDialog = ProgressDialog(this)
        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        priceInput = findViewById(R.id.priceInput)
        stockInput = findViewById(R.id.stockInput)
        imageView = findViewById(R.id.imageView)

        title = "Editar dulce"
        uuid = intent.getStringExtra("candy_uuid")
        getCandy()

        saveButton.setOnClickListener {
            progressDialog!!.setTitle("Guardando...")
            progressDialog!!.show()

            if (checkFillData()) {
                uploadImage()
            } else {
                Toast.makeText(this, "Completa la información", Toast.LENGTH_LONG).show()
            }
        }

        selectImageButton.setOnClickListener {
            chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
            Intent.createChooser(intent, "Seleccionar imagen"),
            PICK_IMAGE_REQUEST
        )
    }

    private fun uploadImage() {
        if (filePath != null) {
            val utilsService = Utils()
            val path = "images/candies/${UUID.randomUUID()}"

            utilsService.uploadFile(filePath!!, path).addOnSuccessListener {uri ->
                filePath = uri
                saveEditedCandy()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            saveEditedCandy()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (
            requestCode == PICK_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {
            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun fillCandyData() {
        nameInput!!.setText(candy!!.name)
        descriptionInput!!.setText(candy!!.description)
        priceInput!!.setText(candy!!.price.toString())
        stockInput!!.setText(candy!!.stock.toString())
        Picasso.get().load(candy!!.image).placeholder(R.mipmap.ic_launcher).into(imageView)
    }

    private fun getCandy() {
        val candyListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                candy = dataSnapshot.getValue(Candy::class.java)
                fillCandyData()
            }

            override fun onCancelled(dataError: DatabaseError) {
                println("showCandyError: ${dataError.toException()}")
            }
        }

        candyService.show(uuid!!).addListenerForSingleValueEvent(candyListener)
    }

    private fun saveEditedCandy() {
        val candyService = CandyService()

        candy!!.name = nameInput!!.text.toString()
        candy!!.description = descriptionInput!!.text.toString()
        candy!!.price = priceInput!!.text.toString().toDouble()
        candy!!.stock = stockInput!!.text.toString().toInt()

        if (filePath != null) {
            candy!!.image = filePath!!.toString()
        }

        candyService.update(candy!!).addOnSuccessListener {
            progressDialog!!.dismiss()
            Toast.makeText(this, "Se ha guardado el dulce", Toast.LENGTH_SHORT).show()

            setResult(Activity.RESULT_OK)
            finish()
        }.addOnFailureListener {
            progressDialog!!.dismiss()
            Toast.makeText(this, "Error al guardar el dulce, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }
}
