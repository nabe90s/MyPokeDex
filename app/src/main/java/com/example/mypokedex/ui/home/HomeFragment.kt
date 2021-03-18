package com.example.mypokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.example.mypokedex.R
import com.example.mypokedex.databinding.FragmentHomeBinding
import com.example.mypokedex.model.Pokemon
import com.example.mypokedex.pokemon
import com.skydoves.whatif.whatIfNotNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeController: HomeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeController = HomeController(homeViewModel)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
            recyclerView.setController(homeController)
            recyclerView.layoutManager = GridLayoutManager(root.context, 3)
            recyclerView.setItemSpacingDp(8)
        }

        lifecycle.addObserver(homeViewModel)
        observeState()
    }

    private fun observeState() {
        homeViewModel.pokemonListLiveData.observe(viewLifecycleOwner) {
            homeController.setData(it)
        }
    }
}

class HomeController(
    private val viewModel: HomeViewModel
) : TypedEpoxyController<List<Pokemon>>() {

    override fun buildModels(data: List<Pokemon>?) {
        data.whatIfNotNullOrEmpty { list ->
            list.forEach {
                pokemon {
                    id(it.hashCode())
                    pokemon(it)
                    onClick { _ ->
                        viewModel.onPokemonClicked(it)
                    }
                }
            }
        }
    }
}