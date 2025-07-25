package com.example.pink.Fragment

data class SpinnerItems(
    val spinnerItemName: String?,
    val spinnerItemID: String?,
) {
    override fun toString(): String {
        return spinnerItemName ?: ""
    }
}