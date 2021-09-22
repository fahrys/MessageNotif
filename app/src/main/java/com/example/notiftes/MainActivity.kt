package com.example.notiftes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref" , Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC).addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w(TAG , "Token Failed" , task.exception)
                    return@OnCompleteListener
                }
            })

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnsend.setOnClickListener {
            val title = ettitle.text.toString()
            val message = etmessage.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty()) {
                PushNotification(
                    NotificationData(title,message) ,
                    TOPIC
                ).also {
                    sendNotification(it)
                }

            }
            ettitle.text.clear()
            etmessage.text.clear()
        }


    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG , "Response: ${Gson().toJson(response)}")
            }else {
                Log.e(TAG , response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}