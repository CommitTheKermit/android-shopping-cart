package woowacourse.shopping.data

import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.CartContent
import woowacourse.shopping.domain.Product

interface CartRepository {
    suspend fun loadCart(): Cart
    suspend fun loadCartContents(): List<CartContent>
    suspend fun loadCartSize(): Int
    suspend fun increase(product: Product)
    suspend fun decrease(productId: String)
}
