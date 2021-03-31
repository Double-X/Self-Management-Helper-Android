package com.doublex.selfmanagementhelper.views

import android.view.View
import com.doublex.selfmanagementhelper.R

internal class BelowAboveView(view: View) : BeforeBelowAboveAfterView(view) {

    override fun afterAboveId(): Int = R.id.chip_above
    override fun beforeBelowId(): Int = R.id.chip_below
    override fun afterAboveBeforeBelowId(): Int = R.id.chip_group_above_below
    override fun afterAboveStringR(): Int = R.string.above
    override fun beforeBelowStringR(): Int = R.string.below

}