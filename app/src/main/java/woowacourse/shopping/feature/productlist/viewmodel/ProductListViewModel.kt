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
import woowacourse.shopping.data.repository.recentproduct.RecentProductRepository
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductUiModel

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = false,
    val isEnd: Boolean = false,
    val recentProducts: List<ProductUiModel> = emptyList(),
)

class ProductListViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val recentProductRepository: RecentProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()
    fun initialLoading() {
        viewModelScope.launch {
            refreshCart()
            fetchAndAppendProducts(20)
            refreshRecentProducts()
        }
    }

    fun loadingFetch() {
        viewModelScope.launch { fetchAndAppendProducts(20) }
    }

    fun increase(productId: String) {
        val product = _uiState.value.products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            cartRepository.increase(product)
            refreshCart()
        }
    }

    fun decrease(productId: String) {
        val product = _uiState.value.products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            cartRepository.decrease(productId)
            refreshCart()
        }
    }

    fun cartRefresh() {
        viewModelScope.launch { refreshCart() }
    }

    fun insertRecentProduct(productId: String) {
        viewModelScope.launch {
            recentProductRepository.insert(productId)
        }
    }

    fun loadRecentProducts() {
        viewModelScope.launch { refreshRecentProducts() }
    }

    private suspend fun refreshCart() {
        val cart = cartRepository.loadCart()
        _uiState.update { it.copy(cart = cart) }
    }

    private suspend fun fetchAndAppendProducts(pageSize: Int) {
        _uiState.update { it.copy(isLoading = true) }
        val result = productRepository.loadProducts(uiState.value.products.size, pageSize)
        _uiState.update {
            it.copy(
                products = it.products + result,
                isLoading = false,
                isEnd = result.size >= MockData.MOCK_PRODUCTS.size,
            )
        }
    }

    private suspend fun refreshRecentProducts() {
        val recentProductIds = recentProductRepository.loadProducts()
        val recents = uiState.value.products.filter { it.id in recentProductIds }
        _uiState.update {
            it.copy(recentProducts = recents.map(::toProductUiModel))
        }
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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProductListViewModel(
                    AppContainer.cartRepository,
                    AppContainer.productRepository,
                    AppContainer.recentProductRepository,
                )
            }
        }
    }
}
