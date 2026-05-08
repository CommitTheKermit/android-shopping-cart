package woowacourse.shopping.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import woowacourse.shopping.AppContainer
import woowacourse.shopping.constants.MockData
import woowacourse.shopping.data.repository.cart.CartRepository
import woowacourse.shopping.data.repository.product.ProductRepository
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.CartContent
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductUiModel

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = false,
    val isEnd: Boolean = false,
)

class ProductListViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()
    fun initialLoading() {
        cartRefresh()
        loadingFetch()
    }

    fun loadingFetch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = fetchProducts(20)
            _uiState.update {
                it.copy(
                    products = result,
                    isLoading = false,
                    isEnd = result.size >= MockData.MOCK_PRODUCTS.size,
                )
            }
        }
    }

    fun increase(productId: String) {
        val product = _uiState.value.products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            cartRepository.increase(product)
            cartRefresh()
        }
    }

    fun decrease(productId: String) {
        val product = _uiState.value.products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            cartRepository.decrease(productId)
            cartRefresh()
        }
    }

    fun cartRefresh() {
        viewModelScope.launch {
            val cart = cartRepository.loadCart()
            _uiState.update {
                it.copy(
                    cart = cart,
                )
            }
        }
    }

    private suspend fun fetchProducts(pageSize: Int): List<Product> {
        return productRepository.loadProducts(uiState.value.products.size, pageSize)
    }

    fun toProductUiModel(product: Product): ProductUiModel {
        return ProductUiModel.of(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
            quantity = uiState.value.cart.quantityOf(product.id),
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
    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProductListViewModel(AppContainer.cartRepository, AppContainer.productRepository)
            }
        }
    }
}
