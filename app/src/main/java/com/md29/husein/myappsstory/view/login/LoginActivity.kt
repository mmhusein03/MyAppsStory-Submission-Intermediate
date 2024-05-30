package com.md29.husein.myappsstory.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.md29.husein.myappsstory.R
import com.md29.husein.myappsstory.data.Result
import com.md29.husein.myappsstory.data.pref.UserModel
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.databinding.ActivityLoginBinding
import com.md29.husein.myappsstory.utils.ShowLoading
import com.md29.husein.myappsstory.utils.wrapEspressoIdlingResource
import com.md29.husein.myappsstory.view.ViewModelFactory
import com.md29.husein.myappsstory.view.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var showLoading: ShowLoading
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore),this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading = ShowLoading()

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.loginButtonMain.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.loginAcc(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading.showLoading(true, binding.progressBar)
                    }

                    is Result.Success -> {
                        showLoading.showLoading(false, binding.progressBar)
                        val data = result.data
                        val id = data.result.userId
                        val name = data.result.name
                        val token = data.result.token
                        loginViewModel.saveUser(UserModel(token, id, name,true))
                        dialogAlert()
                    }

                    is Result.Error -> {
                        showLoading.showLoading(false, binding.progressBar)
                        toast(result.error)
                    }
                }
            }
        }

        @Suppress("DEPRECATION")
        binding.fabBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dialogAlert() {
        wrapEspressoIdlingResource {
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle(getString(R.string.title_alert_dialog))
                setMessage(getString(R.string.message_alert_dialog_login))
                setPositiveButton(getString(R.string.message_positive_button)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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

        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTv, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEdtLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButtonMain, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}