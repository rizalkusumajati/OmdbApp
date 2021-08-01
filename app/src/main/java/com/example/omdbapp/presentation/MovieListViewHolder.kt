package com.example.omdbapp.presentation

import android.view.View
import android.widget.ImageView
import com.afollestad.recyclical.ViewHolder
import com.example.omdbapp.R

class MovieListViewHolder(view: View) : ViewHolder(view) {
    val poster: ImageView = view.findViewById(R.id.iv_poster)
}
