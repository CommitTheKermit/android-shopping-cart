package woowacourse.shopping.feature.common.state

data class ProductDetailUiModel(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val id: String,
    val quantity: Int,
)
