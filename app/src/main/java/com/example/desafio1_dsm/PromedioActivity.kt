package com.example.desafio1_dsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PromedioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        val etNota1 = findViewById<EditText>(R.id.etNota1)
        val etNota2 = findViewById<EditText>(R.id.etNota2)
        val etNota3 = findViewById<EditText>(R.id.etNota3)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnCalcular.setOnClickListener {

            val nota1 = etNota1.text.toString().toDoubleOrNull()
            val nota2 = etNota2.text.toString().toDoubleOrNull()
            val nota3 = etNota3.text.toString().toDoubleOrNull()

            if (nota1 == null || nota2 == null || nota3 == null) {
                Toast.makeText(
                    this,
                    "Ingrese las tres notas",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val promedio = (nota1 + nota2 + nota3) / 3
                tvResultado.text = "Promedio: %.2f".format(promedio)
            }
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }
}