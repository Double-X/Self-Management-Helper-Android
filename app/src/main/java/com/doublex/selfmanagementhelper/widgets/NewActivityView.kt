package com.doublex.selfmanagementhelper.widgets

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import com.doublex.selfmanagementhelper.views.BelowAboveView
import com.doublex.selfmanagementhelper.views.HourMinSecView
import com.doublex.selfmanagementhelper.views.TimeDescView
import com.doublex.selfmanagementhelper.views.WeekDayHourMinSecView
import org.json.JSONObject

internal class NewActivityView(context: Context, private val _view: View) {

    companion object {

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

    private val _name = editText(R.id.edit_text_activity_name)
    private val _desc = editText(R.id.edit_text_activity_desc)
    private val _start = editText(R.id.edit_text_activity_start_notification)
    private val _end = editText(R.id.edit_text_activity_end_notification)
    private val _intensity = editText(R.id.edit_text_activity_intensity)

    private val _startBeforeAfter = TimeDescView(
        view(R.id.view_before_after_start),
        R.string.activity_targeted_start_time,
        TimeDescView.ViewClass.BeforeAfter
    )
    private val _endBeforeAfter = TimeDescView(
        view(R.id.view_before_after_end),
        R.string.activity_targeted_end_time,
        TimeDescView.ViewClass.BeforeAfter
    )
    private val _durationAboveBelow = TimeDescView(
        view(R.id.view_duration_below_above),
        R.string.activity_targeted_duration,
        TimeDescView.ViewClass.BelowAbove
    )
    private val _intensityAboveBelow = BelowAboveView(view(R.id.view_intensity_below_above))
    private val _intervalAboveBelow = TimeDescView(
        view(R.id.view_interval_below_above),
        R.string.activity_targeted_interval,
        TimeDescView.ViewClass.BelowAbove
    )

    private val _startHourMinSec = HourMinSecView(context, view(R.id.view_start_hour_min_sec))
    private val _endHourMinSec = HourMinSecView(context, view(R.id.view_end_hour_min_sec))
    private val _durationHourMinSec = HourMinSecView(context, view(R.id.view_duration_hour_min_sec))
    private val _intervalWeekDayHourMinSec = WeekDayHourMinSecView(
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
        setDetails(details)
    }

    @UiThread
    fun clear() {
        clearChips()
        clearEditTexts()
        clearTimes()
    }
    @UiThread
    fun redrawTexts() {
        redrawEditTextHints()
        redrawRadioButtonTexts()
        redrawTimes()
    }
    @UiThread
    fun setName(name: String) {
        _name.isEnabled = false
        _name.setText(name)
    }
    @UiThread
    fun setDetails(details: JSONObject) {
        setChips(details)
        setEditTexts(details)
        setTimes(details)
    }

    @UiThread
    fun name() = viewString(_name)
    @UiThread
    fun details(): JSONObject {
        val details = JSONObject()
        putJSONObjects(details)
        putStrings(details)
        return details
    }
    @UiThread
    fun startNotification() = viewString(_start)
    @UiThread
    fun endNotification() = viewString(_end)

    @UiThread
    private fun clearChips() {
        _startBeforeAfter.clear()
        _endBeforeAfter.clear()
        _durationAboveBelow.clear()
        _intensityAboveBelow.clear()
        _intervalAboveBelow.clear()
    }
    @UiThread
    private fun clearEditTexts() {
        clearEditText(_name)
        clearEditText(_desc)
        clearEditText(_start)
        clearEditText(_end)
        clearEditText(_intensity)
    }
    @UiThread
    private fun clearEditText(editText: EditText) = editText.setText("")
    @UiThread
    fun clearTimes() {
        _startHourMinSec.clear()
        _endHourMinSec.clear()
        _durationHourMinSec.clear()
        _intervalWeekDayHourMinSec.clear()
    }

    @UiThread
    private fun redrawEditTextHints() {
        _name.setHint(R.string.activity_name)
        _desc.setHint(R.string.activity_desc)
        _start.setHint(R.string.activity_start_notification)
        _end.setHint(R.string.activity_end_notification)
        _intensity.setHint(R.string.activity_targeted_intensity)
    }
    @UiThread
    private fun redrawRadioButtonTexts() {
        _startBeforeAfter.redrawTexts()
        _endBeforeAfter.redrawTexts()
        _durationAboveBelow.redrawTexts()
        _intensityAboveBelow.redrawTexts()
        _intervalAboveBelow.redrawTexts()
    }
    @UiThread
    private fun redrawTimes() {
        _startHourMinSec.redrawTexts()
        _endHourMinSec.redrawTexts()
        _durationHourMinSec.redrawTexts()
        _intervalWeekDayHourMinSec.redrawTexts()
    }

    @UiThread
    private fun setChips(details: JSONObject) {
        _startBeforeAfter.set(string(details, START_TIME_FLAG))
        _endBeforeAfter.set(string(details, END_TIME_FLAG))
        _durationAboveBelow.set(string(details, DURATION_FLAG))
        _intensityAboveBelow.set(string(details, INTENSITY_FLAG))
        _intervalAboveBelow.set(string(details, INTERVAL_FLAG))
    }
    @UiThread
    private fun setEditTexts(details: JSONObject) {
        _desc.setText(string(details, DESC))
        _start.setText(string(details, START_NOTIFICATION))
        _end.setText(string(details, END_NOTIFICATION))
        _intensity.setText(string(details, TARGETED_INTENSITY))
    }
    private fun string(details: JSONObject, key: String): String {
        return if (details.has(key)) details.getString(key) else ""
    }
    @UiThread
    private fun setTimes(details: JSONObject) {
        _startHourMinSec.set(jsonObject(details, TARGETED_START_TIME))
        _endHourMinSec.set(jsonObject(details, TARGETED_END_TIME))
        _durationHourMinSec.set(jsonObject(details, TARGETED_DURATION))
        _intervalWeekDayHourMinSec.set(jsonObject(details, TARGETED_INTERVAL))
    }
    private fun jsonObject(details: JSONObject, key: String): JSONObject {
        // Avoid null checks at the cost of making throwaway objects
        return if (details.has(key)) details.getJSONObject(key) else JSONObject()
        //
    }

    @UiThread
    private fun putJSONObjects(details: JSONObject) {
        putJSONObject(details, TARGETED_START_TIME, _startHourMinSec.times())
        putJSONObject(details, TARGETED_END_TIME, _endHourMinSec.times())
        putJSONObject(details, TARGETED_DURATION, _durationHourMinSec.times())
        putJSONObject(details, TARGETED_INTERVAL, _intervalWeekDayHourMinSec.times())
    }
    private fun putJSONObject(details: JSONObject, key: String, jsonObject: JSONObject) {
        // Minimizes the size of the data stored
        if (jsonObject.length() > 0) details.put(key, jsonObject)
        //
    }
    @UiThread
    private fun putStrings(details: JSONObject) {
        putString(DESC, details, viewString(_desc))
        putString(START_TIME_FLAG, details, _startBeforeAfter.flag())
        putString(START_NOTIFICATION, details, startNotification())
        putString(END_TIME_FLAG, details, _endBeforeAfter.flag())
        putString(END_NOTIFICATION, details, endNotification())
        putString(DURATION_FLAG, details, _durationAboveBelow.flag())
        putString(TARGETED_INTENSITY, details, viewString(_intensity))
        putString(INTENSITY_FLAG, details, _intensityAboveBelow.flag())
        putString(INTERVAL_FLAG, details, _intervalAboveBelow.flag())
    }
    private fun putString(key: String, details: JSONObject, string: String) {
        if (string.isNotEmpty()) details.put(key, string) // Minimizes the size of the data stored
    }

    @UiThread
    private fun editText(id: Int): EditText = view(id)
    @UiThread
    private fun <T: View>view(id: Int): T = _view.findViewById(id)
    @UiThread
    private fun viewString(editText: EditText) = editText.text.toString()

}