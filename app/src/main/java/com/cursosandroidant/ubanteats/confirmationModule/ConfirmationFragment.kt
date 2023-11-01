package com.cursosandroidant.ubanteats.confirmationModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.databinding.FragmentConfirmationBinding
import com.google.android.material.transition.MaterialSharedAxis

/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats.confirmationModule
 * Created by Alain Nicolás Tello on 14/09/22 at 15:15
 * All rights reserved 2022.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Web: www.alainnicolastello.com
 ***/
class ConfirmationFragment : Fragment(){
    
    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentConfirmationBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupButtons()
    }
    
    private fun setupButtons() {
        binding.btnDone.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnDone.isEnabled = false
//            NavHostFragment.findNavController(this)
//                .navigate(R.id.action_confirmation_to_tracking)
            val action = ConfirmationFragmentDirections.actionConfirmationToTracking()
            val totalProducts = ConfirmationFragmentArgs.fromBundle(requireArguments()).totalProducts
            action.totalProducts = totalProducts
            NavHostFragment.findNavController(this).navigate(action)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}