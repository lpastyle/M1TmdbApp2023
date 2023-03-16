package com.example.m1tmdbapp2023

import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.m1tmdbapp2023.ApiClient.Companion.IMAGE_BASE_URL
import com.squareup.picasso.Picasso
import android.content.Context
import android.graphics.Color

class PersonPopularAdapter(val persons: ArrayList<Person>, context: Context) : RecyclerView.Adapter<PersonPopularAdapter.ViewHolder>(){

    private var maxPopularity:Double = 0.0
    private val scoreRatings: Array<String> = context.resources.getStringArray(R.array.score_rating)
    private val ratingColors: Array<String> = context.resources.getStringArray(R.array.rating_colors)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView
        val knownForTv: TextView
        val popularityTv: TextView
        val photoIv: ImageView
        val scoreGaugeView: ScoreGaugeView

        init {
            nameTv = view.findViewById(R.id.name_tv)
            knownForTv = view.findViewById(R.id.known_for_tv)
            popularityTv = view.findViewById(R.id.popularity_tv)
            photoIv = view.findViewById(R.id.photo_iv)
            scoreGaugeView = view.findViewById(R.id.score_gauge_view)
        }
    }
    init {
        setMaxPopularity()
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
        Picasso.get()
            .load(IMAGE_BASE_URL + curItem.profilePath)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.photoIv)

        holder.scoreGaugeView.updateScore(
            getRating(curItem.popularity,maxPopularity),
            getScoreColor(curItem.popularity,maxPopularity),
            curItem.popularity!!.toFloat(),
            maxPopularity.toInt()
        )
    }

    fun setMaxPopularity() {
        maxPopularity = 0.0
        for (p in persons) {
            if (p.popularity != null && p.popularity!! > maxPopularity) maxPopularity = p.popularity!!
        }
    }

    private fun getRating(value:Double?, max:Double):String {
        val i:Int = if (value != null && max > 0.0) ((scoreRatings.size -1) * value/max).toInt() else 0
        return scoreRatings[i]
    }

    private fun getScoreColor(value:Double?, max:Double):Int {
        val i:Int = if (value != null && max > 0.0) ((ratingColors.size -1) * value/max).toInt() else 0
        return try {
            ratingColors[i].toInt()
        } catch (e : Exception) {
            Color.RED
        }
    }
}
