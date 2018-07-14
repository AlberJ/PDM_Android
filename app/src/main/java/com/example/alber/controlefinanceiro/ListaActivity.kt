package com.example.alber.controlefinanceiro

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.alber.controlefinanceiro.DAOs.MovimentacaoDAO
import com.example.alber.controlefinanceiro.DAOs.TotalDAO
import com.example.alber.controlefinanceiro.Models.Movimentacao

import kotlinx.android.synthetic.main.activity_lista.*
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.widget.Toast


class ListaActivity : AppCompatActivity()
{
    lateinit var lv: ListView
    val UPDATE = 3
    lateinit var context: Context
    lateinit var mdao:MovimentacaoDAO
    private lateinit var internet:EmailReceiver
    private lateinit var itf: IntentFilter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            dispacha()
        }

        fabEmail.setOnClickListener { view ->
           val uri = Uri.parse("mailto:")
           val it = Intent(Intent.ACTION_SENDTO, uri)
           it.putExtra(Intent.EXTRA_SUBJECT, "Movimentações Financeiras de Alber")
           var lista = mdao.select().toString()
           it.putExtra(Intent.EXTRA_TEXT, lista)
           startActivity(it)
        }

        this.internet = EmailReceiver()
        this.itf = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(internet,itf)

        this.mdao = MovimentacaoDAO(this)

        this.lv = this.findViewById(R.id.lvLista)

        this.adapter()

        this.lv.setOnItemClickListener(OnClick())
        this.lv.setOnItemLongClickListener(OnLongClick())
    }

    fun adapter(){
        this.lv.adapter = ArrayAdapter<Movimentacao>(this,
                android.R.layout.simple_list_item_1, MovimentacaoDAO(this).select())
    }

    inner class OnClick: AdapterView.OnItemClickListener
    {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
        {
            if(parent != null)
            {
                var m = parent.adapter.getItem(position) as Movimentacao
                var it = Intent(this@ListaActivity,
                        MovimentaActivity::class.java)
                it.putExtra("EDITAR", m)
                startActivityForResult(it, UPDATE)
            }
        }
    }

    inner class OnLongClick: AdapterView.OnItemLongClickListener
    {
        override fun onItemLongClick(parent: AdapterView<*>?, view: View?,
                                     position: Int, id: Long): Boolean
        {
            if (parent != null)
            {
                var m = parent.adapter.getItem(position) as Movimentacao
                dispacha(m)
            }
            return true
        }
    }

    fun dispacha(movi:Movimentacao)
    {
        var it = Intent()
        it.putExtra("APAGAR", movi)
        setResult(Activity.RESULT_OK, it)
        finish()
    }

    fun dispacha() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        adapter()
    }
}
