package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.local.ShoppingDatabase

object AppContainer {
    private var database: ShoppingDatabase? = null
    private var _cartRepository: CartRepository? = null

    private var _productRepository: ProductRepository? = null

    val cartRepository: CartRepository
        get() = checkNotNull(_cartRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    val productRepository: ProductRepository
        get() = checkNotNull(_productRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    val client = OkHttpClient()
    val json = Json { ignoreUnknownKeys = true }

    fun init(context: Context) {
        if (database != null) return
        val db = Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping.db",
        ).build()
        database = db
        _cartRepository = CartRepositoryImpl(db.cartDao())

        _productRepository = ProductRepositoryImpl(
            client = client,
            baseUrl = "http://localhost:12345/".toHttpUrl(),
            json = json,
        )
    }
}
