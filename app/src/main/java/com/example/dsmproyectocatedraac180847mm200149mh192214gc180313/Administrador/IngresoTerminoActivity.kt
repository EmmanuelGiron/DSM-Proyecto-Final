package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import android.os.Handler
import android.os.Looper
import org.w3c.dom.Text


class IngresoTerminoActivity: AppCompatActivity() {
    //Conexion con firebase
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("terminos")

    //Trabajando con el boton de imagen
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        if(uri!=null){
            subirImagenALmacenamiento(uri)
        }else{
            //No imagen
        }

    }
    lateinit var btnImagen: Button

    class Termino {
        var nombre = ""
        var descripcion = ""
        var imagen = ""
        var materia = ""
    }

    val termino = Termino()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_ingreso_termino)

        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)

        var txtTermino: EditText
        var txtDescripcion: EditText
        var btnIngresarTermino: Button
        var materiaActual: TextView
        var imagen: TextView
        var usuarioActual : TextView
        var btnDescartarTermino: Button


        txtTermino = findViewById(R.id.txtTermino)
        txtDescripcion = findViewById(R.id.txtDescripcionTermino)
        btnIngresarTermino = findViewById(R.id.btnIngresarTermino)
        materiaActual = findViewById(R.id.txtMateriaActual)
        imagen = findViewById(R.id.txtURLImagen)
        usuarioActual = findViewById(R.id.txtUsuarioActual)
        btnDescartarTermino = findViewById(R.id.btnDescartarTermino)

        btnImagen = findViewById(R.id.btnImagen)
        btnImagen.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val extras = intent.extras
        materiaActual.text = extras?.getString("nombre").toString()
        usuarioActual.text = extras?.getString("usuarioActual").toString()
        termino.materia = materiaActual.text.toString()

        arrowLeft.setOnClickListener{
            val intent = Intent(this,ListaTermino::class.java)
            intent.putExtra("nombre",materiaActual.text.toString())
            intent.putExtra("usuarioActual",usuarioActual.text.toString())
            startActivity(intent)
        }
        btnDescartarTermino.setOnClickListener{
            val intent = Intent(this,ListaTermino::class.java)
            intent.putExtra("nombre",materiaActual.text.toString())
            intent.putExtra("usuarioActual",usuarioActual.text.toString())
            startActivity(intent)
        }


        //Ingreso de una termino a la base
        btnIngresarTermino.setOnClickListener{
            termino.nombre = txtTermino.text.toString().trim()
            termino.descripcion = txtDescripcion.text.toString().trim()
            termino.imagen = imagen.text.toString().trim()

            val builder = AlertDialog.Builder(this)

            if(termino.nombre != "" && termino.descripcion != "")
            {  //Ingreso a la base de datos firebase
                val ticketRef = reference.push()
                ticketRef.setValue(termino)

                //Alerta de confirmacion

                builder.setTitle("Resultado")
                builder.setMessage("Termino ingresada con éxito!")

                builder.setPositiveButton("Aceptar") { dialog, which ->
                    val intent = Intent(this,ListaTermino::class.java)
                    intent.putExtra("nombre",materiaActual.text.toString())
                    intent.putExtra("usuarioActual",usuarioActual.text.toString())
                    startActivity(intent)
                }
            }else {
                builder.setTitle("Atención!!")
                builder.setMessage("Debe llenar todos los campos!")
                builder.setPositiveButton("Aceptar") { dialog, which ->

                }
            }
            // Mostrando la alerta
            val dialog: AlertDialog = builder.create()
            dialog.show()
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
        }, 5000) // 3000 milisegundos = 3 segundos
    }
}