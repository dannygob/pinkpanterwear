package com.example.slickkwear

class SpinnerItems //    public SpinnerItems(String namePart, Object idPart) {
//        spinnerItemName = namePart;
//        spinnerItemID = idPart;
//    }
    (var spinnerItemName: String, var spinnerItemID: String) {
    override fun toString(): String {
        return spinnerItemName
    }
}