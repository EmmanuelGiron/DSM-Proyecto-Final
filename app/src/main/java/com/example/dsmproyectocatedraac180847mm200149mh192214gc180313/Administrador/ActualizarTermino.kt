package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ActualizarTermino: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_editar_termino)

        //Trabajando con el boton de imagen
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if(uri!=null){
                subirImagenALmacenamiento(uri)
            }else{
                //No imagen
            }

        }
        lateinit var btnImagen: Button

        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)



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
        var usuarioActual = TextView(this)


        txtReferencia = findViewById(R.id.referenciaActualizarTerminoID)
        txtNombreTermino= findViewById(R.id.txtNombreEditarTermino)
        txtDescripcion = findViewById(R.id.txtEditarDescripcionTermino)
        txtImagenTermino= findViewById(R.id.txtURLImagen)
        txtMateriaActual = findViewById(R.id.txtMateriaActual)
        imageViewTermino = findViewById(R.id.URLImagenView)
        btnEditar = findViewById(R.id.btnEditarTermino)
        itemBorrar = findViewById(R.id.btnEliminarTermino)
        usuarioActual = findViewById(R.id.txtUsuarioActual)

        val extras = intent.extras
        txtReferencia.text = extras?.getString("referencia").toString()
        txtNombreTermino.text = extras?.getString("nombre").toString()
        txtDescripcion.text = extras?.getString("descripcion").toString()
        txtImagenTermino.text = extras?.getString("imagen").toString()
        txtMateriaActual.text = extras?.getString("materiaActual").toString()
        usuarioActual.text = extras?.getString("usuarioActual").toString()

        val urlImagen = txtImagenTermino.text.toString()
        if(urlImagen.isNotEmpty()) {
            Picasso.get().load(urlImagen).into(imageViewTermino)
        } else {
            // Manejar el caso cuando la URL está vacía
        }

        arrowLeft.setOnClickListener{
            val intent = Intent(this,ListaTermino::class.java)
            intent.putExtra("nombre",txtMateriaActual.text.toString())
            intent.putExtra("usuarioActual",usuarioActual.text.toString())
            startActivity(intent)
        }

        btnImagen = findViewById(R.id.btnImagen)
        btnImagen.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnEditar.setOnClickListener{
            val refereciaListaTerminos = database.getReference("terminos/${txtReferencia.text}")
            if(txtNombreTermino.text.trim().toString() != "" && txtDescripcion.text.trim().toString() != "") {
                val actualizaciones = mapOf<String, Any>(
                    "nombre" to txtNombreTermino.text.toString().trim(),
                    "descripcion" to txtDescripcion.text.toString().trim(),
                    "imagen" to txtImagenTermino.text.toString().trim(),
                    "materia" to txtMateriaActual.text.toString().trim()
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
                            intent.putExtra("usuarioActual",usuarioActual.text.toString())
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
                        intent.putExtra("usuarioActual",usuarioActual.text.toString())
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
    fun subirImagenALmacenamiento(uri: Uri) {
        showProgressDialog()
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // Obtener el nombre del archivo de la URI
        val nombreArchivo = uri.lastPathSegment ?: "imagen.jpg"

        // Crear una referencia al archivo en Firebase Storage
        val archivoRef: StorageReference = storageRef.child("img/$nombreArchivo")

        // Subir el archivo a Firebase Storage
        val uploadTask = archivoRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            // El archivo se ha subido exitosamente
            // Puedes obtener la URL de descarga del archivo aquí si la necesitas
            archivoRef.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                var urlImagen: TextView
                var imagenMostrar: ImageView
                urlImagen = findViewById(R.id.txtURLImagen)
                imagenMostrar = findViewById(R.id.URLImagenView)
                urlImagen.setText(url)
                Picasso.get().load(url).into(imagenMostrar)
            }.addOnFailureListener {
                // Manejar errores al obtener la URL de descarga
            }
        }.addOnFailureListener { exception ->
            // Manejar errores durante la subida del archivo
        }
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
        }, 4000) // 3000 milisegundos = 3 segundos
    }
}