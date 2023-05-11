package com.cursosandroidant.ubanteats.common.entities

/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats.common.entities
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
data class Product(
    var name: String,
    var photoUrl: String,
    var isSelected: Boolean = false)