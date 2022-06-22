package com.mansidev.diceroller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mansidev.diceroller.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first)

        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@FirstActivity, MasterActivity::class.java)
            startActivity(i)
            finish()
        }, 2 * 800.toLong())
    }
}
