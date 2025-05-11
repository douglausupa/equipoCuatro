package com.example.aplicacioncitas.view.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.model.Cita
import de.hdodenhof.circleimageview.CircleImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class HomeAdapter(
    private var citas: List<Cita>,
    private val onItemClick: (Cita) -> Unit
) : RecyclerView.Adapter<HomeAdapter.CitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(citas[position])
    }

    override fun getItemCount(): Int = citas.size

    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgMascota: CircleImageView = itemView.findViewById(R.id.imgMascota)
        private val tvNombreMascota: TextView = itemView.findViewById(R.id.tvNombreMascota)
        private val tvSintoma: TextView = itemView.findViewById(R.id.tvSintoma)
        private val tvTurno: TextView = itemView.findViewById(R.id.tvTurno)

        fun bind(cita: Cita) {
            // Llenando los campos con los datos de la cita
            tvNombreMascota.text = cita.nombreMascota
            tvSintoma.text = cita.sintomas ?: "No especificado"
            tvTurno.text = "#${cita.id}"

            // Lógica para elegir la imagen de la mascota según la raza
            val raza = cita.raza.lowercase().trim()
            val razaImagen = when {
                "pastor" in raza -> R.drawable.pastor_aleman
                "pitbull" in raza -> R.drawable.pitbull
                "labrador" in raza -> R.drawable.labrador
                "criollo" in raza -> R.drawable.perro_criollo
                "doberman" in raza -> R.drawable.doberman
                else -> R.drawable.ico_dog // Imagen por defecto si no se encuentra la raza
            }

            // Usamos Glide para cargar la imagen de la mascota
            Glide.with(itemView.context)
                .load(razaImagen)
                .into(imgMascota)

            // Configuramos el click listener en el itemView
            itemView.setOnClickListener {
                onItemClick(cita)
            }
        }
    }

    // Función para actualizar la lista de citas
    fun updateList(newList: List<Cita>) {
        citas = newList
        notifyDataSetChanged()
    }
}