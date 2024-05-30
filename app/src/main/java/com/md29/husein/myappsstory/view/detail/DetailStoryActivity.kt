package com.md29.husein.myappsstory.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.md29.husein.myappsstory.data.networking.ListStories
import com.md29.husein.myappsstory.databinding.ActivityDetailStoryBinding
import com.md29.husein.myappsstory.utils.DateFormatting
import com.md29.husein.myappsstory.utils.loadImage
import java.util.TimeZone

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setDetailStory()
        setAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @SuppressLint("NewApi")
    private fun setDetailStory() {
        val detailStory = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DETAIL_STORY, ListStories::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(DETAIL_STORY)
        }

        binding.apply {
            tvName.text = detailStory?.name
            tvDescription.text = detailStory?.description
            tvCreateAt.text = detailStory?.createAt?.let {
                DateFormatting.formatDate(
                    it,
                    TimeZone.getDefault().id
                )
            }
            imageStories.loadImage(detailStory?.photoUrl)
        }
    }

    @Suppress("DEPRECATION")
    private fun setAction() {
        binding.apply {
            fabBack.setOnClickListener {
                onBackPressed()
            }
            settingImageView.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}