package com.mansidev.diceroller

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mansidev.diceroller.databinding.ActivityMainBinding
import com.segment.analytics.Analytics
import com.segment.analytics.Properties
import java.util.*

class MasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val randomDiceGenrater = Random()
    private var diceCount: String? = null
    private var isSwtich: String? = null
    private var privateMode = 0
    private val myPreferences = "MyPREFERENCES"
    private var mLastClickTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Analytics.with(applicationContext)
            .track("Master Activty Started", Properties().putValue("Masterscreen", true))
        val sharedPreferences = getSharedPreferences(myPreferences, privateMode)
        isSwtich = sharedPreferences.getString("Swtich", "")
        diceCount = sharedPreferences.getString("Count", "")

        binding.rollButton.isClickable = true
        binding.rollButton.setOnClickListener(View.OnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            Analytics.with(applicationContext).track(
                "Dice Roller Clicked",
                Properties().putValue("Masterscreen", true).putValue("Clicked", true)
            )

            if (isSwtich.toString() == "On" || isSwtich.toString()
                    .equals(null) || isSwtich.toString() == "null" || isSwtich.toString() == ""
            ) {
                val mp: MediaPlayer = MediaPlayer.create(this, R.raw.play_dice)
                mp.setVolume(100F, 100F)
                mp.start()
            } else if (isSwtich.toString() == "Off") {
                val mp: MediaPlayer = MediaPlayer.create(this, R.raw.play_dice)
                mp.setVolume(0F, 0F)
                mp.start()
            }

            val blink = AnimationUtils.loadAnimation(applicationContext, R.anim.blink)
            binding.lLView01.animation = blink
            binding.lLView01.startAnimation(blink)
            binding.lLView02.animation = blink
            binding.lLView02.startAnimation(blink)
            binding.lLView03.animation = blink
            binding.lLView03.startAnimation(blink)
            binding.lLView04.animation = blink
            binding.lLView04.startAnimation(blink)
            diceRoller(binding.imageView01)
            diceRoller(binding.imageView02)
            diceRoller(binding.imageView03)
            diceRoller(binding.imageView04)
        })

        when {
            diceCount.toString() == "1 Dice" -> {
                binding.llLayout2.visibility = View.GONE
                binding.lLView01.visibility = View.VISIBLE
                binding.lLView02.visibility = View.GONE
            }
            diceCount.toString() == "2 Dice" -> {
                binding.llLayout2.visibility = View.GONE
                binding.lLView01.visibility = View.VISIBLE
                binding.lLView02.visibility = View.VISIBLE
            }
            diceCount.toString() == "3 Dice" -> {
                binding.lLView01.visibility = View.VISIBLE
                binding.lLView02.visibility = View.VISIBLE
                binding.llLayout2.visibility = View.VISIBLE
                binding.lLView03.visibility = View.VISIBLE
                binding.lLView04.visibility = View.GONE
            }
            diceCount.toString() == "4 Dice" -> {
                binding.lLView01.visibility = View.VISIBLE
                binding.lLView02.visibility = View.VISIBLE
                binding.llLayout2.visibility = View.VISIBLE
                binding.lLView03.visibility = View.VISIBLE
                binding.lLView04.visibility = View.VISIBLE
            }
        }
    }

    private fun diceRoller(imgView: ImageView) {
        val rdg: Int = randomDiceGenrater.nextInt(6) + 1
        Log.d("Checking", "" + rdg)
        when (rdg) {
            1 -> imgView.setImageResource(R.drawable.dice01)
            2 -> imgView.setImageResource(R.drawable.dice02)
            3 -> imgView.setImageResource(R.drawable.dice03)
            4 -> imgView.setImageResource(R.drawable.dice04)
            5 -> imgView.setImageResource(R.drawable.dice05)
            6 -> imgView.setImageResource(R.drawable.dice06)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra("comeFromSetting", diceCount.toString())
                intent.putExtra("comeFromSwitch", isSwtich.toString())
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}