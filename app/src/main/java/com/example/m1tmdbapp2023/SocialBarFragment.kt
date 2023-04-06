package com.example.m1tmdbapp2023

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SocialBarFragment : Fragment() {

    companion object {
        fun newInstance() = SocialBarFragment()
    }

    private lateinit var viewModel: SocialBarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_social_bar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SocialBarViewModel::class.java)
        // TODO: Use the ViewModel
    }

}