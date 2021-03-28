package com.doublex.selfmanagementhelper.widgets

import android.view.View
import androidx.annotation.UiThread
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

internal abstract class BeforeBelowAboveAfterView(private val _view: View) {

    private enum class Flag(val flag: String) {
        AFTER_ABOVE("After Above"),
        BEFORE_BELOW("Before Below"),
        NONE("None")
    }

    private val _afterAboveChip by lazy { chip(afterAboveId()) }
    private val _beforeBelowChip by lazy { chip(beforeBelowId()) }
    private val _chipGroup by lazy { _view.findViewById(afterAboveBeforeBelowId()) as ChipGroup }

    @UiThread
    fun set(flag: String) {
        setChip(_afterAboveChip, flag, Flag.AFTER_ABOVE)
        setChip(_beforeBelowChip, flag, Flag.BEFORE_BELOW)
    }
    @UiThread
    fun clear() = _chipGroup.clearCheck()
    @UiThread
    fun redrawTexts() {
        _afterAboveChip.setText(afterAboveStringR())
        _beforeBelowChip.setText(beforeBelowStringR())
    }

    @UiThread
    fun flag(): String {
        if (_afterAboveChip.isChecked) return Flag.AFTER_ABOVE.flag
        return if (_beforeBelowChip.isChecked) Flag.BEFORE_BELOW.flag else Flag.NONE.flag
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