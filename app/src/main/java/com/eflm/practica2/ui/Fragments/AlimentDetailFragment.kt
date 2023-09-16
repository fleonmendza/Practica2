package com.eflm.practica2.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment


import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.eflm.practica2.application.AlimentsRFApp
import com.eflm.practica2.data.AlimentRepository
import com.eflm.practica2.data.remote.model.AlimentodetDto
import com.eflm.practica2.databinding.FragmentAlimentDetailBinding
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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            alimentoId = args.getString(ALIMENTO_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $alimentoId")

            repository = (requireActivity().application as AlimentsRFApp).repository

            lifecycleScope.launch {

                alimentoId?.let { id ->

                    val call: Call<AlimentodetDto> = repository.getAlimentDetail(id)

                    call.enqueue(object: Callback<AlimentodetDto> {
                        override fun onResponse(
                            call: Call<AlimentodetDto>,
                            response: Response<AlimentodetDto>
                        ) {
                            Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")
                            binding.apply {
                                tvTitle.text = response.body()?.nombre
                                tvLongDesc.text = response.body()?.descripcion

                                Glide.with(requireContext())
                                    .load(response.body()?.imagen)
                                    .into(ivImage)
                            }

                        }

                        override fun onFailure(call: Call<AlimentodetDto>, t: Throwable) {
                            Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
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