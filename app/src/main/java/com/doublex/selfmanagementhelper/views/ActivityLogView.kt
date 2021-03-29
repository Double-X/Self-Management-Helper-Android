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

    private val _buttonDelete = view<Button>(R.id.button_delete_activity_log)
    private val _buttonEnd = view<Button>(R.id.button_end_activity)

    private val _textName = textView(R.id.text_activity_name)
    private val _textDesc = textView(R.id.text_activity_desc)

    private val _textTargetedStartTime = textView(R.id.text_activity_targeted_start_time)
    private val _textActualStartTime = textView(R.id.text_activity_actual_start_time)
    private val _textStartNotification = textView(R.id.text_activity_start_notification)

    private val _textTargetedEndTime = textView(R.id.text_activity_targeted_end_time)
    private val _textActualEndTime = textView(R.id.text_activity_targeted_end_time)
    private val _textEndNotification = textView(R.id.text_activity_end_notification)

    private val _textTargetedDuration = textView(R.id.text_activity_targeted_duration)
    private val _textActualDuration = textView(R.id.text_activity_actual_duration)

    private val _textTargetedIntensity = textView(R.id.text_activity_targeted_intensity)
    private val _textActualIntensity = textView(R.id.text_activity_actual_intensity)

    init {
        _buttonDelete.setOnClickListener {
            showConfirmDialog(context, R.string.on_delete_activity_log) { deleteCallback(this) }
        }
        _buttonEnd.setOnClickListener {
            showEditTextDialog(context, R.string.end, R.string.activity_actual_intensity) {
                onEnd(it, endCallback)
            }
        }
        _buttonEnd.visibility = if (_details.has(ACTUAL_END_TIME)) View.GONE else View.VISIBLE
        redrawTexts()
    }

    fun name() = _name
    fun startTime() = _startTime.toLong()
    fun details() = _details
    fun endNotification(): String = _details.getString(END_NOTIFICATION)
    fun view(): View = _view

    fun redrawTexts() {
        _textName.text = _name
        setText(_textDesc, text(DESC))
        setText(_textTargetedStartTime, text(TARGETED_START_TIME))
        _textActualStartTime.text = _startTime
        setText(_textStartNotification, text(START_NOTIFICATION))
        setText(_textTargetedEndTime, text(TARGETED_END_TIME))
        setText(_textActualEndTime, text(ACTUAL_END_TIME))
        setText(_textEndNotification, text(END_NOTIFICATION))
        setText(_textTargetedDuration, text(TARGETED_DURATION))
        setText(_textActualDuration, text(ACTUAL_DURATION))
        setText(_textTargetedIntensity, text(TARGETED_INTENSITY))
        setText(_textActualIntensity, text(ACTUAL_INTENSITY))
    }

    @UiThread
    private fun onEnd(actualIntensity: String, endCallback: (ActivityLogView) -> Unit) {
        _buttonEnd.visibility = View.GONE
        setText(_textActualIntensity, actualIntensity)
        _details.put(ACTUAL_INTENSITY, actualIntensity)
        val endTime = now()
        _details.put(ACTUAL_END_TIME, endTime)
        _details.put(ACTUAL_DURATION, endTime - _startTime.toLong())
        setText(_textActualEndTime, endTime.toString())
        endCallback(this)
    }
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