package com.md29.husein.myappsstory.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.md29.husein.myappsstory.R
import com.md29.husein.myappsstory.data.adapter.LoadingStateAdapter
import com.md29.husein.myappsstory.data.adapter.StoriesAdapter
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.databinding.ActivityMainBinding
import com.md29.husein.myappsstory.view.ViewModelFactory
import com.md29.husein.myappsstory.view.detail.DetailStoryActivity
import com.md29.husein.myappsstory.view.maps.MapsActivity
import com.md29.husein.myappsstory.view.story.AddStoryActivity
import com.md29.husein.myappsstory.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getStories()
        setAction()
        setupView()
    }

    private fun getStories() {
        binding.listStories.apply {
            layoutManager =
                if (application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(this@MainActivity, 2)
                } else {
                    LinearLayoutManager(this@MainActivity)
                }
        }

        lifecycleScope.launch {
            mainViewModel.getUser().collect {
                if (it.isLogin) {
                    binding.tvWelcome.text = getString(R.string.welcome_messages, it.name)
                    val adapter = StoriesAdapter { listStories ->
                        val imgItemPhoto = findViewById<ImageView>(R.id.image_stories)
                        val tvName = findViewById<TextView>(R.id.tv_name)
                        val tvCreateAt = findViewById<TextView>(R.id.tv_createAt)
                        val optionCompact: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@MainActivity,
                                Pair(imgItemPhoto, "photo"),
                                Pair(tvName, "name"),
                                Pair(tvCreateAt, "time_create")
                            )

                        val moveDetailStories =
                            Intent(this@MainActivity, DetailStoryActivity::class.java)
                        moveDetailStories.putExtra(DetailStoryActivity.DETAIL_STORY, listStories)

                        startActivity(moveDetailStories, optionCompact.toBundle())
                    }

                    binding.listStories.adapter = adapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            adapter.retry()
                        }
                    )

                    mainViewModel.getStories().observe(this@MainActivity) { result ->
                        adapter.submitData(lifecycle, result)
                    }

                } else {
                    startWelcome()
                }
            }
        }
    }

    private fun setAction() {
        binding.apply {
            fabLogout.setOnClickListener {
                mainViewModel.logout()
            }

            settingImageView.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            swipeRefresh.setOnRefreshListener {
                getStories()
                swipeRefresh.isRefreshing = false
            }

            fabAddStories.setOnClickListener {
                val moveAddStories = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(moveAddStories)
            }

            fbMaps.setOnClickListener {
                val moveViewMaps = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(moveViewMaps)
            }
        }
    }

    private fun startWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
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

    override fun onResume() {
        super.onResume()
        mainViewModel.getStories()
    }
}