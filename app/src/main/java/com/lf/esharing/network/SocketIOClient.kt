package com.lf.esharing.network

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import com.lf.esharing.database.user.UserViewModel
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.lang.Exception
import java.net.URISyntaxException

// TODO: should change this to builder pattern
//@WorkerThread
object SocketIOClient {

    private var INSTANCE: Socket? = null

    const val ONLINE = "USER_ONLINE_CHANNEL"
    const val USERNAME = "USERNAME"
    const val JOIN_HOUSEHOLD_REQUEST = "JOIN_HOUSEHOLD_REQUEST"
    const val JOIN_HOUSEHOLD_REQUEST_RESPONSE = "JOIN_HOUSEHOLD_REQUEST_RESPONSE"
    const val REPLY = "REPLY"
    const val FROM_USER = "FROM_USER"
    const val FROM_OWNER = "FROM_OWNER"
    const val TO_OWNER = "TO_OWNER"
    const val USER_DISCONNECT = "USER_DISCONNECT"
    const val TO_USER = "TO_USER"
    const val MESSAGE = "MESSAGE"

    fun initInstance(): Socket? {
        if (INSTANCE == null) {
            synchronized(this) {
                try {
                    INSTANCE = IO.socket(ClientBuilder.baseUrl)
                } catch (e: URISyntaxException) {
                    println("Invalid url format: " + e.message)
                    return null
                } catch (e: Exception) {
                    println("Can not connect to endpoint service: " + e.message)
                    return null
                }

            }
        }
        return INSTANCE
    }

    fun connect(username: String): Boolean {
        if (INSTANCE == null)
            return false
        try {
            INSTANCE?.once(Socket.EVENT_CONNECT, Emitter.Listener {
                INSTANCE?.emit(ONLINE, username)
            })

            INSTANCE?.connect()
        } catch (e: Exception) {
            println("Can not connect to endpoint service: " + e.message)
            return false
        }

        return true
    }

    fun requestJoinHousehold(context: Context, toOwner: String, fromUser: String): Boolean {
        if (INSTANCE == null)
            return false

        val jObject = JSONObject()
        jObject.put(FROM_USER, fromUser)
        jObject.put(TO_OWNER, toOwner)
        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST, jObject)

        INSTANCE?.once(JOIN_HOUSEHOLD_REQUEST_RESPONSE, Emitter.Listener {
            val data = it[0] as String
            val jsonObject = JSONObject(data)
            val user = jsonObject.getString(TO_USER)
            val fromOwner = jsonObject.getString(FROM_OWNER)
            val message = jsonObject.getString(MESSAGE)
            Toast.makeText(context, "Reply from $fromOwner to your request: $message", Toast.LENGTH_SHORT).show()
        })
        return true
    }

    fun disconnect() {
        INSTANCE?.emit(USER_DISCONNECT, UserViewModel.username)
        INSTANCE?.disconnect()
    }

    fun rejectJoinHouseholdRequest(owner: String, people: String, message: String) {
        val jsonOb = JSONObject()
        jsonOb.put(FROM_OWNER, owner)
        jsonOb.put(TO_USER, people)
        jsonOb.put(MESSAGE, message)
        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST_RESPONSE, )
    }
}