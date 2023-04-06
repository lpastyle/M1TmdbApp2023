package com.example.m1tmdbapp2023

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.m1tmdbapp2023.databinding.FragmentSocialBarBinding

class SocialBarFragment : Fragment() {

    companion object {
        fun newInstance() = SocialBarFragment()
    }
    private val  LOGTAG = SocialBarFragment::class.simpleName
    private lateinit var binding : FragmentSocialBarBinding
    private var cn : Int? = Color.LTGRAY
    private var cs : Int? = Color.RED

    // Using the activityViewModels() Kotlin property delegate from the
    // fragment-ktx artifact to retrieve the ViewModel in the activity scope
    private val viewModel by activityViewModels<SocialBarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSocialBarBinding.inflate(inflater)
        val view = binding.root
        view.tag = requireArguments().getString("sbfc_view_tag")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cn = getContext()?.getColor(R.color.ic_social_normal)
        cs = getContext()?.getColor(R.color.ic_social_selected)

        Log.d(LOGTAG,"personId=${view.tag}")

        view.tag?.let {
            val mapkey = it.toString().toInt()
            Log.d(LOGTAG,"mk=$mapkey")

            // set like button
            val nblikes = viewModel.nbLikes.getOrElse(mapkey, {0})
            binding.nbLikeTv.text =nblikes.toString()
            val likeColor =  if (nblikes > 0 ) cs!! else cn!!
            binding.likeIv.setColorFilter(likeColor)
            binding.nbLikeTv.setTextColor(likeColor)

            binding.likeIv.setOnClickListener {
                viewModel.nbLikes.set(mapkey,viewModel.nbLikes.getOrElse(mapkey, {0}) + 1)
                binding.nbLikeTv.setText(viewModel.nbLikes[mapkey].toString())
                binding.likeIv.setColorFilter(cs!!)
                binding.nbLikeTv.setTextColor(cs!!)
            }

            // set favorite button
            val isFavorite = viewModel.isFavorite.getOrElse(mapkey,{false})
            binding.favoriteIv.setColorFilter(if (isFavorite) cs!! else cn!!)
            binding.favoriteIv.setOnClickListener {
                viewModel.isFavorite.set(mapkey, !viewModel.isFavorite.getOrElse(mapkey,{false}))
                binding.favoriteIv.setColorFilter(if (viewModel.isFavorite[mapkey] == true) cs!! else cn!! )
            }

            // set share button
            binding.shareIv.setColorFilter(cn!!)
            binding.shareIv.setOnClickListener {
                Log.d(LOGTAG,"shared clicked for id=${mapkey}")
            }
        }

    }

   /*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SocialBarViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}