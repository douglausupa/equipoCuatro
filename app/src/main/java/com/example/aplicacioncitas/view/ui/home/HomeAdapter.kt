package com.example.aplicacioncitas.view.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacioncitas.R
import com.example.aplicacioncitas.model.Cita
import com.example.aplicacioncitas.model.ImagenRazaResponse
import com.example.aplicacioncitas.webservice.DogApiService
import com.example.aplicacioncitas.webservice.RetrofitRazas
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            tvNombreMascota.text = cita.nombreMascota
            tvSintoma.text = cita.sintomas ?: "No especificado"
            tvTurno.text = "#${cita.id}"

            val razaApi = normalizarRazaParaApi(cita.raza)

            val apiService = RetrofitRazas.instance.create(DogApiService::class.java)
            val call = apiService.obtenerImagenPorRaza(razaApi)

            call.enqueue(object : Callback<ImagenRazaResponse> {
                override fun onResponse(
                    call: Call<ImagenRazaResponse>,
                    response: Response<ImagenRazaResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val url = response.body()?.message
                        Glide.with(itemView.context)
                            .load(url)
                            .placeholder(R.drawable.ico_dog)
                            .error(R.drawable.ico_dog)
                            .into(imgMascota)
                    } else {
                        Glide.with(itemView.context).load(R.drawable.ico_dog).into(imgMascota)
                    }
                }

                override fun onFailure(call: Call<ImagenRazaResponse>, t: Throwable) {
                    Glide.with(itemView.context).load(R.drawable.ico_dog).into(imgMascota)
                }
            })

            itemView.setOnClickListener {
                onItemClick(cita)
            }
        }

        private fun normalizarRazaParaApi(raza: String): String {
            return raza.lowercase()
                .replace("[áàäâ]".toRegex(), "a")
                .replace("[éèëê]".toRegex(), "e")
                .replace("[íìïî]".toRegex(), "i")
                .replace("[óòöô]".toRegex(), "o")
                .replace("[úùüû]".toRegex(), "u")
                .replace("[^a-z/]".toRegex(), "")
                .replace(" ", "")
        }
    }

    fun updateList(newList: List<Cita>) {
        citas = newList
        notifyDataSetChanged()
    }
}