package com.calculaVirusApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.calculaVirusApp.model.Insumo
import com.calculaVirusApp.model.RequestChecklist
import com.calculaVirusApp.model.RequestInsumo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_insumo.*

class InsumoActivity : AppCompatActivity() {
    private val datalist:MutableList<Insumo> = mutableListOf()
    private lateinit var insumoAdapter: InsumoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insumo)
        setSupportActionBar(findViewById(R.id.toolbar))
        val account = GoogleSignIn.getLastSignedInAccount(this)
        var user_email="barrons.guillermo.sal@gmail.com"
        if(account!=null){
            user_email = account.email!!
        }
        recycler_insumo.layoutManager =
            LinearLayoutManager(this)
        insumoAdapter = InsumoAdapter(datalist)
        recycler_insumo.adapter = insumoAdapter

        orden_prioridad.setOnClickListener {
            val intent = Intent(this,InsumoActivity::class.java)
            intent.putExtra("query_type", 2)
            startActivity(intent)
        }
        orden_caducidad.setOnClickListener {
            val intent = Intent(this,InsumoActivity::class.java)
            intent.putExtra("query_type", 3)
            startActivity(intent)
        }
        orden_cantidad.setOnClickListener {
            val intent = Intent(this,InsumoActivity::class.java)
            intent.putExtra("query_type", 4)
            startActivity(intent)
        }
        orden_categoria.setOnClickListener {
            val intent = Intent(this,InsumoActivity::class.java)
            intent.putExtra("query_type", 5)
            startActivity(intent)
        }

        val query_type: Int = intent.getIntExtra("query_type",1)
        AndroidNetworking.initialize(this)
        var url = ""
        if(query_type==1){
            url = "http://martinhelmut.pythonanywhere.com/insumos/get_insumos_by_user/"
        }
        else if(query_type==2){
            url = "http://martinhelmut.pythonanywhere.com/insumos/get_insumos_by_priority/"
        }
        else if(query_type==3){
            url = "http://martinhelmut.pythonanywhere.com/insumos/get_insumos_by_quantity/"
        }
        else if(query_type==4){
            url = "http://martinhelmut.pythonanywhere.com/insumos/get_insumos_by_due_date/"
        }
        else if(query_type==5){
            url = "http://martinhelmut.pythonanywhere.com/insumos/get_insumos_by_category/"
        }
        AndroidNetworking.get(url)
            .addQueryParameter("user_email",user_email)
            .build().getAsObject(RequestInsumo::class.java,object:
                ParsedRequestListener<RequestInsumo> {
                override fun onResponse(response: RequestInsumo?) {
                    response?.results?.let{datalist.addAll(it)}
                    insumoAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    Log.e("NetworkError",anError.toString())
                }
            })
        crear_insumo.setOnClickListener {
            val intent = Intent(this,CrearInsumoActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        menu?.findItem(R.id.toolbar)?.title = "Calcula virus"
        return true
    }
}
