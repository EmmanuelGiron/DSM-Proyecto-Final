package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Registro

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador.ListaMateria
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {

    private lateinit var nombreUser: EditText
    private lateinit var correoUser: EditText
    private lateinit var contraseñaUser: EditText
    private lateinit var confirmarPass: EditText
    private lateinit var registrarUser: Button
    private lateinit var tengoCuenta: TextView

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var nombre = ""
    private var correo = ""
    private var contraseña = ""
    private var confirmarContraseña = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_signup)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Registro"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)

        nombreUser = findViewById(R.id.Nombres)
        correoUser = findViewById(R.id.Correo)
        contraseñaUser = findViewById(R.id.Contraseña)
        confirmarPass = findViewById(R.id.ConfirmarContra)
        registrarUser = findViewById(R.id.RegistrarUser)
        tengoCuenta = findViewById(R.id.TengoCuentaTxt)

        registrarUser.setOnClickListener{
            validarDatos()
        }

        tengoCuenta.setOnClickListener{
            startActivity(Intent(this@Registro,Login::class.java))
        }


    }

    private fun validarDatos() {
        nombre = nombreUser.text.toString()
        correo = correoUser.text.toString()
        contraseña = contraseñaUser.text.toString()
        confirmarContraseña = confirmarPass.text.toString()

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Debe de ingresar su nombre", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
        } else if (contraseña.isEmpty()) {
            Toast.makeText(this, "Debe de ingresar una contraseña", Toast.LENGTH_SHORT).show()
        } else if (confirmarContraseña.isEmpty()) {
            Toast.makeText(this, "Confirme su contraseña", Toast.LENGTH_SHORT).show()
        } else if (contraseña != confirmarContraseña) {
            Toast.makeText(this, "Las contraseñas que ingreso no coinciden", Toast.LENGTH_SHORT).show()
        } else {
            crearCuenta()
        }
    }

    private fun crearCuenta() {
        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnSuccessListener {
                guardarInformacion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarInformacion() {
        val uid = firebaseAuth.uid

        val datos: HashMap<String, String> = HashMap()
        datos["uid"] = uid!!
        datos["correo"] = correo
        datos["nombres"] = nombre
        datos["password"] = contraseña

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")
        databaseReference.child(uid)
            .setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Registro, ListaMateria::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
    }

}
