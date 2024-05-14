package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R

class ListaMateria: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_materias)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewMaterias)?: return
        val adapter = CustomAdapterMateria()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        var btnAgregarMateria: Button

        btnAgregarMateria = findViewById(R.id.btnAgregarMateria)

        btnAgregarMateria.setOnClickListener{
            val intent = Intent(this, IngresoMateriaActivity::class.java)
            this.startActivity(intent)
        }



    }


}