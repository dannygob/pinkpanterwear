package com.example.slickkwear.Model

class Products {
    var productUniqueID: String? = null
    var productName: String? = null
    var productDescription: String? = null
    var productImage: String? = null
    var productCategory: String? = null
    var productStatus: String? = null
    var dateCreated: String? = null
    var timeCreated: String? = null
    var productPrice: Int = 0

    constructor()

    constructor(
        productUniqueID: String?,
        productName: String?,
        productPrice: Int,
        productDescription: String?,
        productImage: String?,
        productCategory: String?,
        productStatus: String?,
        dateCreated: String?,
        timeCreated: String?
    ) {
        this.productUniqueID = productUniqueID
        this.productName = productName
        this.productPrice = productPrice
        this.productDescription = productDescription
        this.productImage = productImage
        this.productCategory = productCategory
        this.productStatus = productStatus
        this.dateCreated = dateCreated
        this.timeCreated = timeCreated
    }
}
