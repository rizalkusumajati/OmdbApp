package com.example.omdbapp.domain

import android.content.Context
import android.widget.Toast


sealed class UIState<out T> {
    var hasBeenConsumed: Boolean = false
        protected set

    data class SuccessFromRemote<out T>(val data: T) : UIState<T>() {
        fun getData(isConsumed: Boolean = true) = data.also {
            if (isConsumed) hasBeenConsumed = true
        }
    }

    data class Error(val errorResponse: String? = null) : UIState<Nothing>() {
        fun showToast(context: Context) {
            Toast.makeText(context, errorResponse ?: "", Toast.LENGTH_SHORT).show()

        }

        fun consumed() {
            hasBeenConsumed = true
        }
    }

    object Empty : UIState<Nothing>() {}
    object Loading : UIState<Nothing>()
}
