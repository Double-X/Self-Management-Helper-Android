package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import org.json.JSONObject

internal class NewActivityView(context: Context, private val _view: View) {

    private companion object {
        const val DESC = "Desc"
        const val TARGETED_START_TIME = "Targeted Start Time"
        const val START_TIME_FLAG = "Start Time Flag"
        const val START_NOTIFICATION = "Start Notification"
        const val TARGETED_END_TIME = "Targeted End Time"
        const val END_TIME_FLAG = "End Time Flag"
        const val END_NOTIFICATION = "End Notification"
        const val TARGETED_DURATION = "Targeted Duration"
        const val DURATION_FLAG = "Duration Flag"
        const val TARGETED_INTENSITY = "Targeted Intensity"
        const val INTENSITY_FLAG = "Intensity Flag"
        const val TARGETED_INTERVAL = "Targeted Interval"
        const val INTERVAL_FLAG = "Interval Flag"
    }

    private val _editTextName = editText(R.id.edit_text_activity_name)
    private val _editTextDesc = editText(R.id.edit_text_activity_desc)
    private val _editTextStart = editText(R.id.edit_text_activity_start_notification)
    private val _editTextEnd = editText(R.id.edit_text_activity_end_notification)
    private val _editTextIntensity = editText(R.id.edit_text_activity_intensity)

    private val _startBeforeAfterView = TimeDescView(
        view(R.id.view_before_after_start),
        R.string.activity_target_start_time,
        TimeDescView.ViewClass.BeforeAfter
    )
    private val _endBeforeAfterView = TimeDescView(
        view(R.id.view_before_after_end),
        R.string.activity_target_end_time,
        TimeDescView.ViewClass.BeforeAfter
    )
    private val _durationAboveBelowView = TimeDescView(
        view(R.id.view_duration_below_above),
        R.string.activity_target_duration,
        TimeDescView.ViewClass.BelowAbove
    )
    private val _intensityAboveBelowView = BelowAboveView(view(R.id.view_intensity_below_above))
    private val _intervalAboveBelowView = TimeDescView(
        view(R.id.view_interval_below_above),
        R.string.activity_target_interval,
        TimeDescView.ViewClass.BelowAbove
    )

    private val _startHourMinSecView = HourMinSecView(context, view(R.id.view_start_hour_min_sec))
    private val _endHourMinSecView = HourMinSecView(context, view(R.id.view_end_hour_min_sec))
    private val _durationHourMinSecView = HourMinSecView(
        context,
        view(R.id.view_duration_hour_min_sec)
    )
    private val _intervalWeekDayHourMinSecView = WeekDayHourMinSecView(
        context,
        view(R.id.view_interval_week_day_hour_min_sec)
    )

    constructor(
        context: Context,
        view: View,
        name: String,
        details: JSONObject
    ): this(context, view) {
        setName(name)
        setEditTexts(details)
        setRadioButtons(details)
        setTimes(details)
    }

    @UiThread
    fun clear() {
        clearEditTexts()
        clearRadioButtons()
        clearTimes()
    }
    @UiThread
    fun redrawTexts() {
        redrawEditTextHints()
        redrawRadioButtonTexts()
        redrawTimes()
    }

    @UiThread
    fun name(): String = viewString(_editTextName)
    @UiThread
    fun details(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(DESC, viewString(_editTextDesc))
        jsonObject.put(TARGETED_START_TIME, _startHourMinSecView.times())
        jsonObject.put(START_TIME_FLAG, _startBeforeAfterView.flag())
        jsonObject.put(START_NOTIFICATION, startNotification())
        jsonObject.put(TARGETED_END_TIME, _endHourMinSecView.times())
        jsonObject.put(END_TIME_FLAG, _endBeforeAfterView.flag())
        jsonObject.put(END_NOTIFICATION, endNotification())
        jsonObject.put(TARGETED_DURATION, _durationHourMinSecView.times())
        jsonObject.put(DURATION_FLAG, _durationAboveBelowView.flag())
        jsonObject.put(TARGETED_INTENSITY, viewString(_editTextIntensity))
        jsonObject.put(INTENSITY_FLAG, _intensityAboveBelowView.flag())
        jsonObject.put(TARGETED_INTERVAL, _intervalWeekDayHourMinSecView.times())
        jsonObject.put(INTERVAL_FLAG, _intervalAboveBelowView.flag())
        return jsonObject
    }
    @UiThread
    fun startNotification(): String = viewString(_editTextStart)
    @UiThread
    fun endNotification(): String = viewString(_editTextEnd)

    @UiThread
    private fun setName(name: String) {
        _editTextName.isEnabled = false
        _editTextName.setText(name)
    }
    @UiThread
    private fun setEditTexts(details: JSONObject) {
        _editTextDesc.setText(details.getString(DESC))
        _editTextStart.setText(details.getString(START_NOTIFICATION))
        _editTextEnd.setText(details.getString(END_NOTIFICATION))
        _editTextIntensity.setText(details.getString(TARGETED_INTENSITY))
    }
    @UiThread
    private fun setRadioButtons(details: JSONObject) {
        _startBeforeAfterView.set(details.getString(START_TIME_FLAG))
        _endBeforeAfterView.set(details.getString(END_TIME_FLAG))
        _durationAboveBelowView.set(details.getString(DURATION_FLAG))
        _intensityAboveBelowView.set(details.getString(INTENSITY_FLAG))
        _intervalAboveBelowView.set(details.getString(INTERVAL_FLAG))
    }
    @UiThread
    fun setTimes(details: JSONObject) {
        _startHourMinSecView.set(details.getJSONObject(TARGETED_START_TIME))
        _endHourMinSecView.set(details.getJSONObject(TARGETED_END_TIME))
        _durationHourMinSecView.set(details.getJSONObject(TARGETED_DURATION))
        _intervalWeekDayHourMinSecView.set(details.getJSONObject(TARGETED_INTERVAL))
    }

    @UiThread
    private fun clearEditTexts() {
        clearEditText(_editTextName)
        clearEditText(_editTextDesc)
        clearEditText(_editTextStart)
        clearEditText(_editTextEnd)
        clearEditText(_editTextIntensity)
    }
    @UiThread
    private fun clearEditText(editText: EditText) = editText.setText("")
    @UiThread
    private fun clearRadioButtons() {
        _startBeforeAfterView.clear()
        _endBeforeAfterView.clear()
        _durationAboveBelowView.clear()
        _intensityAboveBelowView.clear()
        _intervalAboveBelowView.clear()
    }
    @UiThread
    fun clearTimes() {
        _startHourMinSecView.clear()
        _endHourMinSecView.clear()
        _durationHourMinSecView.clear()
        _intervalWeekDayHourMinSecView.clear()
    }

    @UiThread
    private fun redrawEditTextHints() {
        _editTextName.setHint(R.string.activity_name)
        _editTextDesc.setHint(R.string.activity_desc)
        _editTextStart.setHint(R.string.activity_start_notification)
        _editTextEnd.setHint(R.string.activity_end_notification)
        _editTextIntensity.setHint(R.string.activity_target_intensity)
    }
    @UiThread
    private fun redrawRadioButtonTexts() {
        _startBeforeAfterView.redrawTexts()
        _endBeforeAfterView.redrawTexts()
        _durationAboveBelowView.redrawTexts()
        _intensityAboveBelowView.redrawTexts()
        _intervalAboveBelowView.redrawTexts()
    }
    @UiThread
    private fun redrawTimes() {
        _startHourMinSecView.redrawTexts()
        _endHourMinSecView.redrawTexts()
        _durationHourMinSecView.redrawTexts()
        _intervalWeekDayHourMinSecView.redrawTexts()
    }

    @UiThread
    private fun editText(id: Int): EditText = view(id)
    @UiThread
    private fun <T: View>view(id: Int): T = _view.findViewById(id)
    @UiThread
    private fun viewString(editText: EditText): String = editText.text.toString()

}