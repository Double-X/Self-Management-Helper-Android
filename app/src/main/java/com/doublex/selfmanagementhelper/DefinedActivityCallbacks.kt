package com.doublex.selfmanagementhelper

import androidx.annotation.UiThread
import com.doublex.selfmanagementhelper.widgets.DefinedActivityView
import org.json.JSONObject

// It's created to avoid circular reference between DefinedActivityView and DefinedActivityViews
internal interface DefinedActivityCallbacks {
    @UiThread
    fun onDelete(definedActivityView: DefinedActivityView)
    @UiThread
    fun onRename(newName: String, details: JSONObject, oldName: String)
    @UiThread
    fun onSave(name: String, details: JSONObject)
    @UiThread
    fun onSaveAs(name: String, details: JSONObject)
    @UiThread
    fun onStart(name: String, details: JSONObject, notification: String)
}
//