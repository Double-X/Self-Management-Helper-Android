package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import com.doublex.selfmanagementhelper.showConfirmDialog
import com.doublex.selfmanagementhelper.showEditTextDialog
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.DESC
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.END_NOTIFICATION
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.START_NOTIFICATION
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.TARGETED_DURATION
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.TARGETED_END_TIME
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.TARGETED_INTENSITY
import com.doublex.selfmanagementhelper.views.NewActivityView.Companion.TARGETED_START_TIME
import org.json.JSONObject
import java.util.*


internal class ActivityLogView(
    private val _name: String,
    private val _startTime: String,
    private val _details: JSONObject,
    context: Context,
    deleteCallback: (ActivityLogView) -> Unit,
    endCallback: (ActivityLogView) -> Unit
) {

    companion object {
        fun now() = Calendar.getInstance().time.time
        private const val ACTUAL_END_TIME = "Actual End Time"
        private const val ACTUAL_DURATION = "Actual Duration"
        private const val ACTUAL_INTENSITY = "Actual Intensity"
    }

    private val _view = View.inflate(context, R.layout.widget_activity_log, null)
    private val _deleteButton = view<Button>(R.id.button_delete_activity_log)
    private val _endButton = view<Button>(R.id.button_end_activity)
    private val _actualEndTime = textView(R.id.text_activity_actual_end_time)
    private val _actualIntensity = textView(R.id.text_activity_actual_intensity)

    init {
        _deleteButton.setOnClickListener {
            showConfirmDialog(context, R.string.on_delete_activity_log) { deleteCallback(this) }
        }
        _endButton.setOnClickListener {
            showEditTextDialog(context, R.string.end, R.string.activity_actual_intensity) {
                _endButton.visibility = View.GONE
                setText(_actualIntensity, it)
                _details.put(ACTUAL_INTENSITY, it)
                val endTime = now()
                _details.put(ACTUAL_END_TIME, endTime)
                _details.put(ACTUAL_DURATION, endTime - _startTime.toLong())
                setText(_actualEndTime, endTime.toString())
                endCallback(this)
            }
        }
        _endButton.visibility = if (_details.has(ACTUAL_END_TIME)) View.GONE else View.VISIBLE
        setText(R.id.text_activity_name, _name)
        setText(R.id.text_activity_desc, text(DESC))
        setText(R.id.text_activity_targeted_start_time, text(TARGETED_START_TIME))
        setText(R.id.text_activity_actual_start_time, _startTime)
        setText(R.id.text_activity_start_notification, text(START_NOTIFICATION))
        setText(R.id.text_activity_targeted_end_time, text(TARGETED_END_TIME))
        setText(_actualEndTime, text(ACTUAL_END_TIME))
        setText(R.id.text_activity_end_notification, text(END_NOTIFICATION))
        setText(R.id.text_activity_targeted_duration, text(TARGETED_DURATION))
        setText(R.id.text_activity_actual_duration, text(ACTUAL_DURATION))
        setText(R.id.text_activity_targeted_intensity, text(TARGETED_INTENSITY))
        setText(_actualIntensity, text(ACTUAL_INTENSITY))
    }

    fun name() = _name
    fun startTime() = _startTime.toLong()
    fun details() = _details
    fun endNotification(): String = _details.getString(END_NOTIFICATION)
    fun view(): View = _view

    @UiThread
    private fun setText(id: Int, text: String) = setText(textView(id), text)
    @UiThread
    private fun setText(textView: TextView, text: String) {
        textView.text = text
        textView.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    @UiThread
    private fun textView(id: Int): TextView = view(id)
    @UiThread
    private fun <T : View> view(id: Int): T = _view.findViewById(id)
    @UiThread
    private fun text(key: String) = if (_details.has(key)) _details.getString(key) else ""

}