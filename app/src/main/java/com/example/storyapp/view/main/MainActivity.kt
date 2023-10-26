package com.example.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.factory.StoryModelFactory
import com.example.storyapp.view.addstory.AddStoryActivity
import com.example.storyapp.view.maps.MapsActivity
import com.example.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel> {
        StoryModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager


        getData()

        binding.fabMain.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun getData() {
        val adapter = MainAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val userPreference = UserPreference.getInstance(dataStore)
        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    userPreference.logout()
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                    finish()
                }
                return true
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}