package com.example.m1tmdbapp2023

import com.google.gson.annotations.SerializedName


data class Person (

  @SerializedName("adult"                ) var adult              : Boolean?            = null,
  @SerializedName("gender"               ) var gender             : Int?                = null,
  @SerializedName("id"                   ) var id                 : Int?                = null,
  @SerializedName("known_for"            ) var knownFor           : ArrayList<KnownFor> = arrayListOf(),
  @SerializedName("known_for_department" ) var knownForDepartment : String?             = null,
  @SerializedName("name"                 ) var name               : String?             = null,
  @SerializedName("popularity"           ) var popularity         : Double?             = null,
  @SerializedName("profile_path"         ) var profilePath        : String?             = null

)