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
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.state.ProductDetailUiModel

data class ProductDetailUiState(
    val productDetailUiModel: ProductDetailUiModel? = null,
    val recentProductDetailUiModel: ProductDetailUiModel? = null,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
)

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private lateinit var product: Product

    fun initialLoading(
        productId: String,
        recentProductId: String? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val product = loadingProduct(productId)
            if (recentProductId != null) {
                val recentProductDetailUiModel = loadingProduct(recentProductId)
                _uiState.update {
                    it.copy(
                        recentProductDetailUiModel = toProductDetailUiModel(recentProductDetailUiModel),
                        isLoading = false,
                    )
                }
            }
            _uiState.update {
                it.copy(
                    productDetailUiModel = toProductDetailUiModel(product),
                    isLoading = false,
                )
            }
        }
    }

    suspend fun loadingProduct(productId: String): Product {
        return productRepository.getProduct(productId)
    }

    fun toProductDetailUiModel(product: Product): ProductDetailUiModel {
        return ProductDetailUiModel(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
            quantity = 0,
        )
    }

    fun increase() {
        _uiState.update {
            it.copy(quantity = uiState.value.quantity + 1)
        }
    }

    fun decrease() {
        _uiState.update {
            it.copy(quantity = uiState.value.quantity - 1)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.setProductQuantity(product, uiState.value.quantity)
            _uiState.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProductDetailViewModel(AppContainer.productRepository, AppContainer.cartRepository)
            }
        }
    }
}
