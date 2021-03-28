package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.DefinedActivity
import com.doublex.selfmanagementhelper.R
import com.doublex.selfmanagementhelper.showConfirmDialog
import com.doublex.selfmanagementhelper.showActivityNameDialog
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

    private var _details = details // Restores the details of this activity after pressing "save as"

    init {
        button(R.id.button_delete_activity).setOnClickListener { onDelete() }
        button(R.id.button_rename_activity).setOnClickListener { onRename() }
        button(R.id.button_save_activity).setOnClickListener { onSave() }
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
    private fun onRename() {
        showActivityNameDialog(_context, R.string.rename, R.string.activity_name) { newName ->
            val oldName = _newActivityView.name()
            if (newName == oldName) return@showActivityNameDialog
            _details = _newActivityView.details()
            _callbacks.onRename(newName, _details, oldName)
            _newActivityView.setName(newName)
        }
    }
    @UiThread
    private fun onSave() {
        _details = _newActivityView.details()
        _callbacks.onSave(_name, _details)
    }
    @UiThread
    private fun onSaveAs() {
        showActivityNameDialog(_context, R.string.save_as, R.string.activity_name) { name ->
            _callbacks.onSaveAs(name, _newActivityView.details())
            _newActivityView.setDetails(_details)
        }
    }
    @UiThread
    private fun onStart() {
        _callbacks.onStart(_name, _newActivityView.details(), _newActivityView.startNotification())
    }

}