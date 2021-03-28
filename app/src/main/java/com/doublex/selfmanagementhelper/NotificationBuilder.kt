package com.doublex.selfmanagementhelper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.UiThread
import androidx.core.app.NotificationCompat

internal class NotificationBuilder(private val _context: Context) {

    private val _channelId = BuildConfig.APPLICATION_ID
    private val _appName = _context.resources.getString(R.string.app_name)
    private val _manager = _context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager
    private val _sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    @UiThread
    fun show(notification: String) {
        val builder = NotificationCompat.Builder(_context, BuildConfig.APPLICATION_ID)
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setContentText(notification)
        builder.setContentTitle(_appName)
        builder.setDefaults(Notification.DEFAULT_SOUND)
        builder.setDefaults(Notification.DEFAULT_VIBRATE)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setSound(_sound)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(notification))
        showOnOreoPlus(builder)
        _manager.notify(0, builder.build())
    }

    @UiThread
    private fun showOnOreoPlus(builder: NotificationCompat.Builder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        builder.setChannelId(_channelId)
        val audioAttributes = AudioAttributes.Builder()
        audioAttributes.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        audioAttributes.setUsage(AudioAttributes.USAGE_ALARM)
        val notificationChannel = NotificationChannel(
            _channelId,
            _appName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.setSound(_sound, audioAttributes.build())
        _manager.createNotificationChannel(notificationChannel)
    }

}