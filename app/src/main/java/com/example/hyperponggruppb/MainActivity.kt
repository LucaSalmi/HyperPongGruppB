package com.example.hyperponggruppb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityMainBinding
import android.os.Build


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PointManager.player

        supportFragmentManager.commit {
            add(R.id.fragment_container, PointFragment())
        }

        binding.pointsBtn.setOnClickListener {
            PointManager.addPoints(10)
            supportFragmentManager.commit {
                replace(R.id.fragment_container, PointFragment())
            }
        }


    }
}