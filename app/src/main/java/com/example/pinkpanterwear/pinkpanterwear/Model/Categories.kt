package com.example.slickkwear.Model

class Categories {
    var categoryUniqueID: String? = null
    var categoryName: String? = null
    var categoryImage: String? = null
    var categoryStatus: String? = null
    var categoryDeleted: String? = null
    var dateCreated: String? = null
    var timeCreated: String? = null

    constructor()

    constructor(
        categoryUniqueID: String?,
        categoryName: String?,
        categoryImage: String?,
        categoryStatus: String?,
        categoryDeleted: String?,
        dateCreated: String?,
        timeCreated: String?
    ) {
        this.categoryUniqueID = categoryUniqueID
        this.categoryName = categoryName
        this.categoryImage = categoryImage
        this.categoryStatus = categoryStatus
        this.categoryDeleted = categoryDeleted
        this.dateCreated = dateCreated
        this.timeCreated = timeCreated
    }
}
