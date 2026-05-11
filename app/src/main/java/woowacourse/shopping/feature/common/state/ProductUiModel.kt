package woowacourse.shopping.feature.common.state

import android.os.Parcelable
import java.text.DecimalFormat
import kotlinx.parcelize.Parcelize
import woowacourse.shopping.feature.format.NumberFormatRule
import woowacourse.shopping.feature.format.PriceFormatter

@Parcelize
data class ProductUiModel(
    val name: String,
    val price: Int,
    val imageUrl: String,
    val id: String,
    val quantity: Int,
) : Parcelable {
    fun formattedPrice(quantity: Int = 1): String = priceFormatter.format(price * quantity)

    companion object {
        private val priceFormatter = PriceFormatter(
            rule = NumberFormatRule { DecimalFormat("#,###").format(it) },
            suffix = "원",
        )
    }
}
