package com.example.android.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnectivity(context : Context):Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val actionNetwork : NetworkInfo? = connectivityManager.activeNetworkInfo

        if(actionNetwork?.isConnected != null){
            return actionNetwork.isConnected
        }else{
            return false
        }
    }
}