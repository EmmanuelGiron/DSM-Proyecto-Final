package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.R
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.databinding.RgbLayoutDialogBinding
import com.google.firebase.database.FirebaseDatabase

class IngresoMateriaActivity: AppCompatActivity() {
    //Conexion con firebase
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("materias")

    class Materia {
        var nombre = ""
        var descripcion = ""
        var color = ""
    }

    val materia = Materia()

    private val rgbLayoutDialogBinding : RgbLayoutDialogBinding by lazy {
        RgbLayoutDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrador_ingreso_materia)
        var arrowLeft: ImageView

        arrowLeft = findViewById(R.id.btn_back)

        arrowLeft.setOnClickListener{
            val intent = Intent(this, ListaMateria::class.java)
            startActivity(intent)
        }

        //Datos form de ingreso de materia
        var txtNombreMateria = EditText(this)
        var txtDescripcionMateria = EditText(this)
        var btnIngresarMateria = Button(this)

        txtNombreMateria = findViewById(R.id.txtMateria)
        txtDescripcionMateria = findViewById(R.id.txtDescripcionMateria)
        btnIngresarMateria = findViewById(R.id.btnIngresarMateria)

        //Parte del color picker
        val colorTxt = findViewById<TextView>(R.id.colorTxt)
        val pickColorBtn = findViewById<Button>(R.id.pickColorBtn)


        val rgbDialog = Dialog(this).apply {
            setContentView(rgbLayoutDialogBinding.root)
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
        }

        setOnSeekbar(
            "R",
            rgbLayoutDialogBinding.redLayout.typeTxt,
            rgbLayoutDialogBinding.redLayout.seekBar,
            rgbLayoutDialogBinding.redLayout.colorValueTxt
        )
        setOnSeekbar(
            "G",
            rgbLayoutDialogBinding.greenLayout.typeTxt,
            rgbLayoutDialogBinding.greenLayout.seekBar,
            rgbLayoutDialogBinding.greenLayout.colorValueTxt
        )
        setOnSeekbar(
            "B",
            rgbLayoutDialogBinding.blueLayout.typeTxt,
            rgbLayoutDialogBinding.blueLayout.seekBar,
            rgbLayoutDialogBinding.blueLayout.colorValueTxt
        )
        rgbLayoutDialogBinding.cancelBtn.setOnClickListener {
            rgbDialog.dismiss()
        }
        rgbLayoutDialogBinding.pickBtn.setOnClickListener {
            colorTxt.text = setRGBColor()
            rgbDialog.dismiss()
        }


        pickColorBtn.setOnClickListener {
            rgbDialog.show()
        }

        //Ingreso de una materia a la base
        btnIngresarMateria.setOnClickListener{
            materia.nombre = txtNombreMateria.text.toString()
            materia.descripcion = txtDescripcionMateria.text.toString()
            materia.color = colorTxt.text.toString()

            val builder = AlertDialog.Builder(this)

            if(materia.nombre != "" && materia.descripcion != "")
            {  //Ingreso a la base de datos firebase
                val ticketRef = reference.push()
                ticketRef.setValue(materia)

                //Alerta de confirmacion

                builder.setTitle("Resultado")
                builder.setMessage("Materia ingresada con éxito!")

                builder.setPositiveButton("Aceptar") { dialog, which ->
                    val intent = Intent(this, ListaMateria::class.java)
                    startActivity(intent)
                    //txtNombreMateria.setText("")
                    //txtDescripcionMateria.setText("")
                    //colorTxt.setText("#000000")

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

    private fun setOnSeekbar(type:String, typeTxt:TextView,seekBar: SeekBar,colorTxt:TextView) {
        typeTxt.text = type
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                colorTxt.text = seekBar.progress.toString()
                setRGBColor()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        colorTxt.text = seekBar.progress.toString()
    }

    private fun setRGBColor():String {
        val hex = String.format(
            "#%02x%02x%02x",
            rgbLayoutDialogBinding.redLayout.seekBar.progress,
            rgbLayoutDialogBinding.greenLayout.seekBar.progress,
            rgbLayoutDialogBinding.blueLayout.seekBar.progress
        )
        rgbLayoutDialogBinding.colorView.setBackgroundColor(Color.parseColor(hex))
        return hex
    }
}
