package space.lobanov.musicapplication

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import space.lobanov.musicapplication.Constants.constants.CHANNEL_ID
import space.lobanov.musicapplication.Constants.constants.MUSIC_NOTIFICATION_ID
import space.lobanov.musicapplication.NotificationReceiver.Companion.ACTION_PREV


class MyServices : Service(){
    val ACTION_PLAY = "ACTION_PLAY"
     var musicPlayer: MediaPlayer? = null
    private val song:ArrayList<Int> =ArrayList()
    var mBinder: IBinder = LocalBinder()
    var lengthMusic:Int?=null
    var position:Int = 0

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        initMusic()
        createNotificationChannel()

    }

    inner class LocalBinder : Binder() {
        fun getServerInstance(): MyServices? {
            return this@MyServices
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY

    }



 private fun showNotification() {

     val notificationIntent = Intent(this, MainActivity::class.java)
     val pendingIntent = PendingIntent.getActivity(this, 0,
             notificationIntent, 0)

     val prevBtnClickIntent = Intent(this,NotificationReceiver::class.java)
     prevBtnClickIntent.action = ACTION_PREV
     val prevClickPendingIntent = PendingIntent.getBroadcast(this,0,prevBtnClickIntent,0)

     val playBtnClickIntent = Intent(this,NotificationReceiver::class.java)
     playBtnClickIntent.action = ACTION_PLAY
     val playClickPendingIntent = PendingIntent.getBroadcast(this,0,playBtnClickIntent,0)

     //TODO add nextAction button for ACTION_NEXT


     val collapsedView = RemoteViews(
         packageName,
         R.layout.notif_collapsed
     )
     val expandedView = RemoteViews(
         packageName,
         R.layout.notification_expanded
     )

     expandedView.setImageViewResource(R.id.prev_img, R.drawable.ic_baseline_skip_previous_24)
     expandedView.setOnClickPendingIntent(R.id.prev_img, prevClickPendingIntent)

     expandedView.setImageViewResource(R.id.play_img, R.drawable.ic_baseline_play_circle_filled_24)
     expandedView.setOnClickPendingIntent(R.id.play_img, playClickPendingIntent)
     //TODO set click pendings nextAction button for ACTION_NEXT

     val nt2 : Notification = NotificationCompat.Builder(this,CHANNEL_ID)
         .setSmallIcon(R.drawable.ic_baseline_skip_next_24)
         .setContentIntent(pendingIntent)
         .setCustomContentView(expandedView)
         //.setCustomBigContentView(expandedView)
         .build()

     startForeground(MUSIC_NOTIFICATION_ID, nt2)

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
        musicPlayer = MediaPlayer.create(this, R.raw.song1)
         song.add(0, R.raw.song1)
        song.add(1, R.raw.song2)

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