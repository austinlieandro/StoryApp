package com.example.storyapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.widget.StoryWidget.Companion.EXTRA_ITEM
import kotlinx.coroutines.runBlocking

internal class StackRemoteViewsFactory(private val context: Context, private val repository: StoryRepository): RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()
    private val listStory = ArrayList<ListStoryItem>()

    override fun onCreate() {}

    override fun onDataSetChanged(): Unit = runBlocking{
            val response = repository.getStory()
            val story = response.listStory
            val image = story.map {
                Glide.with(context)
                    .asBitmap()
                    .load(it.photoUrl)
                    .submit()
                    .get()
            }
            mWidgetItems.clear()
            mWidgetItems.addAll(image)
            listStory.clear()
            listStory.addAll(story)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.ivWidget, mWidgetItems[position])

        val itemId = listStory[position].id

        val extras = Bundle()
        extras.putString(EXTRA_ITEM, itemId)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.ivWidget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = i.toLong()

    override fun hasStableIds(): Boolean = false
}