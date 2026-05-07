package woowacourse.shopping.feature.productlist

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import woowacourse.shopping.feature.cart.CartActivity
import woowacourse.shopping.feature.productdetail.ProductDetailActivity
import woowacourse.shopping.feature.productlist.viewmodel.ProductListViewModel

@Composable
fun ProductListRoute(viewModel: ProductListViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val detailLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val addedId = ProductDetailActivity.getAddedId(result.data)
            ?: return@rememberLauncherForActivityResult
//        TODO viewModel.addToCart(addedId)

        Toast.makeText(context, "장바구니에 추가되었습니다", Toast.LENGTH_SHORT).show()
    }

    val cartLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val deletedIds = result.data
            ?.let { CartActivity.getDeletedList(it) }
            ?.map { it.id }
            ?: return@rememberLauncherForActivityResult
//  TODO      viewModel.syncDeletedCartItems(deletedIds)
    }

    ProductListScreen(
        isLoading = state.isLoading,
        productUiModels = state.uiModels,
        isEnd = state.isEnd,
        onLoading = viewModel::loadingFetch,
        onProductClick = { uiModel ->
            detailLauncher.launch(ProductDetailActivity.newIntent(context, uiModel))
        },
        onCartIconClick = {
            cartLauncher.launch(CartActivity.newIntent(context, state.uiModels))
        },
    )
}
