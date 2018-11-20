package com.leonardolirabecerra.candystore.services

import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.lang.Exception


class Utils {
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    init {
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
    }

    fun uploadFile(filePath: Uri, firebasePath: String): Task<Uri> {
        val ref = storageReference!!.child(firebasePath)
        val task = ref.putFile(filePath)

        while (!task.isComplete) {}

        return ref.downloadUrl
    }
}