package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import  android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R

class ListaTermino: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_terminos)

        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)

        arrowLeft.setOnClickListener{
            val intent = Intent(this,ListaMateria::class.java)
            startActivity(intent)
        }

        var materiaActual = TextView(this)
        var insetarTermino = Button(this)

        materiaActual = findViewById(R.id.txtMateriaActual)
        insetarTermino = findViewById(R.id.btnAgregarTermino)

        val extras = intent.extras
        materiaActual.text = extras?.getString("nombre").toString()

        insetarTermino.setOnClickListener{
            val intent = Intent(this,IngresoTerminoActivity::class.java)
            intent.putExtra("nombre",materiaActual.text.toString())
            this.startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewTerminos)?: return
        val adapter = CustomAdapterTermino(materiaActual.text.toString())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}