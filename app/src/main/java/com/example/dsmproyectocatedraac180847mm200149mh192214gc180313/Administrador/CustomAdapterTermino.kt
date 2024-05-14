package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CustomAdapterTermino(var materiaA: String, var usuarioActual: String): RecyclerView.Adapter<CustomAdapterTermino.ViewHolder>() {
    //Trayendo datos de la base
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("terminos")

    var referencias = mutableListOf<String>()
    var terminos = mutableListOf<String>()
    var descripciones = mutableListOf<String>()
    var imagenes = mutableListOf<String>()
    var materias = mutableListOf<String>()

    class termino {
        var referencia: String? = null
        var nombre: String? = null
        var descripcion: String? = null
        var imagen: String? = null
        var materia: String? = null
        constructor() {
        }

        constructor(referencia: String, nombre: String, descripcion: String, imagen:String, materia:String) {
            this.referencia = referencia
            this.nombre = nombre
            this.descripcion = descripcion
            this.imagen = imagen
            this.materia = materia
        }
    }
    init {

        obteniendoDatos(materiaA)
    }

    fun obteniendoDatos(materiaActual:String) {
        //println(materiaActual)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val termino = snapshot.getValue(termino::class.java)
                    val childKey = snapshot.key
                    if (termino != null && materiaActual == termino.materia.toString()) {
                        referencias.add(childKey.toString())
                        terminos.add(termino.nombre.toString())
                        descripciones.add(termino.descripcion.toString())
                        imagenes.add(termino.imagen.toString())
                        materias.add(termino.materia.toString())

                    }else{

                    }
                }
                notifyDataSetChanged()
                //println(referencias)
                //println(materias)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_termino,viewGroup,false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemReferenciaTermino.text = referencias[i]
        viewHolder.itemNombreTermino.text = terminos[i]
        viewHolder.itemDescripcionTermino.text = descripciones[i]
        val uri: Uri = Uri.parse(imagenes[i])
        viewHolder.itemImagenTermino.setImageURI(uri)
        Picasso.get().load(uri).into(viewHolder.itemImagenTermino)
        viewHolder.itemURLImagen.text = imagenes[i]

        if(usuarioActual == "Administrador"){
            viewHolder.itemEditar.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return terminos.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemReferenciaTermino: TextView
        var itemNombreTermino: TextView
        var itemDescripcionTermino: TextView
        var itemImagenTermino: ImageView
        var itemURLImagen: TextView
        var itemEditar: Button
        var itemTerminoLayout : RelativeLayout





        init {
            itemReferenciaTermino = itemView.findViewById(R.id.referenciaTerminoCardID)
            itemNombreTermino = itemView.findViewById(R.id.nombreTerminoCardID)
            itemDescripcionTermino = itemView.findViewById(R.id.descripcionTerminoCardID)
            itemImagenTermino = itemView.findViewById(R.id.imagenTerminoCardID)
            itemURLImagen = itemView.findViewById(R.id.urlImagenTerminoCardID)
            itemEditar = itemView.findViewById(R.id.btnEditarTermino)
            itemTerminoLayout = itemView.findViewById(R.id.TerminoLayout)

            //Actualizar datos
            itemEditar.setOnClickListener{
                val intent = Intent(itemEditar.context,ActualizarTermino::class.java)
                intent.putExtra("referencia",itemReferenciaTermino.text.toString())
                intent.putExtra("nombre",itemNombreTermino.text.toString())
                intent.putExtra("descripcion",itemDescripcionTermino.text.toString())
                intent.putExtra("imagen",itemURLImagen.text.toString())
                intent.putExtra("materiaActual",materiaA)
                intent.putExtra("usuarioActual",usuarioActual)
                itemEditar.context.startActivity(intent)
            }
            if(usuarioActual != "Administrador"){
            itemTerminoLayout.setOnClickListener{
                val intent = Intent(itemTerminoLayout.context,flipcard::class.java)
                intent.putExtra("nombre",itemNombreTermino.text.toString())
                intent.putExtra("descripcion",itemDescripcionTermino.text.toString())
                intent.putExtra("imagen",itemURLImagen.text.toString())
                intent.putExtra("materiaActual",materiaA)
                itemTerminoLayout.context.startActivity(intent)
                }
            }

        }
    }




}