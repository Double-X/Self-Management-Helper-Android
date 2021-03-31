package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import org.json.JSONObject

internal class WeekDayHourMinSecView(context: Context, view: View) : HourMinSecView(context, view) {

    private companion object {
        const val DAYS = "Days"
        const val WEEKS = "Weeks"
    }

    private val _editTextWeek = view<EditText>(R.id.edit_text_week)
    private val _spinnerDays = spinner(R.id.spinner_days, R.array.spinner_0_6)
    private val _textDays = textView(R.id.text_days)

    @UiThread
    override fun clear() {
        super.clear()
        _editTextWeek.setText("")
        clearSpinner(_spinnerDays)
    }
    @UiThread
    override fun redrawTexts() {
        super.redrawTexts()
        _editTextWeek.setHint(R.string.weeks)
        _textDays.setText(R.string.days)
    }
    @UiThread
    override fun set(times: JSONObject) {
        super.set(times)
        _editTextWeek.setText(time(times, WEEKS))
        setSpinner(_spinnerDays, time(times, DAYS))
    }

    override fun times(): JSONObject {
        val times = super.times()
        putString(times, DAYS, _spinnerDays)
        val weeks = _editTextWeek.text.toString()
        if (weeks.isNotEmpty()) times.put(WEEKS, weeks) // Minimizes the size of the data stored
        return times
    }

}