package woowacourse.shopping.data

import woowacourse.shopping.domain.Product

interface ProductRepository {
    suspend fun loadProducts(
        startIndex: Int,
        pageSize: Int,
    ): List<Product>
}
