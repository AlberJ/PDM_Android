package com.example.alber.controlefinanceiro.DAOs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

val VERSAO = 1

class BancoHelper (context: Context?): SQLiteOpenHelper(context, "financa.sqlite",
        null, VERSAO)
{
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlM = "create table movimentacao("+
                    "id integer primary key autoincrement, "+
                    "descricao string, "+
                    "valor double, "+
                    "operacao boolean"+
                    ")"
        db?.execSQL(sqlM)

        val sqlV = "create table valortotal("+
                "id integer primary key, "+
                "total double"+
                ")"
        db?.execSQL(sqlV)

        val setvalor = "INSERT into valortotal values(0, 0.0)"
        db?.execSQL(setvalor)
        Log.e("AQUI", "inseriu na tablea ${setvalor}")
    }

    override fun onUpgrade(db: SQLiteDatabase?, antes: Int, nova: Int) {

    }
}