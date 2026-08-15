package com.example.desafio1_dsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class PromedioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etNota1 = findViewById<EditText>(R.id.etNota1)
        val etNota2 = findViewById<EditText>(R.id.etNota2)
        val etNota3 = findViewById<EditText>(R.id.etNota3)
        val etNota4 = findViewById<EditText>(R.id.etNota4)
        val etNota5 = findViewById<EditText>(R.id.etNota5)

        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnCalcular.setOnClickListener {

            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()) {
                etNombre.error = "Ingrese el nombre del estudiante"
                return@setOnClickListener
            }

            val notasTexto = listOf(
                etNota1.text.toString(),
                etNota2.text.toString(),
                etNota3.text.toString(),
                etNota4.text.toString(),
                etNota5.text.toString()
            )

            if (notasTexto.any { it.isEmpty() }) {
                Toast.makeText(
                    this,
                    "Ingrese todas las notas",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val notas = try {
                notasTexto.map { it.toDouble() }
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this,
                    "Ingrese valores numéricos válidos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (notas.any { it < 0 || it > 10 }) {
                Toast.makeText(
                    this,
                    "Las notas deben estar entre 0 y 10",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Cálculo temporal mientras definimos las ponderaciones oficiales
            val promedio = notas.average()

            val formato = DecimalFormat("0.00")
            val promedioFormateado = formato.format(promedio)

            // El documento no especifica en la parte disponible
            // cuál es la nota mínima para aprobar.
            val resultado = if (promedio >= 6.0) {
                "Aprobado"
            } else {
                "Reprobado"
            }

            tvResultado.text =
                "Estudiante: $nombre\nPromedio: $promedioFormateado\n$resultado"
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }
}