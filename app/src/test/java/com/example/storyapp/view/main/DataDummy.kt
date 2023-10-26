package com.example.storyapp.view.main

import com.example.storyapp.api.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryItem>{
        val story: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10){
            val stories = ListStoryItem(
                "https://fox59.com/wp-content/uploads/sites/21/2022/05/JOHN-DOE-CLAY.jpg",
                "16-desember-2002",
                "Jhon Doe",
                "Testing",
                11.0000000f.toDouble(),
                i.toString(),
                -11.0000000f.toDouble(),
            )
            story.add(stories)
        }
        return story
    }
}