package com.doublex.selfmanagementhelper

import android.content.SharedPreferences
import org.json.JSONObject

internal class Cache(private val _sharedPreferences: SharedPreferences) {

    enum class Lang(val langR: Int) {
        EN(R.string.lang_en),
        ZH(R.string.lang_zh),
        CN(R.string.lang_cn)
    }

    private companion object {

        const val ACTIVITIES = "Activities"
        const val ACTIVITY_LOGS = "Activity Logs"
        const val LANG = "Lang"

        val EMPTY_JSON_OBJECT = JSONObject().toString()

    }

    private val _editor = _sharedPreferences.edit()

    fun changeLang(lang: Lang) {
        _editor.putInt(LANG, lang.langR)
        _editor.commit()
    }
    fun lang(): Lang {
        val langR = _sharedPreferences.getInt(LANG, Lang.EN.langR)
        return Lang.values().firstOrNull { lang -> lang.langR == langR } ?: Lang.EN
    }

    fun clearAll() {
        _editor.clear()
        _editor.commit()
    }

    fun activities() = JSONObject(_sharedPreferences.getString(ACTIVITIES, EMPTY_JSON_OBJECT)!!)
    fun renameActivity(newName: String, details: JSONObject, oldName: String) {
        deleteActivity(oldName)
        saveActivity(newName, details)
    }
    fun deleteActivity(name: String) {
        val activities = activities()
        activities.remove(name)
        putString(ACTIVITIES, activities.toString())
    }
    fun saveActivity(name: String, details: JSONObject) {
        val activities = activities()
        activities.put(name, details)
        putString(ACTIVITIES, activities.toString())
    }

    fun activityLogs(): JSONObject {
        return JSONObject(_sharedPreferences.getString(ACTIVITY_LOGS, EMPTY_JSON_OBJECT)!!)
    }
    fun deleteActivityLog(name: String, startTime: Long) {
        val logs = activityLogs()
        if (!logs.has(name)) return
        logs.getJSONObject(name).remove(startTime.toString())
        putString(ACTIVITY_LOGS, logs.toString())
    }
    fun saveActivityLog(name: String, startTime: Long, details: JSONObject) {
        val logs = activityLogs()
        if (!logs.has(name)) logs.put(name, JSONObject())
        logs.getJSONObject(name).put(startTime.toString(), details)
        putString(ACTIVITY_LOGS, logs.toString())
    }

    private fun putString(key: String, value: String) {
        _editor.putString(key, value)
        _editor.commit()
    }

}