package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import  android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R

class ListaTermino: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_terminos)
        showProgressDialog()
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
            intent.putExtra("usuarioActual",usuarioActual.text.toString())
            this.startActivity(intent)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewTerminos)?: return
        val adapter = CustomAdapterTermino(materiaActual.text.toString(),usuarioActual.text.toString())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    private fun showProgressDialog() {
        // Inflar el layout del ProgressDialog
        val dialogView = layoutInflater.inflate(R.layout.progress_bar, null)

        // Crear el AlertDialog
        val progressDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
            .create()

        // Mostrar el AlertDialog
        progressDialog.show()
        // Descartar el AlertDialog después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
        }, 2500) // 3000 milisegundos = 3 segundos
    }
}