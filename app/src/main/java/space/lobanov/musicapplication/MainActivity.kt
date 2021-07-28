package space.lobanov.musicapplication

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import space.lobanov.musicapplication.MyServices.LocalBinder
import space.lobanov.musicapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var bindingIntent:Intent? = null
    var mBounded = false
    var foregroundService: MyServices? = null
    var mConnection: ServiceConnection? = null
   // var position:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingIntent = Intent(this,MyServices::class.java)

        binding.playBtn.setOnClickListener {
            startPauseMedia()

        }
        binding.nextBtn.setOnClickListener{
            foregroundService?.nextBtn()
            Toast.makeText(this, "next btn", Toast.LENGTH_SHORT).show()

        }
        binding.prevBtn.setOnClickListener{
            foregroundService?.prevBtn()
            Toast.makeText(this, " prev btn", Toast.LENGTH_SHORT).show()
        }
            mConnection = object : ServiceConnection {
                override fun onServiceDisconnected(name: ComponentName) {
                    Toast.makeText(this@MainActivity, "Service is disconnected", Toast.LENGTH_SHORT)
                            .show()
                    mBounded = false
                    foregroundService = null
                }

                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    Toast.makeText(this@MainActivity, "Service is connected", Toast.LENGTH_SHORT).show()
                    mBounded = true
                    val mLocalBinder = service as MyServices.LocalBinder
                    foregroundService = mLocalBinder.getServerInstance()
                }
            }
        val startIntent = Intent(this, MyServices::class.java)
        bindService(startIntent, mConnection as ServiceConnection, Context.BIND_AUTO_CREATE)

    }

    private fun startPauseMedia() {
        foregroundService?.let {
            it.playPauseMedia()
        }

    }

    private fun isMyServiceRunning(mClass: Class<MyServices>): Boolean {
        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE)
                as ActivityManager

        for (service: ActivityManager.RunningServiceInfo in
        manager.getRunningServices(Integer.MAX_VALUE))

            if (mClass.name.equals(service.service.className)) {
                return true
            }
        return false
    }

}