package com.example.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.storyapp.di.Injection

class StackWidgetService : RemoteViewsService() {
    private val storyRepository by lazy { Injection.provideStoryRepository(this.applicationContext) }
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext, storyRepository)
}