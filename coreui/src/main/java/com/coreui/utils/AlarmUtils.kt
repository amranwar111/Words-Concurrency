package com.coreui.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.os.SystemClock
import java.util.*
import java.text.SimpleDateFormat


class AlarmUtils {
    companion object {
        fun getInstanc():AlarmUtils{
            return AlarmUtils()
        }
    }
    fun alarmManager(context: Context): AlarmManager {
        return context.getSystemService(ALARM_SERVICE) as AlarmManager
    }

    fun fireRepeatePeriodicAlarm(context: Context, alarmIntent: PendingIntent, interval: Long) {
        // Hopefully your alarm will have a lower frequency than this!
        alarmManager(context).setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval,
                interval,
                alarmIntent
        )
    }

    fun firePeriodicAlarm(context: Context, alarmIntent: PendingIntent, interval: Long) {
        // Hopefully your alarm will have a lower frequency than this!
        alarmManager(context).set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval,
                alarmIntent
        )
    }

    fun fireExactTimeDaily(context: Context, alarmIntent: PendingIntent, time:String) {
        // Set the alarm to start at approximately 2:00 p.m.
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 14)
//        }
        val hour = time.split(":")[0]
        val minute = time.split(":")[1].split(" ")[0]
        val am= time.split(":")[1].split(" ")[1] == "AM"
//        val df = SimpleDateFormat("hh:mm a")
//        val d1 = df.parse(time)
        val calendar = Calendar.getInstance().apply {
            //            setTime(d1)
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR, hour.toInt())
            set(Calendar.MINUTE, minute.toInt())
            set(Calendar.AM_PM, if(am)Calendar.AM else Calendar.PM)
        }
// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmManager(context).setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        )
    }
    fun fireExactTime(context: Context, alarmIntent: PendingIntent?, time:String,interval: Long) {
        // Set the alarm to start at approximately 2:00 p.m.
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 14)
//        }
        val hour = time.split(":")[0]
        val minute = time.split(":")[1].split(" ")[0]
        val am= time.split(":")[1].split(" ")[1] == "AM"
//        val df = SimpleDateFormat("hh:mm a")
//        val d1 = df.parse(time)
        val calendar = Calendar.getInstance().apply {
//            setTime(d1)
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR, hour.toInt())
            set(Calendar.MINUTE, minute.toInt())
            set(Calendar.AM_PM, if(am)Calendar.AM else Calendar.PM)
        }
// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmManager(context).setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                interval,
                alarmIntent
        )
    }

}