package com.eflm.practica2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eflm.practica2.R
import com.eflm.practica2.databinding.ActivityMainBinding
import com.eflm.practica2.ui.Fragments.AlimentListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AlimentListFragment())
                .commit()
        }
    }
}