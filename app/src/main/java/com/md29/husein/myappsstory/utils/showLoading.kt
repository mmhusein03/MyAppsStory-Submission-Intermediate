package com.md29.husein.myappsstory.utils

import android.view.View

class ShowLoading {
    fun showLoading(isLoading: Boolean, progressBar: View) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}