package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.Cache
import com.doublex.selfmanagementhelper.DefinedActivity
import org.json.JSONObject
import java.util.*

internal class DefinedActivityViews(private val _cache: Cache, private val _views: LinearLayout) {

    private val _activities = Vector<DefinedActivityView>()

    @UiThread
    fun add(activity: NewActivityView, context: Context, callbacks: DefinedActivity) {
        val activityName = activity.name()
        val activityDetails = activity.details()
        _cache.saveActivity(activityName, activityDetails)
        add(activityName, activityDetails, context, callbacks)
        activity.clear()
    }
    @UiThread
    fun delete(activity: DefinedActivityView) {
        _cache.deleteActivity(activity.name())
        _activities.remove(activity)
        _views.removeView(activity.view())
    }
    @UiThread
    fun saveAs(name: String, details: JSONObject, context: Context, callbacks: DefinedActivity) {
        _cache.saveActivity(name, details)
        add(name, details, context, callbacks)
    }
    @UiThread
    fun redrawTexts() = _activities.forEach { it.redrawTexts() }
    @UiThread
    fun update(context: Context, callbacks: DefinedActivity) {
        _activities.clear()
        _views.removeAllViews()
        val activities = _cache.activities()
        for (name: String in activities.keys()) {
            add(name, activities.getJSONObject(name), context, callbacks)
        }
    }

    @UiThread
    private fun add(
        name: String,
        details: JSONObject,
        context: Context,
        callbacks: DefinedActivity
    ) {
        val activityView = DefinedActivityView(details, name, context, callbacks)
        _activities.add(activityView)
        _views.addView(activityView.view())
    }

}