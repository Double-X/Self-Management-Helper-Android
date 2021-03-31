package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R
import org.json.JSONObject

internal open class HourMinSecView(private val _context: Context, private val _view: View) {

    companion object {
        const val HOURS = "Hours"
        const val MINUTES = "Minutes"
        const val SECS = "Secs"
    }

    private val _res = _context.resources

    private val _spinnerHours = spinner(R.id.spinner_hours, R.array.spinner_0_23)
    private val _spinnerMinutes = spinner(R.id.spinner_minutes, R.array.spinner_0_59)
    private val _spinnerSecs = spinner(R.id.spinner_secs, R.array.spinner_0_59)

    private val _textHours = textView(R.id.text_hours)
    private val _textMinutes = textView(R.id.text_minutes)
    private val _textSecs = textView(R.id.text_secs)

    @UiThread
    open fun clear() {
        clearSpinner(_spinnerHours)
        clearSpinner(_spinnerMinutes)
        clearSpinner(_spinnerSecs)
    }
    @UiThread
    open fun redrawTexts() {
        _textHours.setText(R.string.hours)
        _textMinutes.setText(R.string.minutes)
        _textSecs.setText(R.string.secs)
    }
    @UiThread
    open fun set(times: JSONObject) {
        setSpinner(_spinnerHours, time(times, HOURS))
        setSpinner(_spinnerMinutes, time(times, MINUTES))
        setSpinner(_spinnerSecs, time(times, SECS))
    }

    open fun times(): JSONObject {
        val times = JSONObject()
        putString(times, HOURS, _spinnerHours)
        putString(times, MINUTES, _spinnerMinutes)
        putString(times, SECS, _spinnerSecs)
        return times
    }

    @UiThread
    protected fun setSpinner(spinner: Spinner, time: String) {
        // Makes use of the fact that the spinner items are the same as their indices
        if (time.isEmpty()) clearSpinner(spinner) else spinner.setSelection(time.toInt() + 1)
        //
    }
    @UiThread
    protected fun clearSpinner(spinner: Spinner) {
        spinner.setSelection(0)
        spinner.isSelected = false
    }

    protected fun putString(times: JSONObject, key: String, spinner: Spinner) {
        val time = time(spinner)
        if (time.isNotEmpty()) times.put(key, time) // Minimizes the size of the data stored
    }

    protected fun time(times: JSONObject, key: String): String {
        return if (times.has(key)) times.getString(key) else ""
    }

    @UiThread
    protected fun time(spinner: Spinner) = spinner.selectedItem?.toString() ?: ""
    @UiThread
    protected fun spinner(id: Int, objectsR: Int): Spinner {
        val spinner = view<Spinner>(id)
        val res = R.layout.support_simple_spinner_dropdown_item
        spinner.adapter = ArrayAdapter(_context, res, _res.getStringArray(objectsR).toList())
        return spinner
    }
    @UiThread
    protected fun textView(id: Int): TextView = view(id)
    @UiThread
    protected fun <T: View>view(id: Int): T = _view.findViewById(id)

}