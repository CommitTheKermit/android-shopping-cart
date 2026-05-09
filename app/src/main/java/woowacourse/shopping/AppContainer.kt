package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import woowacourse.shopping.data.local.RecentProductDatabase
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.cart.CartRepository
import woowacourse.shopping.data.repository.cart.CartRepositoryImpl
import woowacourse.shopping.data.repository.cart.RecentProductRepositoryImpl
import woowacourse.shopping.data.repository.product.ProductRepository
import woowacourse.shopping.data.repository.product.ProductRepositoryImpl
import woowacourse.shopping.data.repository.recentproduct.RecentProductRepository

object AppContainer {
    private var database: ShoppingDatabase? = null
    private var _cartRepository: CartRepository? = null

    val cartRepository: CartRepository
        get() = checkNotNull(_cartRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    private var _productRepository: ProductRepository? = null

    val productRepository: ProductRepository
        get() = checkNotNull(_productRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    private var _recentProductRepository: RecentProductRepository? = null
    val recentProductRepository: RecentProductRepository
        get() = checkNotNull(_recentProductRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    val client = OkHttpClient()
    val json = Json { ignoreUnknownKeys = true }

    fun init(context: Context) {
        if (database != null) return
        val shoppingDatabase = Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping.db",
        ).build()
        database = shoppingDatabase
        _cartRepository = CartRepositoryImpl(shoppingDatabase.cartDao())

        val recentProductDatabase = Room.databaseBuilder(
            context.applicationContext,
            RecentProductDatabase::class.java,
            "shopping.db",
        ).build()
        _recentProductRepository = RecentProductRepositoryImpl(recentProductDatabase.recentProductDao())

        _productRepository = ProductRepositoryImpl(
            client = client,
            baseUrl = "http://localhost:12345/".toHttpUrl(),
            json = json,
        )
    }
}
