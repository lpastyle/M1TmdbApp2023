package com.example.m1tmdbapp2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m1tmdbapp2023.databinding.ActivityPersonDetailBinding

const val PERSON_ID_EXTRA_KEY = "person_id_ek"
class PersonDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = intent.extras?.getString(PERSON_ID_EXTRA_KEY)


    }
}