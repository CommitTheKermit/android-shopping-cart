package woowacourse.shopping.domain

class CartContent(val product: Product, private val quantity: Int) {
    init {
        require(quantity > 0) { "수량은 1보다 작을 수 없습니다. 수량 : $quantity" }
    }

    fun addQuantity(
        productId: String,
        append: Int,
    ): CartContent {
        require(hasProductId(productId)) { "같은 상품만 더할 수 있습니다." }
        return CartContent(product, quantity + append)
    }

    val productId: String get() = product.id
    fun hasProductId(id: String): Boolean = productId == id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CartContent

        if (product != other.product) return false
        if (quantity != other.quantity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = 31 * result + quantity.hashCode()
        return result
    }
}
