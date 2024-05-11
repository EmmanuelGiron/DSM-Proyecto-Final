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
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.MainActivity
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    private lateinit var correoLogin: EditText
    private lateinit var passLogin: EditText
    private lateinit var btnLogin: Button
    private lateinit var nuevoUsuario: TextView

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var correo = ""
    private var contraseña = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_login)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Login"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)

        correoLogin=findViewById(R.id.CorreoLog)
        passLogin=findViewById(R.id.PassLog)
        btnLogin=findViewById(R.id.Btn_IniciarSesion)
        nuevoUsuario=findViewById(R.id.Register)

        btnLogin.setOnClickListener{
            validarDatos()
        }

        nuevoUsuario.setOnClickListener {
            startActivity(Intent(this@Login,Registro::class.java))
        }
    }

    private fun validarDatos() {
        correo = correoLogin.text.toString()
        contraseña = passLogin.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
        } else if (contraseña.isEmpty()) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show()
        } else {
            loginDeUsuario()
        }
    }

    private fun loginDeUsuario() {
        firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    Toast.makeText(this@Login, "Bienvenido(a): ${user?.email}", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@Login, "Correo y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@Login, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}