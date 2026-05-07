package woowacourse.shopping.feature.cart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.feature.cart.stateholder.CartStateHolder
import woowacourse.shopping.feature.cart.ui.theme.AndroidshoppingcartTheme

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val stateHolder = CartStateHolder(emptyList())
            AndroidshoppingcartTheme {
                CartScreen(
                    cartContents = stateHolder.cartContents,
                    onCloseClick = { finish() },
                    onDelete = { id ->
                        stateHolder.deleteCartItem(id)
                    },
                    canMoveToPreviousPage = stateHolder.isStartPage().not(),
                    onLeftClick = {
                        stateHolder.moveToPreviousPage()
                    },
                    canMoveToNextPage = stateHolder.isEndPage().not(),
                    onRightClick = {
                        stateHolder.moveToNextPage()
                    },
                    page = stateHolder.page,
                )
            }
        }
    }
}
