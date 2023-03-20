package com.example.m1tmdbapp2023

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.m1tmdbapp2023.ApiClient.Companion.IMAGE_BASE_URL
import com.squareup.picasso.Picasso
import android.content.Context
import android.graphics.Color
import com.example.m1tmdbapp2023.databinding.PersonItemBinding

class PersonPopularAdapter(private val persons: ArrayList<Person>, context: Context) : RecyclerView.Adapter<PersonPopularAdapter.PersonItemViewHolder>(){

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class PersonItemViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var maxPopularity:Double = 0.0
    private val scoreRatings: Array<String> = context.resources.getStringArray(R.array.score_rating)
    private val ratingColors: Array<String> = context.resources.getStringArray(R.array.rating_colors)


    init {
        setMaxPopularity()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonItemViewHolder {
        val binding = PersonItemBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return PersonItemViewHolder(binding)
    }

    override fun getItemCount() = persons.size

    override fun onBindViewHolder(holder: PersonItemViewHolder, position: Int) {
        val curItem = persons.get(position)
        holder.binding.nameTv.text = curItem.name
        holder.binding.knownForTv.text = curItem.knownForDepartment
        holder.binding.popularityTv.text = curItem.popularity.toString()
        Picasso.get()
            .load(IMAGE_BASE_URL + curItem.profilePath)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.binding.photoIv)

        holder.binding.scoreGaugeView.updateScore(
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

    /* class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
    }*/
}
