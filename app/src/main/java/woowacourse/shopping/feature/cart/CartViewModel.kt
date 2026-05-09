package woowacourse.shopping.feature.cart

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
import woowacourse.shopping.AppContainer.cartRepository
import woowacourse.shopping.data.repository.cart.CartRepository
import woowacourse.shopping.domain.Cart
import woowacourse.shopping.domain.CartContent
import woowacourse.shopping.feature.common.state.ProductDetailUiModel

data class CartUiState(
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = true,
    val page: Int = 1,
    val paginatedCartContents: List<ProductDetailUiModel> = emptyList(),
    val cartSize: Int = 0,
)

class CartViewModel(
    private val initialPageSize: Int = 5,
    private val cartRepository: CartRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun initialLoading() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cartSize = getCartSize()
            _uiState.update { it.copy(cartSize = cartSize) }
            val cartContents = pagination(
                page = 1,
            )
            val cart = getCart()
            _uiState.update { it.copy(isLoading = false, paginatedCartContents = cartContents, cart = cart) }
        }
    }

    private suspend fun getCart(): Cart {
        val cart = cartRepository.loadCart()
        return cart
    }

    private suspend fun getCartSize(): Int {
        val cartSize = cartRepository.loadCartSize()
        return cartSize
    }

    fun isStartPage(): Boolean = uiState.value.page == 1

    fun isEndPage(): Boolean = uiState.value.page >= lastPage(initialPageSize)

    private fun lastPage(pageSize: Int): Int {
        if (uiState.value.cartSize == 0) return 1
        return (uiState.value.cartSize) / pageSize
    }

    fun moveToPreviousPage() {
        val page = uiState.value.page - 1
        _uiState.update {
            it.copy(
                page = page,
            )
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cartContents = pagination(page)
            _uiState.update { it.copy(isLoading = false, paginatedCartContents = cartContents) }
        }
    }

    fun moveToNextPage() {
        val page = uiState.value.page + 1
        _uiState.update {
            it.copy(
                page = page,
            )
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cartContents = pagination(page)
            _uiState.update { it.copy(isLoading = false, paginatedCartContents = cartContents) }
        }
    }

    private suspend fun pagination(
        page: Int,
        pageSize: Int = 5,
    ): List<ProductDetailUiModel> {
        val toIndex = minOf(page * pageSize, uiState.value.cartSize)
        val startIndex = (page - 1) * pageSize

        val cartContents = cartRepository
            .pagination(startIndex, toIndex)
            .map(::toProductUiModel)
        return cartContents
    }

    fun increase(productId: String) {
        val product = _uiState.value.cart.getProductList().firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.increase(product)
            val cart = getCart()
            val cartContents = pagination(uiState.value.page)
            _uiState.update { it.copy(isLoading = false, cart = cart, paginatedCartContents = cartContents) }
        }
    }

    fun decrease(productId: String) {
        val product = _uiState.value.cart.getProductList().firstOrNull { it.id == productId }
        require(product != null) { "존재하지 않는 상품입니다." }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.decrease(productId)
            val cart = getCart()
            val cartContents = pagination(uiState.value.page)
            _uiState.update { it.copy(isLoading = false, cart = cart, paginatedCartContents = cartContents) }
        }
    }

    fun deleteCartItem(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepository.remove(id)
            val cartContents = pagination(uiState.value.page)
            _uiState.update { it.copy(isLoading = false, paginatedCartContents = cartContents) }
        }
    }

    fun toProductUiModel(cartContent: CartContent): ProductDetailUiModel {
        val product = cartContent.product
        return ProductDetailUiModel(
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
                CartViewModel(5, AppContainer.cartRepository)
            }
        }
    }
}
