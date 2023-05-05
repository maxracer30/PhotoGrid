package ru.maxstelmakh.photogrid.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.maxstelmakh.photogrid.R
import ru.maxstelmakh.photogrid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}