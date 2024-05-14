package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Registro.Login
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*

class ListaMateria: AppCompatActivity() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val correoUsuarioActual = currentUser?.email
    var firebaseAuth: FirebaseAuth? = null
    var usuarioActual = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_materias)
        showProgressDialog()
        //println(correoUsuarioActual)

        val toolbar: Toolbar = findViewById(R.id.toolbarMateria)
        var btnCerrarSesion: ImageView
         btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
        }
        var btnAgregarMateria: Button
        btnAgregarMateria = findViewById(R.id.btnAgregarMateria)


        if (currentUser != null) {
            if (correoUsuarioActual != null) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")
                databaseReference.orderByChild("correo").equalTo(correoUsuarioActual)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                usuarioActual = snapshot.children.firstOrNull()?.child("nombres")?.getValue(String::class.java)
                                    .toString()
                                //println(usuarioActual)
                                SessionManager.iniciarSesion(usuarioActual)
                                if(usuarioActual == "Administrador"){
                                    toolbar.setBackgroundColor(Color.parseColor("#E8E8E8"))
                                    btnAgregarMateria.visibility = View.VISIBLE
                                }
                                val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewMaterias)?: return
                                val adapter = CustomAdapterMateria(usuarioActual)
                                recyclerView.layoutManager = LinearLayoutManager(this@ListaMateria)
                                recyclerView.adapter = adapter
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }



        btnCerrarSesion.setOnClickListener {
            showLogoutDialog()
        }
        btnAgregarMateria.setOnClickListener{
            val intent = Intent(this, IngresoMateriaActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun SalirAplicacion() {
        firebaseAuth?.signOut()
        startActivity(Intent(this@ListaMateria, Login::class.java))
        Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show()
    }
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage("¿Cerrar Sesión?")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            SalirAplicacion()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            // Aquí puedes añadir cualquier acción que quieras que ocurra al cancelar
            dialog.dismiss()
        }
        builder.show()
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