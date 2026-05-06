package woowacourse.shopping.ui.format

class PriceFormatter(private val rule: NumberFormatRule, private val suffix: String) {
    fun format(price: Int): String = "${rule.apply(price)}$suffix"
}
