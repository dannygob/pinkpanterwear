@Singleton
class FirebaseCartRepositoryImpl @Inject constructor(
    private val productRepository: ProductRepository
) : CartRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override suspend fun getCartItems(userId: String): List<CartItem> =
        withContext(Dispatchers.IO) {
            require(userId.isNotBlank()) { "User ID cannot be empty" }

            val snapshot =
                usersCollection.document(userId).collection("cartItems").get().await()

            snapshot.documents.mapNotNull { document ->
                val firestoreItem = document.toObject(FirestoreCartItem::class.java)
                    ?: return@mapNotNull null

                val product =
                    productRepository.getProductById(firestoreItem.productId)
                        ?: return@mapNotNull null

                document.toCartItem(userId, product)
            }
        }

    override suspend fun addCartItem(
        userId: String,
        product: Product,
        quantity: Int
    ) = withContext(Dispatchers.IO) {

        require(userId.isNotBlank()) { "User ID cannot be empty" }
        require(quantity > 0) { "Quantity must be greater than zero" }

        val cartItemRef = usersCollection
            .document(userId)
            .collection("cartItems")
            .document(product.id.toString())

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(cartItemRef)
            if (snapshot.exists()) {
                val currentQty =
                    snapshot.toObject(FirestoreCartItem::class.java)?.quantity ?: 0
                transaction.update(cartItemRef, "quantity", currentQty + quantity)
            } else {
                transaction.set(
                    cartItemRef,
                    FirestoreCartItem(
                        productId = product.id,
                        quantity = quantity,
                        addedAt = Timestamp.now()
                    )
                )
            }
        }.await()
    }

    override suspend fun updateItemQuantity(
        userId: String,
        productId: Int,
        newQuantity: Int
    ) = withContext(Dispatchers.IO) {

        require(userId.isNotBlank()) { "User ID cannot be empty" }

        val ref = usersCollection.document(userId)
            .collection("cartItems")
            .document(productId.toString())

        if (newQuantity <= 0) {
            ref.delete().await()
        } else {
            ref.update("quantity", newQuantity).await()
        }
    }

    override suspend fun removeItem(
        userId: String,
        productId: Int
    ) = withContext(Dispatchers.IO) {

        require(userId.isNotBlank()) { "User ID cannot be empty" }

        usersCollection.document(userId)
            .collection("cartItems")
            .document(productId.toString())
            .delete()
            .await()
    }

    override suspend fun clearCart(userId: String) =
        withContext(Dispatchers.IO) {

            require(userId.isNotBlank()) { "User ID cannot be empty" }

            val snapshot =
                usersCollection.document(userId).collection("cartItems").get().await()

            if (snapshot.isEmpty) return@withContext

            firestore.batch().apply {
                snapshot.documents.forEach { delete(it.reference) }
            }.commit().await()
        }
}
``
