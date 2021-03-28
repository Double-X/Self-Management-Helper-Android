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

    fun clearAll() {
        _editor.clear()
        _editor.commit()
    }

    fun activities() = JSONObject(_sharedPreferences.getString(ACTIVITIES, EMPTY_JSON_OBJECT)!!)
    fun renameActivity(newName: String, details: JSONObject, oldName: String) {
        saveActivity(newName, details)
        deleteActivity(oldName)
    }
    fun deleteActivity(name: String) {
        val activities = activities()
        activities.remove(name)
        _editor.putString(ACTIVITIES, activities.toString())
        _editor.commit()
    }
    fun saveActivity(name: String, details: JSONObject) {
        val activities = activities()
        activities.put(name, details)
        _editor.putString(ACTIVITIES, activities.toString())
        _editor.commit()
    }

    fun activityLogs(): JSONObject {
        return JSONObject(_sharedPreferences.getString(ACTIVITY_LOGS, EMPTY_JSON_OBJECT)!!)
    }
    fun deleteActivityLog(activityName: String, startTime: Long) {
        val logs = activityLogs()
        if (!logs.has(activityName)) return
        logs.getJSONObject(activityName).remove(startTime.toString())
        _editor.putString(ACTIVITY_LOGS, logs.toString())
        _editor.commit()
    }
    fun saveActivityLog(activityName: String, startTime: Long, logDetails: JSONObject) {
        val logs = activityLogs()
        if (!logs.has(activityName)) logs.put(activityName, JSONObject())
        logs.getJSONObject(activityName).put(startTime.toString(), logDetails)
        _editor.putString(ACTIVITY_LOGS, logs.toString())
        _editor.commit()
    }

    fun changeLang(lang: Lang) {
        _editor.putInt(LANG, lang.langR)
        _editor.commit()
    }
    fun lang(): Lang {
        val langR = _sharedPreferences.getInt(LANG, Lang.EN.langR)
        return Lang.values().firstOrNull { lang -> lang.langR == langR } ?: Lang.EN
    }

}