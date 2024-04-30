package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.databinding.RgbLayoutDialogBinding


class IngresoMateriaActivity: AppCompatActivity() {

    private val rgbLayoutDialogBinding : RgbLayoutDialogBinding by lazy {
        RgbLayoutDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_materia)
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
