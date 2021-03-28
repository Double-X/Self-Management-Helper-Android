package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.Cache
import com.doublex.selfmanagementhelper.NotificationBuilder
import com.doublex.selfmanagementhelper.views.ActivityLogView.Companion.now
import org.json.JSONObject

internal class ActivityLogs(
    private val _cache: Cache,
    private val _notification: NotificationBuilder,
    private val _views: LinearLayout
) {

    @UiThread
    fun update(context: Context) {
        _views.removeAllViews()
        val activityLogs = _cache.activityLogs()
        for (activityLogName: String in activityLogs.keys()) {
            val activityLogsWithName = activityLogs.getJSONObject(activityLogName)
            for (startTime: String in activityLogsWithName.keys()) {
                val details = activityLogsWithName.getJSONObject(startTime)
                add(context, activityLogName, startTime, details)
            }
        }
    }
    @UiThread
    fun start(context: Context, name: String, details: JSONObject) {
        val startTime = now()
        _cache.saveActivityLog(name, startTime, details)
        add(context, name, startTime.toString(), details)
    }

    @UiThread
    private fun add(context: Context, name: String, startTime: String, details: JSONObject) {
        val activityLogView = ActivityLogView(name, startTime, details, context, {
            delete(context, it)
        }) { end(it) }
        _views.addView(activityLogView.view())
    }
    @UiThread
    private fun delete(context: Context, activityLogView: ActivityLogView) {
        _cache.deleteActivityLog(activityLogView.name(), activityLogView.startTime())
        update(context)
    }
    @UiThread
    private fun end(activityLogView: ActivityLogView) {
        _cache.saveActivityLog(
            activityLogView.name(),
            activityLogView.startTime(),
            activityLogView.details()
        )
        _notification.show(activityLogView.endNotification())
    }

}