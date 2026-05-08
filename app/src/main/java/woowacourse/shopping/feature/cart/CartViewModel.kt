package woowacourse.shopping.feature.cart

import android.R.attr.name
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import woowacourse.shopping.domain.CartContent
import woowacourse.shopping.feature.common.state.ProductUiModel

data class CartUiState(
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val cartContents: List<ProductUiModel> = emptyList(),
    val cartSize: Int = 0,
)

class CartViewModel(
    private val initialPageSize: Int = 5,
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun initialLoading() {
        getCartSize()
        pagination(
            page = 1,
        )
    }

    fun getCartSize() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cartSize = cartRepository.loadCartSize()
            _uiState.update { it.copy(isLoading = false, cartSize = cartSize) }
        }
    }

    fun isStartPage(): Boolean {
        return uiState.value.page == 1
    }

    fun isEndPage(): Boolean = uiState.value.page >= lastPage(initialPageSize)

    private fun lastPage(pageSize: Int): Int {
        if (uiState.value.cart.getProductList().isEmpty()) return 1
        return (uiState.value.cart.getProductList().size + pageSize - 1) / pageSize
    }

    fun moveToPreviousPage() {
        val page = uiState.value.page - 1
        _uiState.update {
            it.copy(
                page = page,
            )
        }
        pagination(page)
    }

    fun moveToNextPage() {
        val page = uiState.value.page + 1
        _uiState.update {
            it.copy(
                page = page,
            )
        }
        pagination(page)
    }

    private fun pagination(
        page: Int,
        pageSize: Int = 20,
    ) {
        val toIndex = minOf(page * pageSize, uiState.value.cartSize)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.remove(id)
            val cartContents = pagination(uiState.value.page)
            _uiState.update { it.copy(isLoading = false, cartContents = cartContents) }
        }
    }

    fun toProductUiModel(cartContent: CartContent): ProductUiModel {
        val product = cartContent.product
        return ProductUiModel.of(
            name = product.name,
            price = product.priceAmount(),
            imageUrl = product.imageUrl,
            id = product.id,
            quantity = cartContent.quantity,
        )
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CartViewModel(5, AppContainer.cartRepository, AppContainer.productRepository)
            }
        }
    }
}
