package com.doublex.selfmanagementhelper.widgets

import android.view.View
import android.widget.TextView
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.R

internal class TimeDescView(view: View, private val _timeDesc: Int, viewClass: ViewClass) {

    enum class ViewClass { BeforeAfter, BelowAbove }

    private val _beforeBelowAboveAfterView: BeforeBelowAboveAfterView = when (viewClass) {
        ViewClass.BeforeAfter -> BeforeAfterView(view)
        ViewClass.BelowAbove -> BelowAboveView(view)
    }
    private val _textTimeDesc: TextView = view.findViewById(R.id.text_time_desc)

    init { setDesc() }

    @UiThread
    fun set(flag: String) =_beforeBelowAboveAfterView.set(flag)
    @UiThread
    fun clear() =_beforeBelowAboveAfterView.clear()
    @UiThread
    fun redrawTexts() {
        _beforeBelowAboveAfterView.redrawTexts()
        setDesc()
    }
    @UiThread
    fun flag() = _beforeBelowAboveAfterView.flag()

    @UiThread
    private fun setDesc() = _textTimeDesc.setText(_timeDesc)

}