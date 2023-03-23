package com.example.m1tmdbapp2023

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
    private var isNotifPermGranted = false // TODO: replace with SHARED PREFERENCE
    private lateinit var binding: ActivityMainBinding
    private lateinit var personPopularAdapter: PersonPopularAdapter
    private var persons = arrayListOf<Person>()
    private var totalResults = 0
    private var totalPages = Int.MAX_VALUE
    private var curPage = 1

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            isNotifPermGranted = isGranted
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                Log.i(LOGTAG,"Permission was granted")
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.i(LOGTAG, "Permission was denied" )
            }
        }

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
        check4NotificationPermission()
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
                    if (isNotifPermGranted && curPage ==1) {
                        createPopularPersonNotification(applicationContext, response.body()!!.results[0])
                    }
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

    private fun check4NotificationPermission():Any = when {
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED -> {
            Log.i(LOGTAG, "notification permission already granted")
            isNotifPermGranted = true
        }
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            Log.i(LOGTAG, "show a permission rationale explanation dialog")
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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

    fun createPopularPersonNotification(context : Context, p : Person) {
        // Create notification
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(androidx.appcompat.R.drawable.btn_radio_off_mtrl)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_content, p.name.toString(), p.popularity))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // TODO generate unique ID
        notificationManager.notify(2023, notification)

    }

}