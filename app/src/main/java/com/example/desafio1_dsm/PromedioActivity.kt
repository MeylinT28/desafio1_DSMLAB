package com.example.desafio1_dsm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.DecimalFormat

class PromedioActivity : AppCompatActivity() {

    companion object {
        private const val CANAL_ID = "resultado_promedio"
        private const val NOTIFICATION_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        crearCanalNotificacion()

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
                etNombre.error = getString(R.string.error_nombre)
                return@setOnClickListener
            }

            val notasTexto = listOf(
                etNota1.text.toString().trim(),
                etNota2.text.toString().trim(),
                etNota3.text.toString().trim(),
                etNota4.text.toString().trim(),
                etNota5.text.toString().trim()
            )

            if (notasTexto.any { it.isEmpty() }) {
                Toast.makeText(
                    this,
                    getString(R.string.error_campos),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val notas = try {
                notasTexto.map { it.toDouble() }
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this,
                    getString(R.string.error_numero),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (notas.any { it < 0 || it > 10 }) {
                Toast.makeText(
                    this,
                    getString(R.string.error_notas),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val promedio = calcularPromedio(notas)

            val formato = DecimalFormat("0.00")
            val promedioFormateado = formato.format(promedio)

            // Valor provisional mientras se confirma la nota mínima oficial.
            val resultado = if (promedio >= 6.0) {
                getString(R.string.aprobado)
            } else {
                getString(R.string.reprobado)
            }

            val textoEstudiante =
                getString(R.string.resultado_estudiante, nombre)

            val textoPromedio =
                getString(R.string.resultado_promedio_valor, promedioFormateado)

            tvResultado.text =
                "$textoEstudiante\n$textoPromedio\n$resultado"

            mostrarNotificacion(
                nombre,
                promedioFormateado,
                resultado
            )
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun calcularPromedio(notas: List<Double>): Double {
        return notas.average()
    }

    private fun crearCanalNotificacion() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val canal = NotificationChannel(
                CANAL_ID,
                getString(R.string.canal_notificacion_nombre),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            canal.description =
                getString(R.string.canal_notificacion_descripcion)

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun mostrarNotificacion(
        nombre: String,
        promedio: String,
        resultado: String
    ) {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )

            return
        }

        val contenido = getString(
            R.string.notificacion_contenido,
            nombre,
            promedio,
            resultado
        )

        val notification = NotificationCompat.Builder(
            this,
            CANAL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(
                getString(R.string.notificacion_titulo)
            )
            .setContentText(contenido)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(this)
            .notify(1, notification)
    }
}