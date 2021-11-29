package com.ugm.kaskita.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.oratakashi.viewbinding.core.tools.startActivity
import com.ugm.kaskita.R
import com.ugm.kaskita.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(MainActivity::class.java)
                finish()
            }, 2000L
        )
    }
}