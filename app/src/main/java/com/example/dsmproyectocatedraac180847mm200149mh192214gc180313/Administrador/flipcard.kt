package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.squareup.picasso.Picasso

class flipcard : AppCompatActivity() {

    lateinit var front_anim:AnimatorSet
    lateinit var back_anim: AnimatorSet
    var isFront =true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipcard)

        var nombreTermino = TextView(this)
        var definicionTermino = TextView(this)
        var imagenTermino = TextView(this)
        var materiaTermino = TextView(this)
        var imagenTerminoCard: ImageView

        nombreTermino = findViewById(R.id.txtNombreTermino)
        definicionTermino = findViewById(R.id.txtDefinicionTermino)
        imagenTermino = findViewById(R.id.txtImagenTermino)
        materiaTermino = findViewById(R.id.txtMateriaTermino)
        imagenTerminoCard = findViewById(R.id.imagenTermino)

        val extras = intent.extras
        nombreTermino.text = extras?.getString("nombre").toString()
        definicionTermino.text = extras?.getString("descripcion").toString()
        imagenTermino.text = extras?.getString("imagen").toString()
        materiaTermino.text = extras?.getString("materiaActual").toString()

        val uri: Uri = Uri.parse(imagenTermino.text.toString())
        imagenTerminoCard.setImageURI(uri)
        Picasso.get().load(uri).into(imagenTerminoCard)

        var arrowLeft: ImageView
        arrowLeft = findViewById(R.id.btn_back)

        arrowLeft.setOnClickListener{
            val intent = Intent(this, ListaTermino::class.java)
            intent.putExtra("nombre",materiaTermino.text.toString())
            startActivity(intent)
        }

        // Now Create Animator Object
        // For this we add animator folder inside res
        // Now we will add the animator to our card
        // we now need to modify the camera scale
        var scale = applicationContext.resources.displayMetrics.density

        val front = findViewById<TextView>(R.id.card_front) as TextView
        val back =findViewById<TextView>(R.id.card_back) as TextView
        val flip = findViewById<Button>(R.id.flip_btn) as ImageView

        front.cameraDistance = 8000 * scale
        back.cameraDistance = 8000 * scale

        front.setText(nombreTermino.text.toString())
        back.setText(definicionTermino.text.toString())
        // Now we will set the front animation
        front_anim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator) as AnimatorSet

        // Now we will set the event listener
        flip.setOnClickListener{
            if(isFront)
            {
                front_anim.setTarget(front);
                back_anim.setTarget(back);
                front_anim.start()
                back_anim.start()
                imagenTerminoCard.visibility = View.VISIBLE
                front.setText(nombreTermino.text.toString())
                isFront = false
            }
            else
            {
                front_anim.setTarget(back)
                back_anim.setTarget(front)
                back_anim.start()
                front_anim.start()
                imagenTerminoCard.visibility = View.INVISIBLE
                back.setText(definicionTermino.text.toString())
                isFront =true
            }
        }
    }
}