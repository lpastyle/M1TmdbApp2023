package com.example.m1tmdbapp2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m1tmdbapp2023.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val LOGTAG = MainActivity::class.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var personPopularAdapter: PersonPopularAdapter
    private var persons = arrayListOf<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init recycler view
        binding.popularPersonRv.setHasFixedSize(true)
        binding.popularPersonRv.layoutManager = LinearLayoutManager(this)
        personPopularAdapter = PersonPopularAdapter()
        binding.popularPersonRv.adapter = personPopularAdapter

        loadPage(1)

    }

    private fun loadPage(page: Int) {
        val tmdbapi = ApiClient.instance.create(ITmdbApi::class.java)

        val call = tmdbapi.getPopularPerson(TMDB_API_KEY, page)

        call.enqueue(object : Callback<PersonPopularResponse> {
            override fun onResponse(
                call: Call<PersonPopularResponse>,
                response: Response<PersonPopularResponse>
            ) {
                if (response.isSuccessful) {
                    persons.addAll(response.body()?.results!!)
                } else {
                    Log.e(LOGTAG, "Call to getPopularPerson failed with error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PersonPopularResponse>, t: Throwable) {
                Log.e(LOGTAG,"Call to getPopularPerson failed")
            }
        })
    }
}