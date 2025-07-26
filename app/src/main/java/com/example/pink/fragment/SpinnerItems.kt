package com.example.pink.fragment

data class SpinnerItems(
    val spinnerItemName: String?,
    val spinnerItemID: String?,
) {
    override fun toString(): String {
        return spinnerItemName ?: ""
    }
}