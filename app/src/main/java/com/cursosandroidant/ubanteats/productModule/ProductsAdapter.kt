package com.cursosandroidant.ubanteats.productModule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.common.entities.Product
import com.cursosandroidant.ubanteats.databinding.ItemProductBinding

/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats.productModule
 * Created by Alain Nicolás Tello on 14/10/22 at 15:15
 * All rights reserved 2022.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Web: www.alainnicolastello.com
 ***/
class ProductsAdapter(private val products: List<Product>, private val listener: OnClickListener) :
        RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.setListener(product, listener)
        holder.binding.tvName.text = product.name

        Glide.with(context)
            .load(product.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().centerCrop())
            .into(holder.binding.imgPhoto)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemProductBinding.bind(itemView)
        
        fun setListener(product: Product, listener: OnClickListener) {
            binding.root.setOnClickListener {
                product.isSelected = !product.isSelected
                binding.root.isChecked = product.isSelected
                listener.onClick(product)
            }
        }
    }
}