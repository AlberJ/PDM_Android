package com.example.alber.controlefinanceiro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast

class EmailReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("aki", "Entrou no onReceive.")

        var conex:Boolean = pegaconexao(context)
        if (intent?.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
        {
            if (conex){
                Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Desconectado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun pegaconexao(context: Context?): Boolean {
        val connectivity = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.getAllNetworkInfo()
        if (info != null)
        {
            for (i in info.indices)
            {
                if (info[i].getState() === NetworkInfo.State.CONNECTED){
                    return true
                }
            }
        }
        return false
    }
}


