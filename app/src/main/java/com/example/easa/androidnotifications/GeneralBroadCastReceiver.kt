package com.example.easa.androidnotifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.util.Log
import android.widget.Toast

class GeneralBroadCastReceiver : BroadcastReceiver() {
  override fun onReceive(
    context: Context?,
    intent: Intent?
  ) {
    Log.d("my_app", "GeneralBoardCastReceiver onReceive is called")
    intent?.action?.let { action ->
      when (action) {
        App.ACTION_SHOW_TOAST -> {
          context?.let {
            Log.d("my_app", "showing notification")
            Toast.makeText(it, "showing simple toast", Toast.LENGTH_LONG)
                .show()
            dismissMessage(context, intent)
          }
        }
        MainActivity.ACTION_REPLY -> {
          Log.d("my_app", "processing reply msg")
          context?.let { cnx ->
            val msg = RemoteInput.getResultsFromIntent(intent)
                ?.getCharSequence(MainActivity.KEY_TEXT_REPLY)
            Toast.makeText(cnx, msg, Toast.LENGTH_LONG)
                .show()
            dismissMessage(context, intent)
          }
        }
        else -> {
        }
      }
    }
  }

  private fun dismissMessage(
    context: Context,
    intent: Intent
  ) {
    with(NotificationManagerCompat.from(context)) {
      try {
        val byteArray = intent.getByteArrayExtra(MainActivity.NOTIFICATION_ID)
        val id = String(byteArray).toInt()
        Log.d("my_app", "notification id is  -> $id ")
        cancel(id)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

}