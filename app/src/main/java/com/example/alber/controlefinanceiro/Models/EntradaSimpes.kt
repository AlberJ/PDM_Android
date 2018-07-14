package com.example.alber.controlefinanceiro.Models

import java.io.Serializable

class EntradaSimpes(override var descricao:String,
                    override var valor: Double,
                    override var operacao:Boolean = true,
                    override var id:Int = -1): Movimentacao, Serializable
{
    override fun toString(): String {
        return "Es: ${descricao}. R$ +${valor}."
    }

}