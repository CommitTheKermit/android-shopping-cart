package woowacourse.shopping.feature.productdetail.viewmodel

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
import woowacourse.shopping.data.repository.cart.CartRepository
import woowacourse.shopping.data.repository.product.ProductRepository
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductUiModel

data class ProductDetailUiState(
    val productUiModel: ProductUiModel? = null,
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = false,
)

class ProductDetailViewModel(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private lateinit var product: Product

    fun initialLoading(productId: String) {
        cartRefresh()
        loadingProduct(productId)
    }

    fun loadingProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            product = productRepository.getProduct(productId)
            _uiState.update {
                it.copy(
                    productUiModel = toProductUiModel(product),
                    isLoading = false,
                )
            }
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

    fun toProductUiModel(product: Product): ProductUiModel {
        return ProductUiModel.of(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
            quantity = uiState.value.cart.quantityOf(product.id),
        )
    }

    fun increase() {
        viewModelScope.launch {
            cartRepository.increase(
                product,
            )
            cartRefresh()
            _uiState.update {
                it.copy(
                    productUiModel = toProductUiModel(product),
                )
            }
        }
    }

    fun decrease() {
        viewModelScope.launch {
            cartRepository.decrease(product.id)
            cartRefresh()
            _uiState.update {
                it.copy(
                    productUiModel = toProductUiModel(product),
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProductDetailViewModel(AppContainer.cartRepository, AppContainer.productRepository)
            }
        }
    }
}
