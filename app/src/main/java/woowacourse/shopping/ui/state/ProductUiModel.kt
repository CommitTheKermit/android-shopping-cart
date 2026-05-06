package woowacourse.shopping.ui.state

import android.os.Parcelable
import java.text.DecimalFormat
import kotlinx.parcelize.Parcelize
import woowacourse.shopping.ui.format.NumberFormatRule
import woowacourse.shopping.ui.format.PriceFormatter

@Parcelize
data class ProductUiModel(val title: String, val price: String, val imageUrl: String, val id: String) : Parcelable {
    companion object {
        fun of(
            name: String,
            price: Int,
            imageUrl: String,
            id: String,
        ): ProductUiModel {

            val priceFormatter = PriceFormatter(
                rule = NumberFormatRule { DecimalFormat("#,###").format(it) },
                suffix = "원",
            )

            return ProductUiModel(
                title = name,
                price = priceFormatter.format(price),
                imageUrl = imageUrl,
                id = id,
            )
        }
    }
}
