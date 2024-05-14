package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import android.graphics.drawable.ColorDrawable
import android.widget.RelativeLayout

class CustomAdapterMateria(private var usuarioActual: String): RecyclerView.Adapter<CustomAdapterMateria.ViewHolder>() {
    //Usuario
    val usurioActual = ""
    //Trayendo datos de la base
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("materias")

    var referencias = mutableListOf<String>()
    var materias = mutableListOf<String>()
    var descripciones = mutableListOf<String>()
    var colores = mutableListOf<String>()


    init {
        obteniendoDatos(usurioActual)
    }

    class materia {
        var referencia: String? = null
        var nombre: String? = null
        var descripcion: String? = null
        var color: String? = null
        constructor() {
        }

        constructor(referencia: String, nombre: String, descripcion: String, color:String) {
            this.referencia = referencia
            this.nombre = nombre
            this.descripcion = descripcion
            this.color = color
        }
    }

    fun obteniendoDatos(nombreActual:String) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val materia = snapshot.getValue(materia::class.java)
                    val childKey = snapshot.key

                        if (materia != null) {
                            referencias.add(childKey.toString())
                            materias.add(materia.nombre.toString().trim())
                            descripciones.add(materia.descripcion.toString())
                            colores.add(materia.color.toString())
                        }else{

                        }
                }
                notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_materia,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemReferenciaMateria.text = referencias[i]
        viewHolder.itemNombreMateria.text = materias[i]
        viewHolder.itemDescripcionMateria.text = descripciones[i]
        val color = ColorDrawable(Color.parseColor(colores[i]))
        viewHolder.itemLayOutColorMateria.background = color
        println(usuarioActual)
        if(usuarioActual == "Administrador"){
            viewHolder.itemEditar.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return materias.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemReferenciaMateria: TextView
        var itemNombreMateria: TextView
        var itemDescripcionMateria: TextView
        var itemEditar: Button
        var itemLayOutColorMateria : LinearLayout
        var itemMateriaLayout : RelativeLayout

        init {
            itemReferenciaMateria = itemView.findViewById(R.id.referenciaMateriaCardID)
            itemNombreMateria = itemView.findViewById(R.id.nombreMateriaCardID)
            itemDescripcionMateria = itemView.findViewById(R.id.descripcionMateriaCardID)
            itemEditar = itemView.findViewById(R.id.btnEditarMateria)
            itemLayOutColorMateria = itemView.findViewById(R.id.colorMateriaLayout)
            itemMateriaLayout = itemView.findViewById(R.id.MateriaLayout)



            //Actualizar datos
            itemEditar.setOnClickListener{
                val intent = Intent(itemEditar.context,ActualizarMateria::class.java)
                intent.putExtra("referencia",itemReferenciaMateria.text.toString())
                intent.putExtra("nombre",itemNombreMateria.text.toString())
                intent.putExtra("materiaActual",itemNombreMateria.text.toString())
                intent.putExtra("descripcion",itemDescripcionMateria.text.toString())
                val backgroundColor = (itemLayOutColorMateria.background as ColorDrawable).color
                val backgroundColorHex = String.format("#%06X", 0xFFFFFF and backgroundColor)
                intent.putExtra("color", backgroundColorHex)
                itemEditar.context.startActivity(intent)
            }

            itemMateriaLayout.setOnClickListener{
                val intent = Intent(itemMateriaLayout.context,ListaTermino::class.java)
                intent.putExtra("nombre",itemNombreMateria.text.toString())
                intent.putExtra("usuarioActual",usuarioActual)
                itemMateriaLayout.context.startActivity(intent)
            }

        }
    }
}