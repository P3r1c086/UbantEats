package com.cursosandroidant.ubanteats.productModule

import com.cursosandroidant.ubanteats.common.entities.Product

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
interface OnClickListener {
    fun onClick(product: Product)
}