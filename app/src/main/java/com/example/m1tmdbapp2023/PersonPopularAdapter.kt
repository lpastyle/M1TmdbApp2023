package com.example.m1tmdbapp2023

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.m1tmdbapp2023.ApiClient.Companion.IMAGE_BASE_URL
import com.squareup.picasso.Picasso
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.example.m1tmdbapp2023.databinding.PersonItemBinding

class PersonPopularAdapter(private val persons: ArrayList<Person>, private val appCompatActivity: AppCompatActivity) : RecyclerView.Adapter<PersonPopularAdapter.PersonItemViewHolder>(){
    private val LOGTAG = PersonPopularAdapter::class.simpleName
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class PersonItemViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.socialBarFcv.id = View.generateViewId()
        }
    }

    private var maxPopularity:Double = 0.0
    private val scoreRatings: Array<String> = appCompatActivity.resources.getStringArray(R.array.score_rating)
    private val ratingColors: Array<String> = appCompatActivity.resources.getStringArray(R.array.rating_colors)


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

        // set social bar fragment container view tag with unique person id
        holder.binding.socialBarFcv.tag = curItem.id.toString()

    }

    override fun onViewAttachedToWindow(holder: PersonItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        val sbfcv = holder.binding.socialBarFcv
        val bundle = bundleOf(
            "sbfc_view_tag" to sbfcv.tag
        )
        appCompatActivity.supportFragmentManager.commitNow {
            //add(holder.binding.socialBarFcv.id, SocialBarFragment())
            add(sbfcv.id, SocialBarFragment::class.java, bundle )
        }

    }

    override fun onViewDetachedFromWindow(holder: PersonItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        appCompatActivity.supportFragmentManager.findFragmentById(holder.binding.socialBarFcv.id)?.let {
            appCompatActivity.supportFragmentManager.commitNow {
                remove(it)
            }
        }


    }


    fun setMaxPopularity() {
        maxPopularity = 0.0
        for (p in persons) {
            if (p.popularity != null && p.popularity!! > maxPopularity) {
                maxPopularity = p.popularity!!
            }
        }

        val sharedPref = appCompatActivity.getPreferences(Context.MODE_PRIVATE)
        val highscore = sharedPref.getFloat(appCompatActivity.getString(R.string.saved_high_score_key), 0f)
        if (maxPopularity > highscore) {
            with (sharedPref.edit()) {
                putFloat(appCompatActivity.getString(R.string.saved_high_score_key), maxPopularity.toFloat())
                apply()
            }
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
