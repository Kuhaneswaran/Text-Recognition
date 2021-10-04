package com.kuhan.textrecognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kuhan.textrecognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStatic.setOnClickListener {
            binding.llButtons.isVisible = false
            openFragment(StaticTextRecognitionFragment())
        }

        binding.btnLive.setOnClickListener {
            binding.llButtons.isVisible = false
            openFragment(LiveTextRecognitionFragment())
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
        binding.llButtons.isVisible = true
    }
}