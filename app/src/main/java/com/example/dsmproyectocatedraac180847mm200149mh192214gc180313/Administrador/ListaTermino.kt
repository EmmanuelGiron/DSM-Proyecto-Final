package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import  android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R

class ListaTermino: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_terminos)
        val toolbar: Toolbar = findViewById(R.id.toolbarTermino)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
        }

        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)

        arrowLeft.setOnClickListener{
            val intent = Intent(this, ListaMateria::class.java)
            startActivity(intent)
        }

        var materiaActual = TextView(this)
        var insetarTermino = Button(this)
        var usuarioActual= TextView(this)

        materiaActual = findViewById(R.id.txtMateriaActual)
        insetarTermino = findViewById(R.id.btnAgregarTermino)
        usuarioActual = findViewById(R.id.txtUsuarioActual)

        val extras = intent.extras
        materiaActual.text = extras?.getString("nombre").toString()
        usuarioActual.text = extras?.getString("usuarioActual").toString()

        if(usuarioActual.text.toString() == "Administrador"){
            insetarTermino.visibility = View.VISIBLE
            toolbar.setBackgroundColor(Color.parseColor("#E8E8E8"))
        }

        insetarTermino.setOnClickListener{
            val intent = Intent(this,IngresoTerminoActivity::class.java)
            intent.putExtra("nombre",materiaActual.text.toString())
            this.startActivity(intent)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewTerminos)?: return
        val adapter = CustomAdapterTermino(materiaActual.text.toString(),usuarioActual.text.toString())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}