package com.doublex.selfmanagementhelper.widgets

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.Cache
import com.doublex.selfmanagementhelper.NotificationBuilder
import com.doublex.selfmanagementhelper.widgets.ActivityLogView.Companion.now
import org.json.JSONObject
import java.util.*

internal class ActivityLogViews(
    private val _cache: Cache,
    private val _notification: NotificationBuilder,
    private val _views: LinearLayout
) {

    private val _logs = Vector<ActivityLogView>()

    @UiThread
    fun update(context: Context) {
        _logs.clear()
        _views.removeAllViews()
        val logs = _cache.activityLogs()
        for (name: String in logs.keys()) {
            val logsWithName = logs.getJSONObject(name)
            for (startTime: String in logsWithName.keys()) {
                add(context, name, startTime, logsWithName.getJSONObject(startTime))
            }
        }
    }
    @UiThread
    fun redrawTexts() = _logs.forEach { it.redrawTexts() }
    @UiThread
    fun start(context: Context, name: String, details: JSONObject) {
        val time = now()
        _cache.saveActivityLog(name, time, details)
        add(context, name, time.toString(), details)
    }

    @UiThread
    private fun add(context: Context, name: String, startTime: String, details: JSONObject) {
        val log = ActivityLogView(context, name, startTime, details, { delete(it) }) { end(it) }
        _logs.add(log)
        _views.addView(log.view())
    }
    @UiThread
    private fun delete(log: ActivityLogView) {
        _cache.deleteActivityLog(log.name(), log.startTime())
        _logs.remove(log)
        _views.removeView(log.view())
    }
    @UiThread
    private fun end(log: ActivityLogView) {
        _cache.saveActivityLog(log.name(), log.startTime(), log.details())
        _notification.show(log.endNotification())
    }

}