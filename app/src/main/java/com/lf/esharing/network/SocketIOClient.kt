package com.lf.esharing.network

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lf.esharing.database.user.UserViewModel
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.lang.Exception
import java.net.URISyntaxException

// TODO: should change this to builder pattern
@WorkerThread
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
    const val JOIN_HOUSEHOLD_REQUEST_RESPONSE_NOT_EXIST = "JOIN_HOUSEHOLD_REQUEST_RESPONSE_NOT_EXIST"
    const val BEING_REMOVED_FROM_HOUSEHOLD = "BEING_REMOVED_FROM_HOUSEHOLD"
    const val REMOVED_MEMBER_FROM_HOUSEHOLD = "REMOVED_MEMBER_FROM_HOUSEHOLD"

    fun initInstance(): Socket? {
        if (INSTANCE == null) {
            synchronized(this) {
                try {
                    INSTANCE = IO.socket(ClientBuilder.baseUrl)
                } catch (e: URISyntaxException) {
                    println("Invalid url format: " + e.message)
                    return null
                }

            }
        }
        return INSTANCE
    }

    fun connect(username: String): Boolean {
        try {
            INSTANCE?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                INSTANCE?.emit(ONLINE, username)
            })

            INSTANCE?.connect()
        } catch (e: Exception) {
            println("Can not connect to endpoint service: " + e.message)
            return false
        }

        return true
    }

    fun requestJoinHousehold(context: Context, toOwner: String, fromUser: String): LiveData<JSONObject> {

        val jObject = JSONObject()
        jObject.put(FROM_USER, fromUser)
        jObject.put(TO_OWNER, toOwner)
        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST, jObject)

        val result = MutableLiveData<JSONObject>()

        INSTANCE?.on(JOIN_HOUSEHOLD_REQUEST_RESPONSE_NOT_EXIST, Emitter.Listener {
            val jsonObject = it[0] as JSONObject
            result.postValue(jsonObject)
        })

        INSTANCE?.on(JOIN_HOUSEHOLD_REQUEST_RESPONSE, Emitter.Listener {
            val jsonObject = it[0] as JSONObject
            result.postValue(jsonObject)
        })
        return result
    }

    fun disconnect() {
        INSTANCE?.emit(USER_DISCONNECT, UserViewModel.username)
        INSTANCE?.disconnect()
    }

    fun responseJoinHouseholdRequest(owner: String, people: String, message: String) {
        val jsonOb = JSONObject()
        jsonOb.put(FROM_OWNER, owner)
        jsonOb.put(TO_USER, people)
        jsonOb.put(MESSAGE, message)
        INSTANCE?.emit(JOIN_HOUSEHOLD_REQUEST_RESPONSE, jsonOb)
    }

    fun registerOnJoinHouseholdRequest(): LiveData<JSONObject> {
        val result = MutableLiveData<JSONObject>()
        INSTANCE?.on(JOIN_HOUSEHOLD_REQUEST, Emitter.Listener {
            val jsonObject = it[0] as JSONObject
            result.postValue(jsonObject)
        })
        return result
    }

    fun registerOnBeingRemovedFromHouseHold():LiveData<JSONObject> {
        val result = MutableLiveData<JSONObject>()
        INSTANCE?.on(BEING_REMOVED_FROM_HOUSEHOLD, Emitter.Listener {
            val jsonObject = it[0] as JSONObject
            result.postValue(jsonObject)
        })
        return result
    }

    fun notifyRemovedMember(owner: String, user: String, message: String) {
        val jsonOb = JSONObject()
        jsonOb.put(FROM_OWNER, owner)
        jsonOb.put(TO_USER, user)
        jsonOb.put(MESSAGE, message)
        INSTANCE?.emit(REMOVED_MEMBER_FROM_HOUSEHOLD, jsonOb)
    }
}