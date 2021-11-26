package com.lf.esharing.network

import android.content.Context
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.lang.Exception
import java.net.URISyntaxException

// TODO: should change this to builder pattern
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

    fun initInstance(): Socket? {
        if (INSTANCE == null) {
            synchronized(this) {
                try {
                    INSTANCE = IO.socket(ClientBuilder.baseUrl)
                    INSTANCE?.connect()
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
            // catch CONNECTED event
            INSTANCE?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                println("socket connected with username: $username")
                // subscribe to online list
//                val jObject: JSONObject = JSONObject("")
//                jObject.put(USERNAME, username)
//                INSTANCE?.emit(ONLINE, jObject)
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

//        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST, "{\"$FROM_USER\":\"$fromUser\", \"$TO_OWNER\":\"$toOwner\"}")
        val jObject: JSONObject = JSONObject()
        jObject.put(FROM_USER, fromUser)
        jObject.put(TO_OWNER, toOwner)
        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST, jObject)
//        INSTANCE?.once(JOIN_HOUSEHOLD_REQUEST_RESPONSE, Emitter.Listener {
//            val data = it[0] as String
//            val jsonObject = JSONObject(data)
//            val reply = jsonObject.getString(REPLY)
//            val fromOwner = jsonObject.getString(FROM_OWNER)
//            Toast.makeText(context, "Your request was $reply from $fromOwner", Toast.LENGTH_SHORT).show()
//        })
        return true
    }

    fun disconnect() {
        INSTANCE?.disconnect()
    }
}