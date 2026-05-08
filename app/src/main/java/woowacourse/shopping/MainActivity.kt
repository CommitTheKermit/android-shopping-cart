package woowacourse.shopping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.AppContainer
import woowacourse.shopping.feature.cart.CartActivity
import woowacourse.shopping.feature.productdetail.ProductDetailActivity
import woowacourse.shopping.feature.productlist.ProductListScreen
import woowacourse.shopping.feature.productlist.ui.theme.AndroidshoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContainer.init(applicationContext)

        val toCartIntent = Intent(this, CartActivity::class.java)

        setContent {
            AndroidshoppingTheme {
                ProductListScreen(
                    onProductClick = {
                        val toProductDetailIntent = ProductDetailActivity.newIntent(
                            context = this,
                            uiModel = it,
                        )
                        startActivity(toProductDetailIntent)
                    },
                    onCartIconClick = {
                        startActivity(toCartIntent)
                    },
                )
            }
        }
    }
}
