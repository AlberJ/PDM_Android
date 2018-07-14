package com.example.alber.controlefinanceiro.DAOs

import android.content.ContentValues
import android.content.Context
import com.example.alber.controlefinanceiro.Models.EntradaSimpes
import com.example.alber.controlefinanceiro.Models.Movimentacao
import com.example.alber.controlefinanceiro.Models.SaidaSimples

class MovimentacaoDAO(var context: Context)
{
    private lateinit var banco: BancoHelper

    init {
        this.banco = BancoHelper(context)
    }

    fun insert (movimentacao: Movimentacao)
    {
        val cv = ContentValues()
        cv.put("descricao", movimentacao.descricao)
        cv.put("valor", movimentacao.valor)
        cv.put("operacao", movimentacao.operacao)
        this.banco.writableDatabase.insert("movimentacao", null, cv)
    }

    fun select():ArrayList<Movimentacao>
    {
        val lista = ArrayList<Movimentacao>()
        val colunas = arrayOf("id", "descricao","valor", "operacao")
        val cursor = this.banco.readableDatabase.query("movimentacao", colunas,
                null, null, null, null, null)

        cursor.moveToFirst()

        if (cursor.count>0)
        {
            do{
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val desc = cursor.getString(cursor.getColumnIndex("descricao"))
                val valor = cursor.getDouble(cursor.getColumnIndex("valor"))
                val op = cursor.getInt(cursor.getColumnIndex("operacao"))

                if (op == 1){
                    lista.add(EntradaSimpes(desc, valor, true, id))
                }else{
                    lista.add(SaidaSimples(desc, valor, false, id))
                }
            }while (cursor.moveToNext())
        }
        return lista
    }

    fun count(): Int{
        val colunas = arrayOf("id")
        val cursor =this.banco.readableDatabase.query("movimentacao", colunas,
                null, null, null, null, null)

        return cursor.count
    }

    fun delete(m:Movimentacao){
        this.banco.writableDatabase.delete("movimentacao", "id = ?",
                arrayOf(m.id.toString()))
    }

    fun update(m:Movimentacao)
    {
        val cv = ContentValues()
        cv.put("descricao", m.descricao)
        cv.put("valor", m.valor)
//        cv.put("operacao", m.operacao)
        this.banco.writableDatabase.update("movimentacao", cv, "id = ?",
                arrayOf(m.id.toString()))
    }
//    fun update(valor: Double)
//    {
//        val primeiro:Int = 0
//        val cv = ContentValues()
//        cv.put("total", valor)
//        this.banco.writableDatabase.update("valortotal", cv, "id = ?",
//                arrayOf(primeiro.toString()))
//    }


}