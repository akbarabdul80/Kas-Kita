package com.ugm.kaskita.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.oratakashi.viewbinding.core.binding.activity.viewBinding
import com.oratakashi.viewbinding.core.tools.startActivity
import com.ugm.kaskita.R
import com.ugm.kaskita.databinding.ActivitySplashBinding
import com.ugm.kaskita.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val appSpalsh: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.app_splash)
    }

    private val bottomToTop: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.bottom_totop)
    }

    private val binding: ActivitySplashBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appLogo.startAnimation(appSpalsh)
        binding.tvNameApp.startAnimation(bottomToTop)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(MainActivity::class.java)
                finish()
            }, 2000L
        )
    }
}