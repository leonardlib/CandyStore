package com.leonardolirabecerra.candystore.views.candy

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.models.Candy
import com.leonardolirabecerra.candystore.services.CandyService
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import com.leonardolirabecerra.candystore.services.Utils
import java.lang.Exception
import java.util.*


class NewCandyActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_new_candy)
        title = "Nuevo dulce"

        val saveButton = findViewById<Button>(R.id.saveButton)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        progressDialog = ProgressDialog(this)
        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        priceInput = findViewById(R.id.priceInput)
        stockInput = findViewById(R.id.stockInput)
        imageView = findViewById(R.id.imageView)

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

            utilsService.uploadFile(filePath!!, path)!!.addOnSuccessListener {uri ->
                filePath = uri
                saveNewCandy()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
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

    private fun saveNewCandy() {
        val candy = Candy()
        val candyService = CandyService()

        candy.name = nameInput!!.text.toString()
        candy.description = descriptionInput!!.text.toString()
        candy.price = priceInput!!.text.toString().toDouble()
        candy.stock = stockInput!!.text.toString().toInt()
        candy.image = filePath!!.toString()


        candyService.create(candy).addOnSuccessListener {
            progressDialog!!.dismiss()
            Toast.makeText(this, "Se ha guardado el dulce", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            progressDialog!!.dismiss()
            Toast.makeText(this, "Error al guardar el dulce, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }
}
