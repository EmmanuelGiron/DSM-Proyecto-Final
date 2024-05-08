package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ActualizarTermino: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_editar_termino)

        //Firbase
        val database = FirebaseDatabase.getInstance()
        var txtReferencia = TextView(this)
        var txtNombreTermino = TextView(this)
        var txtDescripcion = TextView(this)
        var txtImagenTermino = TextView(this)
        var txtMateriaActual = TextView(this)
        var imageViewTermino : ImageView
        var btnEditar = Button(this)
        var itemBorrar = Button(this)

        txtReferencia = findViewById(R.id.referenciaActualizarTerminoID)
        txtNombreTermino= findViewById(R.id.txtNombreEditarTermino)
        txtDescripcion = findViewById(R.id.txtEditarDescripcionTermino)
        txtImagenTermino= findViewById(R.id.txtURLImagen)
        txtMateriaActual = findViewById(R.id.txtMateriaActual)
        imageViewTermino = findViewById(R.id.URLImagenView)
        btnEditar = findViewById(R.id.btnEditarTermino)
        itemBorrar = findViewById(R.id.btnEliminarTermino)

        val extras = intent.extras
        txtReferencia.text = extras?.getString("referencia").toString()
        txtNombreTermino.text = extras?.getString("nombre").toString()
        txtDescripcion.text = extras?.getString("descripcion").toString()
        txtImagenTermino.text = extras?.getString("imagen").toString()
        txtMateriaActual.text = extras?.getString("materiaActual").toString()

        val urlImagen = txtImagenTermino.text.toString()
        if(urlImagen.isNotEmpty()) {
            Picasso.get().load(urlImagen).into(imageViewTermino)
        } else {
            // Manejar el caso cuando la URL está vacía
        }


        btnEditar.setOnClickListener{
            val refereciaListaTerminos = database.getReference("terminos/${txtReferencia.text}")
            if(txtNombreTermino.text.trim().toString() != "" && txtDescripcion.text.trim().toString() != "") {
                val actualizaciones = mapOf<String, Any>(
                    "nombre" to txtNombreTermino.text.toString(),
                    "descripcion" to txtDescripcion.text.toString(),
                    "imagen" to txtImagenTermino.text.toString(),
                    "materia" to txtMateriaActual.text.toString()
                )

                refereciaListaTerminos.updateChildren(actualizaciones)
                    .addOnSuccessListener {
                        //Alerta de confirmacion
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Resultado")
                        builder.setMessage("Termino actualizado con éxito!")
                        builder.setPositiveButton("Aceptar") { dialog, which ->
                            val intent = Intent(this, ListaTermino::class.java)
                            intent.putExtra("nombre",txtMateriaActual.text.toString())
                            startActivity(intent)
                        }
                        // Mostrando la alerta
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                    .addOnFailureListener {
                        println("Hubo un error")
                    }
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Atención!!")
                builder.setMessage("Debe llenar todos los campos!")
                builder.setPositiveButton("Aceptar") { dialog, which ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        //Eliminar datos

        itemBorrar.setOnClickListener{
            val referecia_a_elimianr = database.getReference("terminos/${txtReferencia.text}")

            referecia_a_elimianr.removeValue()
                .addOnSuccessListener {
                    //Alerta de confirmacion
                    val builder = AlertDialog.Builder(itemBorrar.context)
                    builder.setTitle("Resultado")
                    builder.setMessage("Se elimino correctamente")
                    builder.setPositiveButton("Aceptar") { dialog, which ->
                        val intent = Intent(itemBorrar.context,ListaTermino::class.java)
                        intent.putExtra("nombre",txtMateriaActual.text.toString())
                        itemBorrar.context.startActivity(intent)
                    }
                    // Mostrando la alerta
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
                .addOnFailureListener { error ->
                    Log.e("TAG", "Error al eliminar: $error")
                }
        }
    }
}