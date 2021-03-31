package com.doublex.selfmanagementhelper.views

import android.view.View
import androidx.annotation.UiThread
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

internal abstract class BeforeBelowAboveAfterView(private val _view: View) {

    enum class Flag(val flag: String) {
        AFTER_ABOVE("After Above"),
        BEFORE_BELOW("Before Below")
    }

    private val _afterAbove by lazy { chip(afterAboveId()) }
    private val _beforeBelow by lazy { chip(beforeBelowId()) }
    private val _afterAboveBeforeBelow by lazy {
        _view.findViewById(afterAboveBeforeBelowId()) as ChipGroup
    }

    @UiThread
    fun clear() = _afterAboveBeforeBelow.clearCheck()
    @UiThread
    fun redrawTexts() {
        _afterAbove.setText(afterAboveStringR())
        _beforeBelow.setText(beforeBelowStringR())
    }
    @UiThread
    fun set(flag: String) {
        setChip(_afterAbove, flag, Flag.AFTER_ABOVE)
        setChip(_beforeBelow, flag, Flag.BEFORE_BELOW)
    }

    @UiThread
    fun flag(): String {
        if (_afterAbove.isChecked) return Flag.AFTER_ABOVE.flag
        // Minimizes the size of the data stored
        return if (_beforeBelow.isChecked) Flag.BEFORE_BELOW.flag else ""
        //
    }

    protected abstract fun afterAboveId(): Int
    protected abstract fun beforeBelowId(): Int
    protected abstract fun afterAboveBeforeBelowId(): Int
    protected abstract fun afterAboveStringR(): Int
    protected abstract fun beforeBelowStringR(): Int

    @UiThread
    private fun chip(id: Int): Chip = _view.findViewById(id)

    @UiThread
    private fun setChip(chip: Chip, flagString: String, flagEnum: Flag) {
        chip.isChecked = flagString == flagEnum.flag
    }

}