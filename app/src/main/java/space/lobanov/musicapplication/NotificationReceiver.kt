package space.lobanov.musicapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    companion object{
        val  ACTION_PREV = "PREVIOUS"
        val ACTION_NEXT= "NEXT"
        val ACTION_PLAY = "PLAY"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action!= null){
            when(intent.action){
                ACTION_PLAY-> {
                    Toast.makeText(context,"Play",Toast.LENGTH_SHORT).show()
                }
                ACTION_NEXT->{
                    Toast.makeText(context,"Next",Toast.LENGTH_SHORT).show()
                }
                ACTION_PREV->{
                    Toast.makeText(context,"Prev",Toast.LENGTH_SHORT).show()
                }

            }
            }
        }

    }
