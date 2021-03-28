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

    private val _editor: SharedPreferences.Editor = _sharedPreferences.edit()

    fun clearAll() {
        _editor.clear()
        _editor.commit()
    }

    fun activities(): JSONObject {
        return JSONObject(_sharedPreferences.getString(ACTIVITIES, EMPTY_JSON_OBJECT)!!)
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

    fun activityLogs(activityName: String): JSONObject {
        val logs = allActivityLogs()
        return if (logs.has(activityName)) logs.getJSONObject(activityName) else JSONObject()
    }
    fun allActivityLogs(): JSONObject {
        return JSONObject(_sharedPreferences.getString(ACTIVITY_LOGS, EMPTY_JSON_OBJECT)!!)
    }
    fun saveActivityLog(activityName: String, logCreationTime: Int, logDetails: JSONObject) {
        val logs = allActivityLogs()
        if (!logs.has(activityName)) logs.put(activityName, JSONObject())
        logs.getJSONObject(activityName).put(logCreationTime.toString(), logDetails)
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