package com.eflm.practica2.ui.Fragments


import android.R

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.eflm.practica2.application.AlimentsRFApp
import com.eflm.practica2.data.AlimentRepository
import com.eflm.practica2.data.remote.model.AlimentodetDto
import com.eflm.practica2.databinding.FragmentAlimentDetailBinding
import com.eflm.practica2.ui.MapView
import com.eflm.practica2.ui.VideoPlayer
import com.eflm.practica2.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



private const val ALIMENTO_ID = "aliment_id"
class AlimentDetailFragment : Fragment() {

    private var alimentoId: String? = null
    private var _binding: FragmentAlimentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AlimentRepository
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), com.eflm.practica2.R.raw.comer)

        arguments?.let { args ->
            alimentoId = args.getString(ALIMENTO_ID)

            Log.d(Constants.LOGTAG, getString(com.eflm.practica2.R.string.id_recibido, alimentoId))

            repository = (requireActivity().application as AlimentsRFApp).repository

            lifecycleScope.launch {

                alimentoId?.let { id ->

                    val call: Call<AlimentodetDto> = repository.getAlimentDetail(id)

                    call.enqueue(object: Callback<AlimentodetDto> {
                        override fun onResponse(
                            call: Call<AlimentodetDto>,
                            response: Response<AlimentodetDto>
                        ) {
                            mediaPlayer?.start()
                            Log.d(Constants.LOGTAG,
                                getString(
                                    com.eflm.practica2.R.string.respuesta_del_servidor,
                                    response.body()
                                ))
                            val ingredientes1 = response.body()?.ingredientes
                            if (ingredientes1 != null) {
                                val ingredientesConGuion = ingredientes1.map { "- $it" } // Agrega un guion antes de cada ingrediente
                                val ingredientesConcatenados = ingredientesConGuion.joinToString("\n") // Concatena los ingredientes con saltos de línea
                                binding.tvIngre.text = ingredientesConcatenados
                            }
                            binding.apply {
                                pbLoading.visibility = View.GONE
                                tvTitle.text = response.body()?.nombre
                                tvLongDesc.text = response.body()?.descripcion
                                tvTime.text = response.body()?.tiempo_preparacion.toString().plus(
                                    getString(com.eflm.practica2.R.string.minutos))
                                tvbasePlatillo.text = response.body()?.basePlatillo

                                Glide.with(requireContext())
                                    .load(response.body()?.imagen)
                                    .into(ivImage)
                            }
                            binding.btnVideo.setOnClickListener {
                                val dataIntent = Intent(requireContext(), VideoPlayer::class.java).apply {
                                    putExtra("EXTRA_URLVIDEO", response.body()?.video)
                                }
                                startActivity(dataIntent)
                            }
                            binding.btnLocation.setOnClickListener {
                                val dataIntent = Intent(requireContext(), MapView::class.java).apply {
                                    putExtra("EXTRA_LAT", response.body()?.lat)
                                    putExtra("EXTRA_LONG", response.body()?.long)
                                    putExtra("EXTRA_NAME", response.body()?.nombre)
                                }
                                startActivity(dataIntent)
                            }

                        }

                        override fun onFailure(call: Call<AlimentodetDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(requireActivity(),
                                getString(com.eflm.practica2.R.string.no_hay_conexi_n), Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlimentDetailBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance(alimentoId: String) =
            AlimentDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ALIMENTO_ID, alimentoId)
                }
            }
    }



}