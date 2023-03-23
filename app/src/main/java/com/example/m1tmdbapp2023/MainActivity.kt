package com.example.m1tmdbapp2023

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.m1tmdbapp2023.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val NOTIFICATION_CHANNEL_ID = "popular_person_notification_channel_id"

class MainActivity : AppCompatActivity() {

    val LOGTAG = MainActivity::class.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var personPopularAdapter: PersonPopularAdapter
    private var persons = arrayListOf<Person>()
    private var totalResults = 0
    private var totalPages = Int.MAX_VALUE
    private var curPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        // Init recycler view
        binding.popularPersonRv.setHasFixedSize(true)
        binding.popularPersonRv.layoutManager = LinearLayoutManager(this)
        personPopularAdapter = PersonPopularAdapter(persons, this)
        binding.popularPersonRv.adapter = personPopularAdapter
        binding.popularPersonRv.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    if (curPage < totalPages) {
                        curPage++
                        loadPage(curPage)
                    }
                }
            }
        })

        loadPage(curPage)

    }

    private fun loadPage(page: Int) {
        val tmdbapi = ApiClient.instance.create(ITmdbApi::class.java)

        val call = tmdbapi.getPopularPerson(TMDB_API_KEY, page)
        binding.progressWheel.visibility = VISIBLE
        call.enqueue(object : Callback<PersonPopularResponse> {
            override fun onResponse(
                call: Call<PersonPopularResponse>,
                response: Response<PersonPopularResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(applicationContext, "Page $page loaded", Toast.LENGTH_SHORT).show()
                    persons.addAll(response.body()?.results!!)
                    totalResults = response.body()!!.totalResults!!
                    totalPages = response.body()!!.totalPages!!
                    personPopularAdapter.notifyDataSetChanged()
                    personPopularAdapter.setMaxPopularity()
                    binding.totalResultsTv.text = getString(R.string.total_results_text,persons.size, totalResults)
                } else {
                    Log.e(LOGTAG, "Call to getPopularPerson failed with error ${response.code()}")
                }
                binding.progressWheel.visibility = GONE
            }

            override fun onFailure(call: Call<PersonPopularResponse>, t: Throwable) {
                Log.e(LOGTAG,"Call to getPopularPerson failed")
                binding.progressWheel.visibility = GONE
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}