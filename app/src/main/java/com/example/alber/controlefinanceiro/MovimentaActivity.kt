package com.example.alber.controlefinanceiro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.alber.controlefinanceiro.DAOs.MovimentacaoDAO
import com.example.alber.controlefinanceiro.DAOs.TotalDAO
import com.example.alber.controlefinanceiro.Models.EntradaSimpes
import com.example.alber.controlefinanceiro.Models.Movimentacao
import com.example.alber.controlefinanceiro.Models.SaidaSimples

class MovimentaActivity : AppCompatActivity()
{
    lateinit var etDesc:EditText
    lateinit var etValor:EditText
    lateinit var btnEntrada:Button
    lateinit var btnSaida:Button
    lateinit var btnVolta:Button
    lateinit var mdao:MovimentacaoDAO
    lateinit var tdao:TotalDAO
    lateinit var movimentacao:Movimentacao
    var diferenca:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimenta)

        this.mdao = MovimentacaoDAO(this)
        this.tdao = TotalDAO(this)

        this.etDesc = this.findViewById(R.id.etMovimentaDesc)
        this.etValor = findViewById(R.id.etMovimentaValor)

        this.btnEntrada = findViewById(R.id.btnMovimentaEntrada)
        this.btnSaida = findViewById(R.id.btnMovimentaSaida)
        this.btnVolta = findViewById(R.id.btnMovimentaVolta)

        var mov = intent.getSerializableExtra("EDITAR")
//        VERIFICA SE É EDIÇÃO
        if (mov != null)
        {
            this.movimentacao = mov as Movimentacao
            this.etDesc.setText(this.movimentacao.descricao)
            this.etValor.setText(this.movimentacao.valor.toString())
            this.diferenca = this.movimentacao.valor
        }

        this.btnEntrada.setOnClickListener({
//          SE ENTRAR É EDIÇÃO
            if (this.diferenca != 0.0) {
                editarMovimentacao(it)
            }else {
                novaEntrada(it)
            }
        })

        this.btnSaida.setOnClickListener({
//          SE ENTRAR É EDIÇÃO
            if (this.diferenca != 0.0) {
                editarMovimentacao(it)
            }else {
                novaSaida(it)
            }
        })

        this.btnVolta.setOnClickListener({
            finish()
        })
    }

    fun dispacha(movi: Movimentacao)
    {
        var it = Intent()
        it.putExtra("movimentacao", movi)
        setResult(Activity.RESULT_OK, it)
        finish()
    }

    fun dispacha(){
        finish()
    }

    fun editarMovimentacao(view: View)
    {
        var novoTotal = tdao.select()
        var desc:String = etDesc.text.toString()
        var valor:Double
        var textoValor:String = etValor.text.toString()
        if (textoValor == ""){
            valor = 0.0
        }else {
            valor = textoValor.toDouble()
        }

        this.diferenca -= valor
        this.movimentacao.descricao = desc
        this.movimentacao.valor = valor
        this.mdao.update(this.movimentacao)

        if (this.movimentacao.operacao){
//          SE ENTRADA
            novoTotal -= this.diferenca
        }else{
//          SE SAIDA
            novoTotal += this.diferenca
        }
        tdao.update(novoTotal)

        dispacha()
    }

    fun novaEntrada(view: View)
    {
        var novoTotal = tdao.select()
        var desc:String = etDesc.text.toString()
        var valor:Double
        var textoValor:String = etValor.text.toString()
        if (textoValor == ""){
            valor = 0.0
        }else {
            valor = textoValor.toDouble()
        }

        var entrada = EntradaSimpes(desc, valor) as Movimentacao
        mdao.insert(entrada)

        novoTotal += entrada.valor
        tdao.update(novoTotal)

        dispacha(entrada)
    }

    fun novaSaida(view: View)
    {
        var novoTotal = tdao.select()
        var desc:String = etDesc.text.toString()
        var valor:Double
        var textoValor:String = etValor.text.toString()

        if (textoValor == ""){
            valor = 0.0
        }else {
            valor = textoValor.toDouble()
        }

        var saida = SaidaSimples(desc, valor) as Movimentacao
        mdao.insert(saida)

        novoTotal -= saida.valor
        tdao.update(novoTotal)

        dispacha(saida)
    }


}
