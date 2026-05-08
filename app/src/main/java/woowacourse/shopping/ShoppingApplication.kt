package woowacourse.shopping

import android.app.Application
import kotlin.apply
import woowacourse.shopping.data.network.startMockWebServer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread {
            startMockWebServer()
            AppContainer.init(this)
        }.apply {
            start()
            join()
        }
    }
}
