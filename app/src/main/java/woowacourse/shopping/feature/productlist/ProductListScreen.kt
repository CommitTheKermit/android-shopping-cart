package woowacourse.shopping.feature.productlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import woowacourse.shopping.feature.common.state.ProductUiModel
import woowacourse.shopping.feature.productlist.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    vm: ProductListViewModel = viewModel(factory = ProductListViewModel.Factory),
    onProductClick: (ProductUiModel) -> Unit,
    onCartIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        vm.initialLoading()
    }

    val state by vm.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProductListAppBar(onCartIconClick = onCartIconClick, cartQuantities = 0)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                ProductList(
                    products = state.products.map(vm::toProductUiModel),
                    onProductClick = onProductClick,
                    onLoading = vm::loadingFetch,
                    onIncrease = vm::increase,
                    onDecrease = vm::decrease,
                    isEnd = state.isEnd,
                )
                if (state.isLoading) {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewProductListScreen() {
    ProductListScreen(
        onProductClick = { },
        onCartIconClick = { },
    )
}
