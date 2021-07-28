package space.lobanov.musicapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import space.lobanov.musicapplication.MyService2
import java.util.*

class MyService2 : Service() {
    val LOG_TAG = "myLogs"
    var binder: MyBinder = MyBinder()
    var timer: Timer? = null
    var tTask: TimerTask? = null
    var interval: Long = 1000
    override fun onCreate() {
        super.onCreate()
        timer = Timer()
        schedule()
    }

    fun schedule() {
        if (tTask != null) tTask!!.cancel()
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {}
            }
            timer!!.schedule(tTask, 1000, interval)
        }
    }

    fun upInterval(gap: Long): Long {
        interval = interval + gap
        schedule()
        return interval
    }

    fun downInterval(gap: Long): Long {
        interval = interval - gap
        if (interval < 0) interval = 0
        schedule()
        return interval
    }

    override fun onBind(arg0: Intent): IBinder? {
        return binder
    }

    inner class MyBinder : Binder() {
        val service: MyService2
            get() = this@MyService2
    }
}