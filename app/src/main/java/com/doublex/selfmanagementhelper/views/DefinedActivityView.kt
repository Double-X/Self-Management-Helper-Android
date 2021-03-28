package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.DefinedActivity
import com.doublex.selfmanagementhelper.R
import com.doublex.selfmanagementhelper.showConfirmDialog
import com.doublex.selfmanagementhelper.showEditTextDialog
import org.json.JSONObject

internal class DefinedActivityView(
    details: JSONObject,
    private val _name: String,
    private val _context: Context,
    private val _callbacks: DefinedActivity
) {

    private val _view: View = View.inflate(_context, R.layout.widget_defined_activity, null)
    private val _newActivityView = NewActivityView(_context, _view, _name, details)
    private val _res = _context.resources

    init {
        button(R.id.button_delete_activity).setOnClickListener { onDelete() }
        button(R.id.button_save_activity).setOnClickListener {
            _callbacks.onSave(_name, _newActivityView.details())
        }
        button(R.id.button_save_as_activity).setOnClickListener { onSaveAs() }
        button(R.id.button_start_activity).setOnClickListener { onStart() }
    }

    @UiThread
    fun view() = _view

    @UiThread
    private fun button(id: Int): Button = _view.findViewById(id)
    @UiThread
    private fun onDelete() {
        val text = String.format(_res.getString(R.string.on_delete_activity), _name)
        showConfirmDialog(_context, text) { _callbacks.onDelete(_name) }
    }
    @UiThread
    private fun onSaveAs() {
        showEditTextDialog(_context, R.string.save_as, R.string.activity_name) { name ->
            _callbacks.onSave(name, _newActivityView.details())
        }
    }
    @UiThread
    private fun onStart() {
        _callbacks.onStart(_name, _newActivityView.details(), _newActivityView.startNotification())
    }

}