package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Registro.Login
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListaMateria: AppCompatActivity() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val correoUsuarioActual = currentUser?.email
    var firebaseAuth: FirebaseAuth? = null
    var usuarioActual = ""

    val database = FirebaseDatabase.getInstance()
    val referencemateria = database.getReference("materias")
    var materias = mutableListOf<String>()

    class materia {
        var nombre: String? = null
        constructor() {
        }

        constructor(referencia: String) {
            this.nombre = nombre
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_lista_materias)
        showProgressDialog()

        val spMaterias: Spinner = findViewById(R.id.spinnerMaterias)
        val toolbar: Toolbar = findViewById(R.id.toolbarMateria)
        val btnCerrarSesion: ImageView = findViewById(R.id.btn_cerrar_sesion)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
        }
        val btnAgregarMateria: Button = findViewById(R.id.btnAgregarMateria)

        if (currentUser != null) {
            if (correoUsuarioActual != null) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")
                databaseReference.orderByChild("correo").equalTo(correoUsuarioActual)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                usuarioActual = snapshot.children.firstOrNull()?.child("nombres")?.getValue(String::class.java).toString()
                                SessionManager.iniciarSesion(usuarioActual)
                                if (usuarioActual == "Administrador") {
                                    toolbar.setBackgroundColor(Color.parseColor("#E8E8E8"))
                                    btnAgregarMateria.visibility = View.VISIBLE
                                }
                                val recyclerView = findViewById<RecyclerView>(R.id.recyclerviewMaterias) ?: return
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

        obteniendoDatos { loadedMaterias ->
            // Asegúrate de que la lista es mutable
            val mutableLoadedMaterias = loadedMaterias.toMutableList()
            // Agregar "Selecciona una materia" al inicio de la lista
            mutableLoadedMaterias.add(0, "Selecciona una materia")

            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableLoadedMaterias)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMaterias.adapter = arrayAdapter

            spMaterias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    // Verificar que no sea "Selecciona una materia"
                    if (selectedItem != "Selecciona una materia") {
                        val intent = Intent(this@ListaMateria, ListaTermino::class.java).apply {
                            putExtra("nombre", selectedItem)
                            putExtra("usuarioActual", usuarioActual)
                        }
                        startActivity(intent)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No se seleccionó nada
                }
            }
        }

        btnCerrarSesion.setOnClickListener {
            showLogoutDialog()
        }
        btnAgregarMateria.setOnClickListener {
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
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showProgressDialog() {
        val dialogView = layoutInflater.inflate(R.layout.progress_bar, null)
        val progressDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        progressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
        }, 2500)
    }

    private fun obteniendoDatos(callback: (List<String>) -> Unit) {
        referencemateria.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loadedMaterias = mutableListOf<String>()
                for (snapshot in dataSnapshot.children) {
                    val materia = snapshot.getValue(materia::class.java)
                    if (materia != null) {
                        loadedMaterias.add(materia.nombre.toString().trim())
                    }
                }
                callback(loadedMaterias)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores
            }
        })
    }
}
