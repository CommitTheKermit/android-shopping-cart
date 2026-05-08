package woowacourse.shopping.feature.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.IntentCompat
import kotlin.jvm.java
import woowacourse.shopping.feature.common.state.ProductUiModel
import woowacourse.shopping.feature.productdetail.ui.theme.AndroidshoppingcartTheme

class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uiModel = IntentCompat.getParcelableExtra(intent, DETAIL_PRODUCT, ProductUiModel::class.java)

        setContent {
            AndroidshoppingcartTheme {
                if (uiModel == null) {
                    ProductDetailErrorScreen(onCloseClick = { finish() })
                } else {
                    ProductDetailScreen(
                        id = uiModel.id,
                        onCloseClick = { finish() },
                        onAddToCartClick = {
                            finish()
                        },
                    )
                }
            }
        }
    }

    companion object {
        private const val DETAIL_PRODUCT = "product_id"

        fun newIntent(
            context: Context,
            uiModel: ProductUiModel,
        ): Intent = Intent(context, ProductDetailActivity::class.java)
            .putExtra(DETAIL_PRODUCT, uiModel)
    }
}
