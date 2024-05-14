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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ActualizarMateria: AppCompatActivity() {
    //Trayendo datos de la base
    val dataBase = Firebase.database
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("terminos")

    var referencias = mutableListOf<String>()
    var materias = mutableListOf<String>()

    val updatesMultiples = hashMapOf<String, Any>()
    var materiaAuxiliar:String? = null

    class termino {
        var referencia: String? = null
        var materia: String? = null
        constructor() {
        }

        constructor(referencia: String, materia:String) {
            this.referencia = referencia
            this.materia = materia
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_editar_materia)

        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)

        arrowLeft.setOnClickListener{
            val intent = Intent(this, ListaMateria::class.java)
            startActivity(intent)
        }


        //val database = FirebaseDatabase.getInstance()
        var txtReferencia = TextView(this)
        var txtNombreMateria = TextView(this)
        var txtDescripcion = TextView(this)
        var txtColor = TextView(this)
        var btnEditar = Button(this)
        var itemBorrar = Button(this)

        txtReferencia = findViewById(R.id.referenciaActualizarID)
        txtNombreMateria= findViewById(R.id.txtNombreEditarMateria)
        txtDescripcion = findViewById(R.id.txtEditarDescripcionMateria)
        txtColor= findViewById(R.id.editarColorTxt)
        btnEditar = findViewById(R.id.btnEditarMateria)
        itemBorrar = findViewById(R.id.btnEliminarMateria)

        val extras = intent.extras
        txtReferencia.text = extras?.getString("referencia").toString()
        txtNombreMateria.text = extras?.getString("nombre").toString()
        materiaAuxiliar = txtNombreMateria.text.toString().trim()
        txtDescripcion.text = extras?.getString("descripcion").toString()
        txtColor.text = extras?.getString("color").toString()


        btnEditar.setOnClickListener{
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val termino = snapshot.getValue(termino::class.java)
                        val childKey = snapshot.key
                        println(childKey.toString())
                        println(txtNombreMateria.text)
                        println(materiaAuxiliar)
                        println(termino?.materia.toString())
                        if (materiaAuxiliar == termino?.materia.toString().trim()) {
                            referencias.add(childKey.toString())
                            materias.add(termino?.materia.toString())
                            val refereciaListaTerminos = database.getReference("terminos/${childKey.toString()}")

                                val actualizacionesMultiples = mapOf<String, Any>(
                                    "materia" to txtNombreMateria.text.toString()
                                )
                                refereciaListaTerminos.updateChildren(actualizacionesMultiples)
                                    .addOnSuccessListener {
                                        println("Exito")
                                    }
                                    .addOnFailureListener {
                                        println("Hubo un error")
                                    }
                            }
                        }
                }


                override fun onCancelled(error: DatabaseError) {

                }
            })

           val refereciaListaMaterias = database.getReference("materias/${txtReferencia.text}")
           if(txtNombreMateria.text.trim().toString() != "" && txtDescripcion.text.trim().toString() != "") {
               val actualizaciones = mapOf<String, Any>(
                   "nombre" to txtNombreMateria.text.toString(),
                   "descripcion" to txtDescripcion.text.toString(),
                   "color" to txtColor.text.toString()
               )
               refereciaListaMaterias.updateChildren(actualizaciones)
                   .addOnSuccessListener {
                       //Alerta de confirmacion
                       val builder = AlertDialog.Builder(this)
                       builder.setTitle("Resultado")
                       builder.setMessage("Materia actualizada con éxito!")
                       builder.setPositiveButton("Aceptar") { dialog, which ->
                           val intent = Intent(this, ListaMateria::class.java)
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
            val referecia_a_elimianr = database.getReference("materias/${txtReferencia.text}")

            referecia_a_elimianr.removeValue()
                .addOnSuccessListener {
                    //Alerta de confirmacion
                    val builder = AlertDialog.Builder(itemBorrar.context)
                    builder.setTitle("Resultado")
                    builder.setMessage("Se elimino correctamente")
                    builder.setPositiveButton("Aceptar") { dialog, which ->
                        val intent = Intent(itemBorrar.context, ListaMateria::class.java)
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