package woowacourse.shopping.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import woowacourse.shopping.constants.MockData
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.CartContent
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductUiModel

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val uiModels: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isEnd: Boolean = false,
//        _products.size >= MockData.MOCK_PRODUCTS.size,
)

class ProductListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())

    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    fun loadingFetch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = fetchProducts(20)
            _uiState.update {
                it.copy(
                    products = result,
                    uiModels = result.map(::toProductUiModel),
                    isLoading = false,
                    isEnd = result.size >= MockData.MOCK_PRODUCTS.size,
                )
            }
        }
    }

    private suspend fun fetchProducts(pageSize: Int): List<Product> {
        delay(2000) // 비동기 상황 가정

        val current = _uiState.value.products
        val toOffset = minOf(current.size + pageSize, MockData.MOCK_PRODUCTS.size)
        return MockData.MOCK_PRODUCTS.subList(0, toOffset)
    }

    private fun toProductUiModel(product: Product): ProductUiModel {
        return ProductUiModel.of(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
        )
    }

    //
    fun addToCart(cartContent: CartContent): Cart {
//        return cart.plusCartContent(cartItem)
        return Cart(
            cartContents = emptyList(),
        )
    }

    //
    fun syncDeletedCartItems(ids: List<String>): Cart {
//        return cart.retainOnly(ids)
        return Cart(
            cartContents = emptyList(),
        )
    }
//
//    fun toProductUiModels(): List<ProductUiModel> {
//        return cart.getProductList().map { toProductUiModel(it) }
//    }
}
