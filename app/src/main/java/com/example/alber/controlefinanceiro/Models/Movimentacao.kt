package com.example.alber.controlefinanceiro.Models

import java.io.Serializable

interface Movimentacao: Serializable
{
    var operacao:Boolean
    var valor:Double
    var descricao: String
    var id:Int
}