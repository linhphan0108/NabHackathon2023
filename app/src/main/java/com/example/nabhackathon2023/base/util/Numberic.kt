package com.example.nabhackathon2023.base.util

fun Float.shorten(): String{
    return if (this <= 1000f){
        String.format("%.1f", this)
    }else if (this <= 1_000_000f){
        String.format("%.1fK", this/1000)
    }else if (this <= 1_000_000_000f){
        String.format("%.1fM", this/1000_000)
    }else{
        String.format("%.1fB", this/1000_000_000)
    }
}