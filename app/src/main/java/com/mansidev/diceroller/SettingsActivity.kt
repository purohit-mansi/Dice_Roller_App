package com.mansidev.diceroller

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mansidev.diceroller.databinding.SettingsLayoutBinding
import com.mansidev.diceroller.models.DiceModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsLayoutBinding
    private var diceCount: String? = null
    private var isSwtich: String? = null
    private var privateMode = 0
    private val myPreferences = "MyPREFERENCES"
    private var diceAdapter: DicesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.settings_layout)
        binding.tvVersion.text = "Version " + BuildConfig.VERSION_NAME

        if (intent.extras != null) {
            if (intent.getStringExtra("comeFromSetting") != null) {
                diceCount = intent.getStringExtra("comeFromSetting")
            }
            if (intent.getStringExtra("comeFromSwitch") != null) {
                isSwtich = intent.getStringExtra("comeFromSwitch")
            }
        }

        if (diceCount.toString()
                .equals(null) || diceCount.toString() == "null" || diceCount.toString() == ""
        ) {
            binding.tvDiceCount.text = getString(R.string.one_dice)
        } else {
            binding.tvDiceCount.text = diceCount
        }

        if (isSwtich.equals("On") || isSwtich.equals("") || isSwtich.equals(
                null
            ) || isSwtich.equals(
                "null"
            )
        ) {
            binding.cbSound.isChecked = true
        } else if (isSwtich.equals("Off")) {
            binding.cbSound.isChecked = false
        }

        val sharedPreferences = getSharedPreferences(myPreferences, privateMode)
        binding.cbSound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isSwtich = getString(R.string.switch_on)
                Toast.makeText(applicationContext, "Sound : $isSwtich", Toast.LENGTH_SHORT).show()
            } else {
                isSwtich = getString(R.string.switch_off)
                Toast.makeText(applicationContext, "Sound : $isSwtich", Toast.LENGTH_SHORT).show()
            }
            sharedPreferences.edit().apply {
                putString("Swtich", isSwtich)
            }.apply()
        }
        binding.tvRateApp.setOnClickListener {
            val appPackageName = packageName // package name of the app
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dice_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val recyclerItems: RecyclerView = dialog.findViewById(R.id.recyclerItems)
        val diceModel = ArrayList<DiceModel>()
        diceModel.add(DiceModel("1 Dice"))
        diceModel.add(DiceModel("2 Dice"))
        diceModel.add(DiceModel("3 Dice"))
        diceModel.add(DiceModel("4 Dice"))

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerItems.layoutManager = layoutManager
        recyclerItems.itemAnimator = DefaultItemAnimator()
        diceAdapter = DicesAdapter(diceModel, dialog, binding.tvDiceCount)
        recyclerItems.adapter = diceAdapter

        binding.llDices.setOnClickListener {
            dialog.show()
        }
        dialog.dismiss()

        binding.llBack.setOnClickListener {
            val intent = Intent(this, MasterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private inner class DicesAdapter(
        var diceModel: ArrayList<DiceModel>, val dialog: Dialog,
        var tvDiceCount: TextView/*, val editor: SharedPreferences.Editor*/
    ) : RecyclerView.Adapter<DicesAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.dice_menu, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val diceModel = diceModel[position]
            holder.tvDiceCounts.text = diceModel.diceCount
            holder.tvDiceCounts.setOnClickListener {
                tvDiceCount.text = diceModel.diceCount
                val sharedPreferences = getSharedPreferences(myPreferences, privateMode)
                sharedPreferences.edit().apply {
                    putString("Count", diceModel.diceCount)
                }.apply()
                dialog.dismiss()
            }
        }

        override fun getItemCount(): Int {
            return diceModel.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var tvDiceCounts: TextView = itemView.findViewById(R.id.tvDiceCounts)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MasterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
