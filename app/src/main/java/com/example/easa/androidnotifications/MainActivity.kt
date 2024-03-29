package com.example.easa.androidnotifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.mCategoryMessage
import kotlinx.android.synthetic.main.activity_main.mContent
import kotlinx.android.synthetic.main.activity_main.mDontShowDetailsOnLockScreen
import kotlinx.android.synthetic.main.activity_main.mExpandableText
import kotlinx.android.synthetic.main.activity_main.mProgressbar
import kotlinx.android.synthetic.main.activity_main.mReply
import kotlinx.android.synthetic.main.activity_main.mSimple
import kotlinx.android.synthetic.main.activity_main.mSimpleSound
import kotlinx.android.synthetic.main.activity_main.mSimpleVibrate
import kotlinx.android.synthetic.main.activity_main.mSimpleWithActions
import kotlinx.android.synthetic.main.activity_main.mTitle
import kotlinx.android.synthetic.main.activity_main.mWithButtons

class MainActivity : AppCompatActivity() {

  companion object {
    const val NOTIFICATION_ID = "NOTIFICATION_ID"
    const val BYTE_ARRAY_ARG = "BYTE_ARRAY_ARG"
    const val KEY_TEXT_REPLY = "key_reply_text"
    const val ACTION_REPLY = "action_reply"
  }

  private var notificationId: Int = 200

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mSimple.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), textTitleSmallIcon())
      }
    }
    mSimpleVibrate.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), simpleVibrate())
      }
    }
    mSimpleSound.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), simpleSound())
      }
    }
    mExpandableText.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), style_expandable())
      }
    }
    mSimpleWithActions.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), withAction())
      }
    }
    mWithButtons.setOnClickListener {
      val id = generateUnicNotificationId()
      with(NotificationManagerCompat.from(this)) {
        notify(id, withButtons(id))
      }
    }
    mReply.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        val id = generateUnicNotificationId()
        notify(id, directReplyAction(id))
      }
    }
    mProgressbar.setOnClickListener {
      //read here
      //https://developer.android.com/training/notify-user/build-notification#progressbar
    }
    mCategoryMessage.setOnClickListener {
      with(NotificationManagerCompat.from(this)) {
        notify(generateUnicNotificationId(), withCategoryMessage())
      }
    }
    mDontShowDetailsOnLockScreen.setOnClickListener {
      dontShowDetailsOnLockScreen()
    }

  }

  private fun generateUnicNotificationId(): Int = ++notificationId
  private fun generateUnicRequestCode(): Int = notificationId * 2 + 1

  private fun textTitleSmallIcon(): Notification {
    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
  }

  private fun simpleVibrate(): Notification {
    return NotificationCompat.Builder(this, App.SECONDARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .setVibrate(longArrayOf(0,300,200,300))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build()
  }

  private fun simpleSound(): Notification {
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    return NotificationCompat.Builder(this, App.SECONDARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .setSound(alarmSound)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build()
  }

  /*
  Android uses a some pre-defined system-wide categories to determine whether to disturb the user with a
  given notification when the user has enabled Do Not Disturb mode.

  If your notification falls into one of the pre-defined notification categories defined in NotificationCompat—such
  as CATEGORY_ALARM, CATEGORY_REMINDER, CATEGORY_EVENT, or CATEGORY_CALL—you should declare it as such by passing the appropriate
  category to setCategory().
  */
  private fun withCategoryMessage(): Notification {
    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .build()
  }

  private fun style_expandable(): Notification {
    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("expandable text ${mTitle.text.toString()}")
        .setContentText(mContent.text.toString())
        .setStyle(
            NotificationCompat.BigTextStyle()
                //this will be only expandable if text is more than one line
                .bigText(
                    "big content text akw dk;a w;ld l;aw diaow iod alw" +
                        "a wdlk awlk dlk awlk dlkaw lkd lkwa ldlk awop dpo awpod opaw dpoa" +
                        "aw dop awpod oapw od kla sfjk adskjlf kjads fljk dakls f'lk aslkd' f'lkas" +
                        "akl dfl;kads ljf lk;ads fkl asdlkf ladsk flk adslkf ;lasd flk daslkf " +
                        "klf asd'lkf lkads f" +
                        "k dka w,d klaw ldk awkl dkla wkld klaw dkl"
                )
                //this will replace title set in setContentTitle()
                .setBigContentTitle("big content title")
                //appears in front of title
                .setSummaryText("summary text")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
  }

  private fun withAction(): Notification {
    // Create an explicit intent for an Activity in your app
    val intent = Intent(this, ActionActivity::class.java).apply {
      //            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true) //automatically removes notification when tapped
        .build()
  }

  private fun withButtons(id: Int): Notification {
    val snoozeIntent = Intent(this, GeneralBroadCastReceiver::class.java).apply {
      action = App.ACTION_SHOW_TOAST
      //for some reason only extras that are passed to broadcastReceivers through pending intents that work
      //are bytearrays check stackoverflow
      //pending intents that are made from getActivity() method have similar behavior
      putExtra(NOTIFICATION_ID, id.toString().toByteArray())
    }
    val snoozePendingIntent: PendingIntent =
      PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(snoozePendingIntent)
        .addAction(R.mipmap.ic_launcher, getString(R.string.show_toast), snoozePendingIntent)
        //google suggests we should not have the same pending intents for multiple actions
        //but didn't say why
        .addAction(R.mipmap.ic_launcher, "show another toast", snoozePendingIntent)
        .setAutoCancel(true)
        .build()
  }

  private fun directReplyAction(id: Int): Notification {
    val replyLabel: String = resources.getString(R.string.reply_label)
    val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
        .run {
          setLabel(replyLabel)
          build()
        }
    // Build a PendingIntent for the reply action to trigger.
    val conversationId = generateUnicRequestCode()
    val replyIntent = Intent(this, GeneralBroadCastReceiver::class.java).apply {
      action = ACTION_REPLY
      putExtra(NOTIFICATION_ID, id.toString().toByteArray())

    }
    var replyPendingIntent: PendingIntent =
      PendingIntent.getBroadcast(
          applicationContext,
          conversationId,
          replyIntent,
          PendingIntent.FLAG_UPDATE_CURRENT
      )
    // Create the reply action and add the remote input.
    var action: NotificationCompat.Action =
      NotificationCompat.Action.Builder(
          R.mipmap.ic_launcher,
          "reply", replyPendingIntent
      )
          .addRemoteInput(remoteInput)
          .build()

    // Build the notification and add the action.
    return NotificationCompat.Builder(this, App.PRIMARY_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(mTitle.text.toString())
        .setContentText(mContent.text.toString())
        .addAction(action)
        .build()

  }

  private fun dontShowDetailsOnLockScreen() {
    /*
    Set lock screen visibility
    To control the level of detail visible in the notification from the lock screen, call setVisibility() and specify one of the
    following values:

    VISIBILITY_PUBLIC shows the notification's full content.
    VISIBILITY_SECRET doesn't show any part of this notification on the lock screen.
    VISIBILITY_PRIVATE shows basic information, such as the notification's icon and the content title, but
    hides the notification's full content.

    When VISIBILITY_PRIVATE is set, you can also provide an alternate version of the notification content which hides certain details.
    For example, an SMS app might display a notification that shows You have 3 new text messages, but hides the message contents and
    senders. To provide this alternative notification, first create the alternative notification with NotificationCompat.Builder as usual.
    Then attach the alternative notification to the normal notification with setPublicVersion().
     */
  }

  /*
  posting limits for API 27 +

  Beginning with Android 8.1 (API level 27), apps cannot make a notification sound more than once per second.
  If your app posts multiple notifications in one second, they all appear as expected, but only the first notification per
   second makes a sound.

  However, Android also applies a rate limit when updating a notification. If you post updates to a single notification too
  frequently (many in less than one second), the system might drop some updates.
   */

  /*
  Notification Importance

  https://developer.android.com/guide/topics/ui/notifiers/notifications figure12
  Urgent -> makes sound and pops up on screen
  High -> makes sound
  Medium -> No sound
  Low -> No sound or visual interruption

  All notifications, regardless of importance, appear in non-interruptive system UI locations, such as in the notification drawer
  and as a badge on the launcher icon (though you can modify the appearance of the notification badge).

  On Android 8.0 (API level 26) and above, importance of a notification is determined by the importance of the channel the
  notification was posted to. Users can change the importance of a notification channel in the system settings (figure 12).
  On Android 7.1 (API level 25) and below, importance of each notification is determined by the notification's priority.
   */

  /*
  notification channels

  On devices running Android 7.1 (API level 25) and lower, users can manage notifications on a per-app basis only
  (effectively each app only has one channel on Android 7.1 and lower).

  One app can have multiple notification channels—a separate channel for each type of notification the app issues.
  An app can also create notification channels in response to choices made by users of your app. For example, you may set up separate notification channels for each conversation group created by a user in a messaging app.

  The channel is also where you specify the importance level for your notifications on Android 8.0 and higher.
  So all notifications posted to the same notification channel have the same behavior.
   */

  /*
  heads up notifications :

  Example conditions that might trigger heads-up notifications include the following:
  The user's activity is in fullscreen mode (the app uses fullScreenIntent).
  The notification has high priority and uses ringtones or vibrations on devices running Android 7.1 (API level 25) and lower.
  The notification channel has high importance on devices running Android 8.0 (API level 26) and higher.
   */
}
