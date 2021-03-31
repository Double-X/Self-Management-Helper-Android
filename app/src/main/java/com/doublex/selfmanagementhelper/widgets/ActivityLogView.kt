package com.doublex.selfmanagementhelper.widgets

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import com.doublex.selfmanagementhelper.showConfirmDialog
import com.doublex.selfmanagementhelper.showEditTextDialog
import com.doublex.selfmanagementhelper.views.BeforeBelowAboveAfterView
import com.doublex.selfmanagementhelper.views.HourMinSecView.Companion.HOURS
import com.doublex.selfmanagementhelper.views.HourMinSecView.Companion.MINUTES
import com.doublex.selfmanagementhelper.views.HourMinSecView.Companion.SECS
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.DESC
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.DURATION_FLAG
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.END_NOTIFICATION
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.END_TIME_FLAG
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.START_NOTIFICATION
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.START_TIME_FLAG
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.TARGETED_DURATION
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.TARGETED_END_TIME
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.TARGETED_INTENSITY
import com.doublex.selfmanagementhelper.widgets.NewActivityView.Companion.TARGETED_START_TIME
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

internal class ActivityLogView(
    private val _context: Context,
    private val _name: String,
    private val _startTime: String,
    private val _details: JSONObject,
    deleteCallback: (ActivityLogView) -> Unit,
    endCallback: (ActivityLogView) -> Unit
) {

    companion object {

        fun now() = Calendar.getInstance().time.time

        private const val ACTUAL_END_TIME = "Actual End Time"
        private const val ACTUAL_DURATION = "Actual Duration"
        private const val ACTUAL_INTENSITY = "Actual Intensity"

    }

    private var _timeSeparator = _context.getString(R.string.time_separator)

    private val _simpleDateFormat = SimpleDateFormat(
        _context.getString(R.string.date_format),
        Locale.ROOT
    ) // The date format is the same across all locales so _simpleDateFormat can be initialized here

    private val _view = View.inflate(_context, R.layout.widget_activity_log, null)

    private val _buttonDelete = button(R.id.button_delete_log)
    private val _buttonDetails = button(R.id.button_log_details)
    private val _buttonEnd = button(R.id.button_end_activity)
    private val _linearDetails = linearLayout(R.id.linear_log_details)

    private val _textNameKey = textView(R.id.text_activity_name_key)
    private val _textNameValue = textView(R.id.text_activity_name_value)
    private val _linearDesc = linearLayout(R.id.linear_activity_desc)
    private val _textDescKey = textView(R.id.text_activity_desc_key)
    private val _textDescValue = textView(R.id.text_activity_desc_value)

    private val _linearTargetedStartTime = linearLayout(R.id.linear_activity_targeted_start_time)
    private val _textTargetedStartTimeKey = textView(R.id.text_activity_targeted_start_time_key)
    private val _textTargetedStartTimeValue = textView(R.id.text_activity_targeted_start_time_value)
    private val _textActualStartTimeKey = textView(R.id.text_activity_actual_start_time_key)
    private val _textActualStartTimeValue = textView(R.id.text_activity_actual_start_time_value)
    private val _linearStartNotification = linearLayout(R.id.linear_activity_start_notification)
    private val _textStartNotificationKey = textView(R.id.text_activity_start_notification_key)
    private val _textStartNotificationValue = textView(R.id.text_activity_start_notification_value)

    private val _linearTargetedEndTime = linearLayout(R.id.linear_activity_targeted_end_time)
    private val _textTargetedEndTimeKey = textView(R.id.text_activity_targeted_end_time_key)
    private val _textTargetedEndTimeValue = textView(R.id.text_activity_targeted_end_time_value)
    private val _linearActualEndTime = linearLayout(R.id.linear_activity_actual_end_time)
    private val _textActualEndTimeKey = textView(R.id.text_activity_actual_end_time_key)
    private val _textActualEndTimeValue = textView(R.id.text_activity_actual_end_time_value)
    private val _linearEndNotification = linearLayout(R.id.linear_activity_end_notification)
    private val _textEndNotificationKey = textView(R.id.text_activity_end_notification_key)
    private val _textEndNotificationValue = textView(R.id.text_activity_end_notification_value)

    private val _linearTargetedDuration = linearLayout(R.id.linear_activity_targeted_duration)
    private val _textTargetedDurationKey = textView(R.id.text_activity_targeted_duration_key)
    private val _textTargetedDurationValue = textView(R.id.text_activity_targeted_duration_value)
    private val _linearActualDuration = linearLayout(R.id.linear_activity_actual_duration)
    private val _textActualDurationKey = textView(R.id.text_activity_actual_duration_key)
    private val _textActualDurationValue = textView(R.id.text_activity_actual_duration_value)

    private val _linearTargetedIntensity = linearLayout(R.id.linear_activity_targeted_intensity)
    private val _textTargetedIntensityKey = textView(R.id.text_activity_targeted_intensity_key)
    private val _textTargetedIntensityValue = textView(R.id.text_activity_targeted_intensity_value)
    private val _linearActualIntensity = linearLayout(R.id.linear_activity_actual_intensity)
    private val _textActualIntensityKey = textView(R.id.text_activity_actual_intensity_key)
    private val _textActualIntensityValue = textView(R.id.text_activity_actual_intensity_value)

    init {
        _buttonDelete.setOnClickListener {
            showConfirmDialog(_context, R.string.on_delete_activity_log) { deleteCallback(this) }
        }
        _buttonDetails.setOnClickListener { onToggleDetailsVisibility() }
        _buttonEnd.setOnClickListener {
            showEditTextDialog(_context, R.string.end, R.string.activity_actual_intensity) {
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
    @UiThread
    fun view(): View = _view

    @UiThread
    fun redrawTexts() {
        _timeSeparator = _context.getString(R.string.time_separator)
        redrawButtons()
        redrawNameDesc()
        redrawStartTimes()
        redrawEndTimes()
        redrawDurations()
        redrawIntensities()
    }

    @UiThread
    private fun onEnd(actualIntensity: String, endCallback: (ActivityLogView) -> Unit) {
        _buttonEnd.visibility = View.GONE
        setText(_linearActualIntensity, _textActualIntensityValue, actualIntensity)
        _details.put(ACTUAL_INTENSITY, actualIntensity)
        val endTime = now()
        val endTimeString = endTime.toString()
        _details.put(ACTUAL_END_TIME, endTimeString)
        _details.put(ACTUAL_DURATION, (endTime - _startTime.toLong()).toString())
        setText(_linearActualEndTime, _textActualEndTimeValue, endTimeString)
        endCallback(this)
    }

    @UiThread
    private fun redrawButtons() {
        _buttonDelete.setText(R.string.delete)
        // Preserves the visibility state while still redrawing the button text
        onToggleDetailsVisibility()
        onToggleDetailsVisibility()
        //
        _buttonEnd.setText(R.string.end)
    }

    @UiThread
    private fun onToggleDetailsVisibility() {
        when (_linearDetails.visibility) {
            View.VISIBLE -> hideDetails()
            View.GONE -> showDetails()
        }
    }
    @UiThread
    private fun hideDetails() {
        _buttonDetails.setText(R.string.show)
        _linearDetails.visibility = View.GONE
    }
    @UiThread
    private fun showDetails() {
        _buttonDetails.setText(R.string.hide)
        _linearDetails.visibility = View.VISIBLE
    }

    @UiThread
    private fun redrawNameDesc() {
        _textNameKey.setText(R.string.activity_name)
        _textNameValue.text = _name
        _textDescKey.setText(R.string.activity_desc)
        setText(_linearDesc, _textDescValue, text(DESC))
    }
    @UiThread
    private fun redrawStartTimes() {
        _textTargetedStartTimeKey.setText(R.string.activity_targeted_start_time)
        setText(
            _linearTargetedStartTime,
            _textTargetedStartTimeValue,
            jsonObjectTime(R.string.after, START_TIME_FLAG, R.string.before, TARGETED_START_TIME)
        )
        _textActualStartTimeKey.setText(R.string.activity_actual_start_time)
        _textActualStartTimeValue.text = longTime(_startTime.toLong())
        _textStartNotificationKey.setText(R.string.activity_start_notification)
        setText(_linearStartNotification, _textStartNotificationValue, text(START_NOTIFICATION))
    }
    @UiThread
    private fun redrawEndTimes() {
        _textTargetedEndTimeKey.setText(R.string.activity_targeted_end_time)
        setText(
            _linearTargetedEndTime,
            _textTargetedEndTimeValue,
            jsonObjectTime(R.string.after, END_TIME_FLAG, R.string.before, TARGETED_END_TIME)
        )
        _textActualEndTimeKey.setText(R.string.activity_actual_end_time)
        setText(_linearActualEndTime, _textActualEndTimeValue, longTime(ACTUAL_END_TIME))
        _textEndNotificationKey.setText(R.string.activity_end_notification)
        setText(_linearEndNotification, _textEndNotificationValue, text(END_NOTIFICATION))
    }
    @UiThread
    private fun redrawDurations() {
        _textTargetedDurationKey.setText(R.string.activity_targeted_duration)
        setText(
            _linearTargetedDuration,
            _textTargetedDurationValue,
            jsonObjectTime(R.string.above, DURATION_FLAG, R.string.below, TARGETED_DURATION)
        )
        _textActualDurationKey.setText(R.string.activity_actual_duration)
        setText(_linearActualDuration, _textActualDurationValue, longTime(ACTUAL_DURATION))
    }
    @UiThread
    private fun redrawIntensities() {
        _textTargetedIntensityKey.setText(R.string.activity_targeted_intensity)
        setText(_linearTargetedIntensity, _textTargetedIntensityValue, text(TARGETED_INTENSITY))
        _textActualIntensityKey.setText(R.string.activity_actual_intensity)
        setText(_linearActualIntensity, _textActualIntensityValue, text(ACTUAL_INTENSITY))
    }

    @UiThread
    private fun setText(linearLayout: LinearLayout, textView: TextView, text: String) {
        linearLayout.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
        textView.text = text
    }

    @UiThread
    private fun button(id: Int): Button = view(id)
    @UiThread
    private fun linearLayout(id: Int): LinearLayout = view(id)
    @UiThread
    private fun textView(id: Int): TextView = view(id)
    @UiThread
    private fun <T : View> view(id: Int): T = _view.findViewById(id)

    private fun longTime(key: String): String {
        return if (_details.has(key)) longTime(_details.getString(key).toLong()) else ""
    }
    private fun longTime(time: Long): String = _simpleDateFormat.format(Date(time))
    private fun jsonObjectTime(
        afterAboveR: Int,
        flagKey: String,
        beforeBelowR: Int,
        timeKey: String
    ): String {
        if (!_details.has(timeKey)) return ""
        val jsonObject = _details.getJSONObject(timeKey)
        return listOf(
            flag(afterAboveR, flagKey, beforeBelowR),
            time(jsonObject, HOURS, R.string.hours),
            time(jsonObject, MINUTES, R.string.minutes),
            time(jsonObject, SECS, R.string.secs)
        ).filter { time -> time.isNotEmpty() }.joinToString(_timeSeparator)
    }
    private fun flag(afterAboveR: Int, flagKey: String, beforeBelowR: Int): String {
        if (!_details.has(flagKey)) return ""
        return when (_details.getString(flagKey)) {
            BeforeBelowAboveAfterView.Flag.AFTER_ABOVE.flag -> _context.getString(afterAboveR)
            BeforeBelowAboveAfterView.Flag.BEFORE_BELOW.flag -> _context.getString(beforeBelowR)
            else -> ""
        }
    }
    private fun time(jsonObject: JSONObject, key: String, stringR: Int): String {
        if (!jsonObject.has(key)) return ""
        return jsonObject.getString(key) + _timeSeparator + _context.getString(stringR)
    }
    private fun text(key: String) = if (_details.has(key)) _details.getString(key) else ""

}