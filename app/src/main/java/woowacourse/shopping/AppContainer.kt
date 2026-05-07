package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.local.ShoppingDatabase

object AppContainer {
    private var database: ShoppingDatabase? = null
    private var _cartRepository: CartRepository? = null

    val cartRepository: CartRepository
        get() = checkNotNull(_cartRepository) { "AppContainer.init(context) 호출이 누락되었습니다." }

    fun init(context: Context) {
        if (database != null) return
        val db = Room.databaseBuilder(
            context.applicationContext,
            ShoppingDatabase::class.java,
            "shopping.db",
        ).build()
        database = db
        _cartRepository = CartRepositoryImpl(db.cartDao())
    }
}
