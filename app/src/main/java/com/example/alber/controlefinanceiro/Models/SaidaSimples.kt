package com.example.alber.controlefinanceiro.Models

import java.io.Serializable

class SaidaSimples(override var descricao:String,
                   override var valor: Double,
                   override var operacao:Boolean = false,
                   override var id:Int = -1):Movimentacao, Serializable
{
    override fun toString(): String {
        return "Ss: ${descricao}. R$ -${valor}."
    }
}