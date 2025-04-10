package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var timerBinder: TimerService.TimerBinder? = null

    val timerHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val time = msg.what
            findViewById<TextView>(R.id.textView).text = time.toString()
        }
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val serviceConnection = object : ServiceConnection {
             override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                 timerBinder = p1 as TimerService.TimerBinder
                 timerBinder?.setHandler(timerHandler)
             }

             override fun onServiceDisconnected(p0: ComponentName?) {
                 timerBinder = null
             }


         }

         bindService(
             Intent(this, TimerService::class.java),
             serviceConnection,
             BIND_AUTO_CREATE
         )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            startTimer()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            stopTimer()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.startButton -> {}
            R.id.stopButton -> {}

            else -> return false
        }
        // return super.onOptionsItemSelected(item)
        return true // return true because we have consumed the function
    }


    fun startTimer() {
        if (timerBinder?.isRunning == true) {
            timerBinder!!.pause()
        } else {
            timerBinder?.start(25)
        }
    }

    fun stopTimer() {
        timerBinder?.stop()
    }
}