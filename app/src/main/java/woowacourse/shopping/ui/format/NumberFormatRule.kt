package woowacourse.shopping.ui.format

fun interface NumberFormatRule {
    fun apply(value: Int): String
}
