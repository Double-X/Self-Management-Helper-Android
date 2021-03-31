package com.doublex.selfmanagementhelper

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.doublex.selfmanagementhelper.widgets.ActivityLogViews
import com.doublex.selfmanagementhelper.widgets.DefinedActivityView
import com.doublex.selfmanagementhelper.widgets.DefinedActivityViews
import com.doublex.selfmanagementhelper.widgets.NewActivityView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*


internal class MainActivity : AppCompatActivity() {

    private val _newActivity by lazy { NewActivityView(this, linear_define_activity) }

    private val _res by lazy { resources }
    private val _config by lazy { _res.configuration }

    private val _sharedPreferences by lazy { getSharedPreferences(packageName, 0) }
    private val _cache by lazy { Cache(_sharedPreferences) }
    private val _definedActivities by lazy { DefinedActivityViews(_cache, linear_activities) }

    private val _definedActivityCallbacks = object : DefinedActivityCallbacks {
        override fun onDelete(definedActivityView: DefinedActivityView) {
            _definedActivities.delete(definedActivityView)
        }
        override fun onRename(newName: String, details: JSONObject, oldName: String) {
            _cache.renameActivity(newName, details, oldName)
        }
        override fun onSave(name: String, details: JSONObject) {
            _cache.saveActivity(name, details)
        }
        override fun onSaveAs(name: String, details: JSONObject) {
            _definedActivities.saveAs(name, details, this@MainActivity, this)
        }
        override fun onStart(name: String, details: JSONObject, notification: String) {
            _activityLogs.start(this@MainActivity, name, details)
            _notification.show(notification)
        }
    }

    private val _notification by lazy { NotificationBuilder(this) }
    private val _activityLogs by lazy {
        ActivityLogViews(_cache, _notification, linear_activity_logs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    override fun onStart() {
        super.onStart()
        onChangeLang(_cache.lang())
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // TODO Fixes the locale when changing screen orientation without redundant locale changes
        changeLocale(_cache.lang())
        //
    }

    @UiThread
    private fun init() {
        setContentView(R.layout.activity_main)
        setTitle(R.string.app_name)
        setLangChoices()
        setListeners()
        changeLang(_cache.lang())
        // Calling them before changeLang would cause duplicated text renderings
        _definedActivities.update(this, _definedActivityCallbacks)
        _activityLogs.update(this)
        //
    }

    @UiThread
    private fun setLangChoices() {
        radio_group_lang.removeAllViews() // It's just to play safe
        for (lang in Cache.Lang.values()) addLangChoice(lang)
    }
    @UiThread
    private fun addLangChoice(lang: Cache.Lang) {
        val radioButton = RadioButton(this)
        // The radio button must be added to the radio group before changing any of its states
        radio_group_lang.addView(radioButton)
        radioButton.isChecked = _cache.lang() == lang
        radioButton.setOnCheckedChangeListener { _, b -> if (b) onChangeLang(lang) }
        radioButton.setText(lang.langR)
        // Otherwise the radio group won't properly uncheck all the other radio buttons inside
    }
    @UiThread
    private fun onChangeLang(lang: Cache.Lang) { if (_cache.lang() !== lang) changeLang(lang) }
    @UiThread
    private fun changeLang(lang: Cache.Lang) {
        changeLocale(lang)
        onConfigurationChanged(_config)
        redrawTexts()
    }
    @UiThread
    private fun changeLocale(lang: Cache.Lang) {
        _cache.changeLang(lang)
        _config.locale = Locale(lang.name)
        _res.updateConfiguration(_config, _res.displayMetrics)
    }
    @UiThread
    private fun redrawTexts() {
        setTitle(R.string.app_name)
        button_clear_all_data.setText(R.string.clear_all_data)
        button_define_activity.setText(R.string.define_activity)
        _newActivity.redrawTexts()
        button_clear_define_activity.setText(R.string.clear)
        button_confirm_define_activity.setText(R.string.confirm)
        // Preserves the visibility state while still redrawing the button texts
        toggleActivityVisibilities()
        toggleActivityVisibilities()
        toggleActivityLogVisibilities()
        toggleActivityLogVisibilities()
        //
        //
        // Simplifies the codes at the cost of redundant calls upon activity start
        _definedActivities.redrawTexts()
        _activityLogs.redrawTexts()
        //
    }

    @UiThread
    private fun setListeners() {
        button_clear_all_data.setOnClickListener { onClearAllData() }
        button_define_activity.setOnClickListener { toggleNewActivityVisibility() }
        button_clear_define_activity.setOnClickListener { _newActivity.clear() }
        button_confirm_define_activity.setOnClickListener { onDefineActivity() }
        button_activities.setOnClickListener { toggleActivityVisibilities() }
        button_activity_logs.setOnClickListener { toggleActivityLogVisibilities() }
    }

    @UiThread
    private fun onClearAllData() {
        showConfirmDialog(this, R.string.on_clear_all_data) { clearAllData() }
    }
    @UiThread
    private fun clearAllData() {
        _cache.clearAll()
        refresh()
    }
    @UiThread
    private fun refresh() {
        overridePendingTransition(0, 0) // It's just to play safe
        recreate()
        overridePendingTransition(0, 0) // It's just to play safe
    }

    @UiThread
    private fun onDefineActivity() {
        if (_newActivity.name().isEmpty()) return onEmptyNewActivityName()
        _definedActivities.add(_newActivity, this, _definedActivityCallbacks)
    }
    // It's just accidentally the same as onEmptyEditText in DialogBuilder
    @UiThread
    private fun onEmptyNewActivityName() {
        val format = _res.getString(R.string.on_empty_edit_text)
        val msg = String.format(format, _res.getString(R.string.activity_name))
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    @UiThread
    private fun toggleActivityVisibilities() {
        when (linear_activities.visibility) {
            View.GONE -> showActivities()
            View.VISIBLE -> hideActivities()
        }
    }
    @UiThread
    private fun showActivities() {
        button_activities.setText(R.string.hide_activities)
        linear_activities.visibility = View.VISIBLE
    }
    @UiThread
    private fun hideActivities() {
        button_activities.setText(R.string.show_activities)
        linear_activities.visibility = View.GONE
    }

    @UiThread
    private fun toggleActivityLogVisibilities() {
        when (linear_activity_logs.visibility) {
            View.GONE -> showActivityLogs()
            View.VISIBLE -> hideActivityLogs()
        }
    }
    @UiThread
    private fun showActivityLogs() {
        button_activity_logs.setText(R.string.hide_activity_logs)
        linear_activity_logs.visibility = View.VISIBLE
    }
    @UiThread
    private fun hideActivityLogs() {
        button_activity_logs.setText(R.string.show_activity_logs)
        linear_activity_logs.visibility = View.GONE
    }

    @UiThread
    private fun toggleNewActivityVisibility() {
        when (linear_define_activity.visibility) {
            View.GONE -> showNewActivity()
            View.VISIBLE -> hideNewActivity()
        }
    }
    @UiThread
    private fun showNewActivity() {
        image_define_activity.setImageResource(android.R.drawable.arrow_up_float)
        linear_define_activity.visibility = View.VISIBLE
    }
    @UiThread
    private fun hideNewActivity() {
        image_define_activity.setImageResource(android.R.drawable.arrow_down_float)
        linear_define_activity.visibility = View.GONE
    }

}