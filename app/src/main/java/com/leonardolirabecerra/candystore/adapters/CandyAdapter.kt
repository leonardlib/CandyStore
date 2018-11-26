package com.leonardolirabecerra.candystore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.leonardolirabecerra.candystore.R
import com.leonardolirabecerra.candystore.models.Candy
import com.squareup.picasso.Picasso

class CandyAdapter(
    private val context: Context,
    private val dataSource: ArrayList<Candy>
): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.candy_item, parent, false)

        val titleTextView = rowView.findViewById(R.id.candy_item_title) as TextView
        val subtitleTextView = rowView.findViewById(R.id.candy_item_subtitle) as TextView
        val detailTextView = rowView.findViewById(R.id.candy_item_detail) as TextView
        val thumbnailImageView = rowView.findViewById(R.id.candy_item_thumbnail) as ImageView

        val candy = getItem(position) as Candy
        val description = "${candy.description}\nCantidad: ${candy.stock}"
        val price = "$ ${candy.price}"

        titleTextView.text = candy.name
        subtitleTextView.text = description
        detailTextView.text = price

        Picasso.get().load(candy.image).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }
}