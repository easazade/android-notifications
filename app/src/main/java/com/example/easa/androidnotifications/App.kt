package com.example.easa.androidnotifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build

class App : Application() {

  companion object {
    const val PRIMARY_CHANNEL_ID = "nn app_channel_id"
    const val SECONDARY_CHANNEL_ID = "ss app_channel_id"
    const val ACTION_SHOW_TOAST = "action_show_toast"
  }

  override fun onCreate() {
    super.onCreate()
    //it is completely safe to call this method several time
    //but there is no point in it
    //google recommend to call it as soon as app is created .
    //we can also add more channels anytime at run time
    createPrimaryAppNotificationChannel()
    createSecondaryChannelWithSoundAndVibration()
    registerReceiver(GeneralBroadCastReceiver(), IntentFilter(ACTION_SHOW_TOAST))
  }

  private fun createPrimaryAppNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = getString(R.string.app_primary_channel_name)
      val descriptionText = getString(R.string.app_primary_channel_description)
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(PRIMARY_CHANNEL_ID, name, importance).apply {
        description = descriptionText
      }
      // Register the channel with the system
      val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  private fun createSecondaryChannelWithSoundAndVibration() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      val name = getString(R.string.app_secondary_channel_name)
      val descriptionText = getString(R.string.app_secondary_channel_description)
      val importance = NotificationManager.IMPORTANCE_MAX
      val channel = NotificationChannel(SECONDARY_CHANNEL_ID, name, importance).apply {
        description = descriptionText
        enableVibration(true)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        setSound(alarmSound, audioAttributes)
        vibrationPattern = longArrayOf(0,300,200,300)
      }
      // Register the channel with the system
      val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

}