package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.Cache
import com.doublex.selfmanagementhelper.DefinedActivity
import org.json.JSONObject

internal class DefinedActivityViews(private val _cache: Cache, private val _views: LinearLayout) {

    @UiThread
    fun add(newActivityView: NewActivityView, context: Context, callbacks: DefinedActivity) {
        _cache.saveActivity(newActivityView.name(), newActivityView.details())
        newActivityView.clear()
        update(context, callbacks)
    }
    @UiThread
    fun delete(activityName: String, context: Context, callbacks: DefinedActivity) {
        _cache.deleteActivity(activityName)
        update(context, callbacks)
    }
    @UiThread
    fun save(
        activityName: String,
        activityDetails: JSONObject,
        context: Context,
        callbacks: DefinedActivity
    ) {
        _cache.saveActivity(activityName, activityDetails)
        update(context, callbacks)
    }
    @UiThread
    fun update(context: Context, callbacks: DefinedActivity) {
        _views.removeAllViews()
        val activities = _cache.activities()
        for (activityName: String in activities.keys()) {
            add(activityName, activities.getJSONObject(activityName), context, callbacks)
        }
    }

    @UiThread
    private fun add(
        activityName: String,
        activityDetails: JSONObject,
        context: Context,
        callbacks: DefinedActivity
    ) {
        val view = DefinedActivityView(activityDetails, activityName, context, callbacks).view()
        _views.addView(view)
    }

}