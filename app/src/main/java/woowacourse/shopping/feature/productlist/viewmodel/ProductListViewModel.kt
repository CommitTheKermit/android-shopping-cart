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
import woowacourse.shopping.AppContainer.cartRepository
import woowacourse.shopping.AppContainer.productRepository
import woowacourse.shopping.AppContainer.recentProductRepository
import woowacourse.shopping.constants.MockData
import woowacourse.shopping.data.repository.cart.CartRepository
import woowacourse.shopping.data.repository.product.ProductRepository
import woowacourse.shopping.data.repository.recentproduct.RecentProductRepository
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductUiModel

data class ProductListUiState(
    val productUiModels: List<ProductUiModel> = emptyList(),
    val recentProducts: List<ProductUiModel> = emptyList(),
    val mostRecentProductId: String? = null,
    val isLoading: Boolean = false,
    val isEnd: Boolean = false,
    val cartTotalQuantity: Int = 0,
)

class ProductListViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val recentProductRepository: RecentProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var products: List<Product> = emptyList()
    private var cart: Cart = Cart(emptyList())
    fun initialLoading() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            refreshCart()
            fetchAndAppendProducts(20)
            refreshRecentProducts()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun loadingFetch() {
        viewModelScope.launch {
            fetchAndAppendProducts(20)
        }
    }

    fun increase(productId: String) {
        val product = products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.increase(product)
            refreshCart()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun decrease(productId: String) {
        val product = products.firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.decrease(productId)
            refreshCart()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun insertRecentProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            recentProductRepository.insert(productId)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun loadRecentProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            refreshRecentProducts()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun cartRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            refreshCart()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun refreshCart() {
        cart = cartRepository.loadCart()

        _uiState.update {
            it.copy(
                cartTotalQuantity = cart.totalQuantityOf(),
                productUiModels = products.map(::toProductUiModel),
            )
        }
    }

    private suspend fun fetchAndAppendProducts(pageSize: Int) {
        val result = productRepository.loadProducts(products.size, pageSize)
        products = products + result

        _uiState.update {
            it.copy(
                productUiModels = products.map(::toProductUiModel),
                isEnd = result.size >= MockData.MOCK_PRODUCTS.size,
            )
        }
    }

    private suspend fun refreshRecentProducts() {
        val recentProductIds = recentProductRepository.loadProducts()
        val productById = products.associateBy { it.id }
        val recents = recentProductIds.mapNotNull { productById[it] }
        if (recents.isEmpty()) return

        _uiState.update {
            it.copy(
                recentProducts = recents.map(::toProductUiModel),
                mostRecentProductId = recents.first().id,
            )
        }
    }

    fun toProductUiModel(product: Product): ProductUiModel {
        return ProductUiModel(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
            quantity = cart.quantityOf(product.id),
        )
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProductListViewModel(
                    cartRepository,
                    productRepository,
                    recentProductRepository,
                )
            }
        }
    }
}
