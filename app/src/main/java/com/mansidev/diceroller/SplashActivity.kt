package com.mansidev.diceroller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mansidev.diceroller.databinding.ActivitySplashBinding
import com.segment.analytics.Analytics
import com.segment.analytics.Properties

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
// Create an analytics client with the given context and Segment write key.
        val analytics = Analytics.Builder(
            applicationContext,
            "FWzqAbkrHEeJSc3VSbpEK2fJQwD1QHYT"
        )
            .trackApplicationLifecycleEvents()
            .recordScreenViews()
            .build()
        Analytics.setSingletonInstance(analytics)
        Analytics.with(applicationContext)
            .track("Splash Activity Started", Properties().putValue("Splashscreen", true))
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@SplashActivity, MasterActivity::class.java)
            startActivity(i)
            finish()
        }, 2 * 800.toLong())
    }
}
