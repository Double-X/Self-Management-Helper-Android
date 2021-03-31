package com.doublex.selfmanagementhelper.views

import android.view.View
import com.doublex.selfmanagementhelper.R

internal class BeforeAfterView(view: View) : BeforeBelowAboveAfterView(view) {

    override fun afterAboveId(): Int = R.id.chip_after
    override fun beforeBelowId(): Int = R.id.chip_before
    override fun afterAboveBeforeBelowId(): Int = R.id.chip_group_after_before
    override fun afterAboveStringR(): Int = R.string.after
    override fun beforeBelowStringR(): Int = R.string.before

}