package space.lobanov.musicapplication

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import space.lobanov.musicapplication.Constants.constants.CHANNEL_ID
import space.lobanov.musicapplication.Constants.constants.MUSIC_NOTIFICATION_ID


class MyServices : Service(){
    val ACTION_PLAY = "ACTION_PLAY"
     var musicPlayer: MediaPlayer? = null
    private val song:ArrayList<Int> =ArrayList()
    var mBinder: IBinder = LocalBinder()
    var lengthMusic:Int?=null
    var position:Int = 0
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        initMusic()
       // createNotificationChannel()

    }

    inner class LocalBinder : Binder() {
        fun getServerInstance(): MyServices? {
            return this@MyServices
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY

    }

  /* private fun showNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)
        val prevIntent = Intent(this,
                NotificationReceiver::class.java)
        val prevPendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setContentText("Music Player")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(MUSIC_NOTIFICATION_ID, notification)
    }*/


 private fun showNotification() {

     val notificationIntent = Intent(this, MainActivity::class.java)
     val pendingIntent = PendingIntent.getActivity(this, 0,
             notificationIntent, 0)
    // val prevIntent = Intent(this,
           //  NotificationReceiver::class.java)//lracnelu ban
     //val prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
        //     PendingIntent.FLAG_UPDATE_CURRENT)
     val playIntent = Intent(this, MyServices::class.java)
     // Make head-up notification.
    // builder.setFullScreenIntent(pendingIntent, true);
     playIntent.action = ACTION_PLAY
   //  var largeIconBitmap =  BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_play_circle_filled_24);
    // builder.setLargeIcon(largeIconBitmap)
  //   builder.setSmallIcon(R.drawable.ic_baseline_play_circle_filled_24)

     val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
     val playAction = NotificationCompat.Action(R.drawable.ic_baseline_play_circle_filled_24,
             "", pendingPlayIntent)

     builder.addAction(playAction)
     var notification: Notification = builder
           //  .Builder(this, CHANNEL_ID)
             .setContentText("Music Player")
             .setSmallIcon(R.drawable.ic_baseline_play_circle_filled_24)
             .setContentIntent(pendingIntent)
             .build()

    /* val notificationManager = NotificationManagerCompat.from(this)
     notificationManager.notify(1, builder.build())*/

     startForeground(MUSIC_NOTIFICATION_ID, notification)

 }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID, "My service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
      }
    }

    private fun initMusic() {
       // musicPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        musicPlayer = MediaPlayer.create(this, R.raw.song1)
       //  val song:ArrayList<Int> =ArrayList()
         song.add(0, R.raw.song1)
        song.add(1, R.raw.song2)

       // musicPlayer = MediaPlayer.create(this,R.raw.song2)
        musicPlayer?.isLooping = true
        musicPlayer?.setVolume(100F, 100F)
    }



    fun playPauseMedia(){
        if (musicPlayer?.isPlaying == true) {
               musicPlayer?.pause()
           lengthMusic = musicPlayer?.currentPosition
        } else {
            showNotification()

               musicPlayer?.start()
        }

    }
    fun nextBtn() {
        if (position < song.size) {
            position++
        } else {
            position = 0
        }
        musicPlayer?.stop()
        musicPlayer?.release()
        musicPlayer = MediaPlayer.create(applicationContext, song[position])
        musicPlayer?.start()

    }
    fun prevBtn(){
        if (position > 0 ) {
            position --
        } else {
            song.size - 1
        }
        musicPlayer?.stop()
        musicPlayer?.release()
        musicPlayer = MediaPlayer.create(applicationContext, song.get(position))
        musicPlayer?.start()
    }
}