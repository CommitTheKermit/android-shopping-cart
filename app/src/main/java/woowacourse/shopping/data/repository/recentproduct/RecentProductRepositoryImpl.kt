package woowacourse.shopping.data.repository.cart

import woowacourse.shopping.data.local.recentproduct.RecentProductDao
import woowacourse.shopping.data.local.recentproduct.RecentProductEntity
import woowacourse.shopping.data.repository.recentproduct.RecentProductRepository

class RecentProductRepositoryImpl(
    private val recentProductDao: RecentProductDao,
) : RecentProductRepository {
    override suspend fun loadProducts(): List<String> {
        return recentProductDao.findAll()
    }

    override suspend fun insert(id: String) {
        recentProductDao.insert(
            RecentProductEntity(
                productId = id,
            ),
        )
    }
}
