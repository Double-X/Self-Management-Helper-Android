package com.doublex.selfmanagementhelper.views

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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

    private val _buttonDelete = button(R.id.button_delete_activity)
    private val _buttonDetails = button(R.id.button_activity_details)
    private val _buttonRename = button(R.id.button_rename_activity)
    private val _buttonSave = button(R.id.button_save_activity)
    private val _buttonSaveAs = button(R.id.button_save_as_activity)
    private val _buttonStart = button(R.id.button_start_activity)

    private val _linearDetails = view<LinearLayout>(R.id.linear_activity_details)
    private val _newActivity = NewActivityView(_context, _view, _name, details)
    private val _res = _context.resources

    private var _details = details // Restores the details of this activity after pressing "save as"

    init {
        setListeners()
        hideDetails()
    }

    @UiThread
    fun name() = _newActivity.name()
    @UiThread
    fun view() = _view

    @UiThread
    fun redrawTexts() {
        _buttonDelete.setText(R.string.delete)
        // Preserves the visibility state while still redrawing the button text
        onToggleDetailsVisibility()
        onToggleDetailsVisibility()
        //
        _buttonRename.setText(R.string.rename)
        _buttonSave.setText(R.string.save)
        _buttonSaveAs.setText(R.string.save_as)
        _buttonStart.setText(R.string.start)
        _newActivity.redrawTexts()
    }

    @UiThread
    private fun button(id: Int): Button = view(id)
    @UiThread
    private fun <T> view(id: Int): T = _view.findViewById(id)

    @UiThread
    private fun setListeners() {
        _buttonDelete.setOnClickListener { onDelete() }
        _buttonDetails.setOnClickListener { onToggleDetailsVisibility() }
        _buttonRename.setOnClickListener { onRename() }
        _buttonSave.setOnClickListener { onSave() }
        _buttonSaveAs.setOnClickListener { onSaveAs() }
        _buttonStart.setOnClickListener { onStart() }
    }

    @UiThread
    private fun onDelete() {
        val text = String.format(_res.getString(R.string.on_delete_activity), _name)
        showConfirmDialog(_context, text) { _callbacks.onDelete(this) }
    }

    @UiThread
    private fun onToggleDetailsVisibility() {
        when (_linearDetails.visibility) {
            View.VISIBLE -> hideDetails()
            View.GONE -> showDetails()
        }
    }
    @UiThread
    private fun hideDetails() {
        _buttonDetails.setText(R.string.show)
        _linearDetails.visibility = View.GONE
    }
    @UiThread
    private fun showDetails() {
        _buttonDetails.setText(R.string.hide)
        _linearDetails.visibility = View.VISIBLE
    }

    @UiThread
    private fun onRename() {
        showActivityNameDialog(_context, R.string.rename, R.string.activity_name) { newName ->
            val oldName = _newActivity.name()
            if (newName == oldName) return@showActivityNameDialog
            _details = _newActivity.details()
            _callbacks.onRename(newName, _details, oldName)
            _newActivity.setName(newName)
        }
    }

    @UiThread
    private fun onSave() {
        _details = _newActivity.details()
        _callbacks.onSave(_name, _details)
    }

    @UiThread
    private fun onSaveAs() {
        showActivityNameDialog(_context, R.string.save_as, R.string.activity_name) { name ->
            _callbacks.onSaveAs(name, _newActivity.details())
            _newActivity.setDetails(_details)
        }
    }

    @UiThread
    private fun onStart() {
        _callbacks.onStart(_name, _newActivity.details(), _newActivity.startNotification())
    }

}