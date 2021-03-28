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
    override fun set(time: JSONObject) {
        super.set(time)
        _editTextWeek.setText(time.getString(WEEKS))
        setSpinner(_spinnerDays, time.getString(DAYS))
    }
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
    override fun times(): JSONObject {
        val jsonObject = super.times()
        jsonObject.put(DAYS, time(_spinnerDays))
        jsonObject.put(WEEKS, _editTextWeek.text.toString())
        return jsonObject
    }

}