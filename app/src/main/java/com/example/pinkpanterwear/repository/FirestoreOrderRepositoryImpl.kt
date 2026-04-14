class FirestoreOrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val ordersCollection = firestore.collection("orders")

    override suspend fun getAllOrders(): List<Order> =
        withContext(Dispatchers.IO) {

            val snapshot = ordersCollection
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(Order::class.java)
            }
        }

    override suspend fun getOrderItems(orderId: String): List<OrderItem> =
        withContext(Dispatchers.IO) {

            require(orderId.isNotBlank()) { "Order ID cannot be empty" }

            val snapshot = ordersCollection
                .document(orderId)
                .collection("orderItems")
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(OrderItem::class.java)
            }
        }

    override suspend fun getOrderById(orderId: String): Order? =
        withContext(Dispatchers.IO) {

            require(orderId.isNotBlank()) { "Order ID cannot be empty" }

            ordersCollection
                .document(orderId)
                .get()
                .await()
                .toObject(Order::class.java)
        }

    override suspend fun createOrder(
        order: Order,
        items: List<OrderItem>
    ): String = withContext(Dispatchers.IO) {

        require(items.isNotEmpty()) { "Order must contain at least one item" }

        val newOrderRef = ordersCollection.document()

        val orderToSave =
            order.copy(orderId = newOrderRef.id)

        firestore.batch().apply {

            set(newOrderRef, orderToSave)

            items.forEach { item ->
                set(
                    newOrderRef
                        .collection("orderItems")
                        .document(item.productId.toString()),
                    item
                )
            }
        }.commit().await()

        newOrderRef.id
    }
}
``
