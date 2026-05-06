package woowacourse.shopping.domain

import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CartContentTest {
    @Test
    fun `입력받은 상품의 id가 CartItem 의 상품 id 와 같으면 true를 반환한다`() {

        // given : 상품과 CartItem이 주어진다
        val product = normalProduct("임시", id = "1")

        val cartContent = CartContent(product, 1)

        // when : 다른 상품을 입력받아 비교할 때
        val result = cartContent.hasProductId(
            "1",
        )

        // then : true를 반환한다
        assertEquals(true, result)
    }

    @Test
    fun `입력받은 상품의 id가 CartItem 의 상품 id 와 다르면 false를 반환한다`() {

        // given : 상품과 CartItem이 주어진다
        val product = normalProduct("임시", "1")
        val other = normalProduct("임시2", "2")

        val cartContent = CartContent(product, 1)

        // when : 다른 상품을 입력받아 비교할 때
        val result = cartContent.hasProductId(
            other.id,
        )

        // then : false를 반환한다
        assertEquals(false, result)
    }

    @Test
    fun `상품의 수량이 1개 미만이면 오류가 발생한다`() {
        // given & when & then : 0개인 상품이 주어지면 오류가 발생한다
        assertThrows<IllegalArgumentException> {
            CartContent(normalProduct(), 0)
        }
    }

    @Test
    fun `상품의 수량을 더하면 더해진 새 상품을 반환한다`() {
        // given : 수량이 1개인 상품이 주어진다
        val product = normalProduct()
        val content = CartContent(product, 1)

        // when : 상품에 1을 더할 때
        val newContent = content.addQuantity(product.id, 1)

        // then : Quantity의 수량이 2가 된다
        assertEquals(2, newContent.quantity)
    }

    private fun normalProduct(
        name: String = "상품",
        id: String = UUID.randomUUID().toString(),
    ): Product = Product(
        id = id,
        name = name,
        price = Money(1000),
        imageUrl = "",
    )
}
