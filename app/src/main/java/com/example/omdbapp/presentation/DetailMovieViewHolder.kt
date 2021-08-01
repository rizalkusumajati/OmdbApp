package com.example.omdbapp.presentation

import android.view.View
import android.widget.TextView
import com.afollestad.recyclical.ViewHolder
import com.example.omdbapp.R
import com.google.android.material.chip.Chip

class DetailMovieViewHolder(view: View) : ViewHolder(view) {
    val chip: Chip = view.findViewById(R.id.chip_genre)
}

class DetailMovieInfoHolder(view: View): ViewHolder(view){
    val tvTitle: TextView = view.findViewById(R.id.tv_info_title)
    val tvDetail : TextView = view.findViewById(R.id.tv_info_detail)
}

data class InfoDetailMovie(
    val title: String,
    val detail: String
)
