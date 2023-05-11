package com.cursosandroidant.ubanteats.cartModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.databinding.ItemCartBinding


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

class CartAdapter(private val products: Array<String>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productStr = products[position]
        holder.binding.tvName.text = productStr
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCartBinding = ItemCartBinding.bind(itemView)
    }
}