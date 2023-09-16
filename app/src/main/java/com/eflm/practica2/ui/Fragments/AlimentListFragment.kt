package com.eflm.practica2.ui.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eflm.practica2.R
import com.eflm.practica2.application.AlimentsRFApp
import com.eflm.practica2.data.AlimentRepository
import com.eflm.practica2.data.remote.model.AlimentoDto
import com.eflm.practica2.databinding.FragmentAlimentListBinding
import com.eflm.practica2.ui.Adapters.AlimentsAdapter
import com.eflm.practica2.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlimentListFragment : Fragment() {

    private var _binding: FragmentAlimentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AlimentRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlimentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as AlimentsRFApp).repository

        lifecycleScope.launch {
            val call: Call<List<AlimentoDto>> = repository.getAllAliments()

            call.enqueue(object: Callback<List<AlimentoDto>> {
                override fun onResponse(
                    call: Call<List<AlimentoDto>>,
                    response: Response<List<AlimentoDto>>
                ) {

                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")

                    response.body()?.let{ aliment ->
                        binding.rvAliments.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = AlimentsAdapter(aliment){ aliment ->
                               aliment.id?.let { id ->
                                    //Aquí va el código para la operación para ver los detalles
                                   requireActivity().supportFragmentManager.beginTransaction()
                                       .replace(R.id.fragment_container, AlimentDetailFragment.newInstance(id))
                                       .addToBackStack(null)
                                       .commit()
                                }
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "Error: ${t.message}")

                    Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()

                }

            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}



