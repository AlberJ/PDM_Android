package com.example.alber.controlefinanceiro.DAOs

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.alber.controlefinanceiro.Models.EntradaSimpes
import com.example.alber.controlefinanceiro.Models.Movimentacao
import com.example.alber.controlefinanceiro.Models.SaidaSimples

class TotalDAO(var context: Context)
{
    private lateinit var banco: BancoHelper

    init {
        this.banco = BancoHelper(context)
    }

//    fun insert (valor:Double)
//    {
//        val cv = ContentValues()
//        cv.put("total", valor)
//        this.banco.writableDatabase.insert("valortotal", null, cv)
//        Log.i("AQUI!", "VALOR INSERIDO NO BANCO.")
//    }

    fun count(): Int{
        val colunas = arrayOf("id")
        val cursor =this.banco.readableDatabase.query("valortotal", colunas,
                null, null, null, null, null)
//        Log.e("AQUI", "o count do TotalDAO ta conando mais de 1.")
        return cursor.count
    }

    fun update(valor: Double)
    {
        val primeiro:Int = 0
        val cv = ContentValues()
        cv.put("total", valor)
        this.banco.writableDatabase.update("valortotal", cv, "id = ?",
                arrayOf(primeiro.toString()))
    }

    fun select():Double
    {
        val lista = ArrayList<Double>()
        val colunas = arrayOf("id", "total")
        val cursor = this.banco.readableDatabase.query("valortotal", colunas,
                null, null, null, null, null)

        cursor.moveToFirst()

        if (cursor.count>0){
            do {
                val valor = cursor.getDouble(cursor.getColumnIndex("total"))
                lista.add(valor)
            }while(cursor.moveToNext())
        }
        return lista.last()
    }

}