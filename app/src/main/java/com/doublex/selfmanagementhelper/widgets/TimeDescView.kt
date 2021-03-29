package com.doublex.selfmanagementhelper.widgets

import android.view.View
import android.widget.TextView
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R

internal class TimeDescView(view: View, private val _timeDesc: Int, viewClass: ViewClass) {

    enum class ViewClass { BeforeAfter, BelowAbove }

    private val _beforeBelowAboveAfter: BeforeBelowAboveAfterView = when (viewClass) {
        ViewClass.BeforeAfter -> BeforeAfterView(view)
        ViewClass.BelowAbove -> BelowAboveView(view)
    }
    private val _textTimeDesc: TextView = view.findViewById(R.id.text_time_desc)

    init { setDesc() }

    @UiThread
    fun clear() = _beforeBelowAboveAfter.clear()
    @UiThread
    fun redrawTexts() {
        _beforeBelowAboveAfter.redrawTexts()
        setDesc()
    }
    @UiThread
    fun set(flag: String) = _beforeBelowAboveAfter.set(flag)

    @UiThread
    fun flag() = _beforeBelowAboveAfter.flag()

    @UiThread
    private fun setDesc() = _textTimeDesc.setText(_timeDesc)

}