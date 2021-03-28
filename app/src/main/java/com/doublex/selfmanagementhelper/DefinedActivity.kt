package com.doublex.selfmanagementhelper

import org.json.JSONObject

// It's created to avoid circular reference between DefinedActivityView and DefinedActivityViews
internal interface DefinedActivity {
    fun onDelete(name: String)
    fun onRename(newName: String, details: JSONObject, oldName: String)
    fun onSave(name: String, details: JSONObject)
    fun onSaveAs(name: String, details: JSONObject)
    fun onStart(name: String, details: JSONObject, notification: String)
}