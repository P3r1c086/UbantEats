package com.cursosandroidant.ubanteats.cartModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.databinding.FragmentCartBinding
import com.google.android.material.transition.MaterialSharedAxis


/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats.cartModule
 * Created by Alain Nicolás Tello on 24/09/22 at 15:15
 * All rights reserved 2022.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Web: www.alainnicolastello.com
 ***/
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    
    private var products: Array<String>? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupButtons()
    }
    
    private fun setupRecyclerView() {
        products = CartFragmentArgs.fromBundle(requireArguments()).products
        val cartAdapter = CartAdapter(products ?: arrayOf())
    
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
            setHasFixedSize(true)
        }
    
        setupTotal(cartAdapter.itemCount.toFloat())
    }
    
    private fun setupTotal(total: Float) {
        binding.tvSum.text = getString(R.string.cart_sum, total)
    }
    
    private fun setupButtons(){
        binding.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_cart_to_products) }
    
        binding.btnPay.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_cart_to_confirmation)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}