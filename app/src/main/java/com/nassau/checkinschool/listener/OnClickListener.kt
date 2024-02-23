package com.example.desafio.listener

interface OnClickListener<in T> {

    fun onClick(value: T, position: Int)
}