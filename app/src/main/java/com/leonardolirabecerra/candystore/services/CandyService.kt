package com.leonardolirabecerra.candystore.services

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leonardolirabecerra.candystore.models.Candy

class CandyService {
    private var candyRef: DatabaseReference? = null

    init {
        candyRef = FirebaseDatabase.getInstance().getReference("candy")
    }

    fun index(): DatabaseReference {
        return candyRef!!
    }

    /**
     * Function to save new candy
     * @author Leonardo Lira Becerra
     * @date 13/11/2018
     */
    fun create(candy: Candy): Task<Void> {
        return candyRef!!.push().setValue(candy)
    }
}