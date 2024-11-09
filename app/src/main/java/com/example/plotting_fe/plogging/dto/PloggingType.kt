package com.example.plotting_fe.plogging.dto

enum class PloggingType(val type: String) {
    DIRECT("선착순"),
    ASSIGN("승인제");

    override fun toString(): String {
        return type
    }
}