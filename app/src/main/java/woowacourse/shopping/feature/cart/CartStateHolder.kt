package woowacourse.shopping.feature.cart

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.core.os.BundleCompat
import woowacourse.shopping.feature.common.state.ProductUiModel

class CartStateHolder(
    initialItems: List<ProductUiModel>,
    initialPage: Int = 1,
    private val initialPageSize: Int = 5,
) {
    var totalCartContents: List<ProductUiModel> = initialItems.toList()
    var page by mutableIntStateOf(initialPage)
    var cartContents by mutableStateOf(
        pagination(
            page = initialPage,
            productUiModels = totalCartContents,
            pageSize = initialPageSize,
        ),
    )

    fun isStartPage(): Boolean {
        return page == 1
    }

    fun isEndPage(): Boolean = page >= lastPage(initialPageSize)

    private fun lastPage(pageSize: Int): Int {
        if (totalCartContents.isEmpty()) return 1
        return (totalCartContents.size + pageSize - 1) / pageSize
    }

    fun moveToPreviousPage() {
        page -= 1
        cartContents = pagination(page = page, productUiModels = totalCartContents, pageSize = initialPageSize)
    }

    fun moveToNextPage() {
        page += 1
        cartContents = pagination(page = page, productUiModels = totalCartContents, pageSize = initialPageSize)
    }

    private fun pagination(
        page: Int,
        productUiModels: List<ProductUiModel>,
        pageSize: Int,
    ): List<ProductUiModel> {
        val toIndex = minOf(page * pageSize, productUiModels.size)
        return productUiModels.subList((page - 1) * pageSize, toIndex)
    }

    fun deleteCartItem(id: String) {
        totalCartContents = totalCartContents.filter { it.id != id }
        cartContents = pagination(
            page = page,
            productUiModels = totalCartContents,
            pageSize = 5,
        )
    }

    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_ITEMS = "items"

        private const val PAGE_SIZE = "page_size"

        val Saver: Saver<CartStateHolder, Bundle> = Saver(
            save = { holder ->
                Bundle().apply {
                    putInt(KEY_PAGE, holder.page)
                    putParcelableArrayList(KEY_ITEMS, ArrayList(holder.totalCartContents))
                }
            },
            restore = { bundle ->
                val items: List<ProductUiModel> = BundleCompat.getParcelableArrayList(
                    bundle,
                    KEY_ITEMS,
                    ProductUiModel::class.java,
                )
                    ?: emptyList()

                val page = bundle.getInt(KEY_PAGE, 1)
                val pageSize = bundle.getInt(PAGE_SIZE, 5)
                CartStateHolder(items, page, pageSize)
            },
        )
    }
}
