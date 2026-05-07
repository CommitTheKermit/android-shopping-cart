package woowacourse.shopping.feature.format

fun interface NumberFormatRule {
    fun apply(value: Int): String
}
