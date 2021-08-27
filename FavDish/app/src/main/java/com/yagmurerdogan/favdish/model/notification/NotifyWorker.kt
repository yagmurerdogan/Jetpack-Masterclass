package com.yagmurerdogan.favdish.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yagmurerdogan.favdish.R
import com.yagmurerdogan.favdish.utils.Constants
import com.yagmurerdogan.favdish.view.activities.MainActivity

class NotifyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        sendNotification()
        return success()
    }

    private fun sendNotification() {

        val notification_id = 0

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID, notification_id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_vector_logo)
        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null) // The null is passed to avoid the duplication of image when the notification is en-large from notification tray.

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification =

            NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL)
                // Set the Notification Title
                .setContentTitle(titleNotification)
                // Set the Notification SubTitle
                .setContentText(subtitleNotification)
                // Set the small icon also you can say as notification icon that we have generated.
                .setSmallIcon(R.drawable.ic_stat_notification)
                // Set the Large icon
                .setLargeIcon(bitmap)
                // Set the default notification options that will be used.
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Supply a PendingIntent to send when the notification is clicked.
                .setContentIntent(pendingIntent)
                // Add a rich notification style to be applied at build time.
                .setStyle(bigPicStyle)
                // Setting this flag will make it so the notification is automatically canceled when the user clicks it in the panel.
                .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(Constants.NOTIFICATION_CHANNEL)

            // Setup the Ringtone for Notification.
            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL,
                    Constants.NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notification_id, notification.build())
    }

    private fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
        // Get the Drawable Vector Image
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}