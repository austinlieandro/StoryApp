package com.example.storyapp.view.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.factory.StoryModelFactory

class DetailStoryActivity : AppCompatActivity() {
    val viewModel by viewModels<DetailViewModel> {
        StoryModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)

        if (id != null) {
            viewModel.getDetailStory(id)
            viewModel.detailStory.observe(this){story ->
                Glide.with(binding.imgDetailStory)
                    .load(story.photoUrl)
                    .into(binding.imgDetailStory)
                binding.tvDetailName.text = story.name
                binding.tvDetailDescription.text = story.description
            }
        }

        viewModel.status.observe(this){isSucess ->
            if (isSucess){
                Toast.makeText(this, "Detail Story berhasil dimuat", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.error.observe(this){errorMessage ->
                    if (errorMessage != null){
                        Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}