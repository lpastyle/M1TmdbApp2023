package com.example.m1tmdbapp2023

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonPopularAdapter(val persons: ArrayList<Person>) : RecyclerView.Adapter<PersonPopularAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView
        val knownForTv: TextView
        val popularityTv: TextView

        init {
            nameTv = view.findViewById(R.id.name_tv)
            knownForTv = view.findViewById(R.id.known_for_tv)
            popularityTv = view.findViewById(R.id.popularity_tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = persons.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = persons.get(position)
        holder.nameTv.text = curItem.name
        holder.knownForTv.text = curItem.knownForDepartment
        holder.popularityTv.text = curItem.popularity.toString()
    }

}
