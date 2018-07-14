package com.example.alber.controlefinanceiro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.alber.controlefinanceiro.DAOs.MovimentacaoDAO
import com.example.alber.controlefinanceiro.DAOs.TotalDAO
import com.example.alber.controlefinanceiro.Models.Movimentacao
import com.example.alber.controlefinanceiro.R.*

class MainActivity : AppCompatActivity() {

//    INTEIROS DAS TELAS
    private val movimenta:Int = 1
    private val listaMovimentacoes:Int = 2

//    VARIAVEIS DO SISTEMA
    private lateinit var movimentacaoDAO: MovimentacaoDAO
    private lateinit var totaldao:TotalDAO
    private var valorAtual:Double = 0.0

//    COMPONENTES DA VIEW
    private lateinit var tvValor:TextView
    private lateinit var btnMov:Button
    private lateinit var btnLista:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

//      INSTANCIA AS VARIAVEIS
        this.totaldao = TotalDAO(this)
        this.movimentacaoDAO = MovimentacaoDAO(this)

        this.tvValor = findViewById(id.tvMainValorAtual)
        this.btnMov = findViewById(id.btnMainMovimentar)
        this.btnLista = findViewById(id.btnMainListar)

//        RECUPERA OU SETA NA VARIAVEL O VALOR TOTAL
        atualizaTotalDB()

//        SETA EVENTOS NOS BOTÕES
        this.btnMov.setOnClickListener({
            val it = Intent(this@MainActivity, MovimentaActivity::class.java)
            startActivityForResult(it, movimenta)
        })

        this.btnLista.setOnClickListener({
            val it = Intent(this@MainActivity, ListaActivity::class.java)
            startActivityForResult(it, listaMovimentacoes)
        })

    }

    fun atualizaTotalDB()
    {
        this.valorAtual = this.totaldao.select()
        this.tvValor.setText(valorAtual.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var aviso:Toast

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == movimenta)
            {
                var mov = data?.getSerializableExtra("movimentacao") as Movimentacao
                atualizaTotalDB()
//                INFORMA
                if(mov.operacao)
                {
                    aviso = Toast.makeText(applicationContext,
                            "Valor ADICIONADO, ${mov.valor}", Toast.LENGTH_LONG)
                }else{
                    aviso = Toast.makeText(applicationContext,
                            "Valor SUBTRAIDO, ${mov.valor}", Toast.LENGTH_LONG)
                }

                aviso.show()
            }
            else if(requestCode == listaMovimentacoes)
            {
                var apagar = data?.getSerializableExtra("APAGAR") as Movimentacao
                if (apagar != null) {
                    //                ATUALIZA O VALOR TOTAL MAS NÃO PERSISTEM AINDA
                    // AQUI É O OPOSTO POIS TRATA-SE DE APAGAR A MOVIMENTAÇÃO REALIZADA ANTERIORMENTE
                    if (apagar.operacao) {
                        this.valorAtual -= apagar.valor
                    } else {
                        this.valorAtual += apagar.valor
                    }
                    aviso = Toast.makeText(applicationContext, "Valor DELETADO FOI DE ${apagar.valor}",
                            Toast.LENGTH_LONG)

//                PERSISTE O VALOR TOTAL
                    this.totaldao.update(this.valorAtual)
                    this.tvValor.setText(valorAtual.toString())
//                PERSISTE MOVIMENTAÇÃO
                    movimentacaoDAO.delete(apagar)

//                EXIBE O TOAST
                    aviso.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        atualizaTotalDB()
    }
}
