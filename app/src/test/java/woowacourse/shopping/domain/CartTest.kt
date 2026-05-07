package woowacourse.shopping.domain

import coil3.util.CoilUtils.result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CartTest {
    @Test
    fun `상품을 추가하면 추가된 Cart 를 반환한다`() {
        // given : 상품과 Cart가 주어진다
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 1)
        val cart = Cart(emptyList())

        // when : 상품을 Cart에 추가하면
        val newCart = cart.plusCartContent(cartContent)

        // then : 상품이 추가된 Cart가 반환된다
        val result = newCart.contains(product)
        assertEquals(result, true)
    }

    @Test
    fun `product가 존재한다면 true를 반환한다`() {
        // given : 상품과 Cart가 주어진다
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 1)
        val cart = Cart(listOf(cartContent))

        // when : 상품이 존재하는지 확인할 때
        val result = cart.contains(product)

        // then : true가 반환된다
        assertEquals(result, true)
    }

    @Test
    fun `product가 존재하지 않는다면 false를 반환한다`() {
        // given : 상품과 Cart, 다른 상품이 주어진다
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 1)
        val cart = Cart(listOf(cartContent))

        val otherProduct = normalProduct("임시2")

        // when : 상품이 존재하는지 확인할 때
        val result = cart.contains(otherProduct)

        // then : false가 반환된다
        assertEquals(result, false)
    }

    @Test
    fun `이미 존재하는 상품을 추가하면 수량이 합쳐진 Cart 를 반환한다`() {
        // given : 수량이 하나인 장바구니 내역과 Cart가 주어진다.
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 1)
        val cart = Cart(listOf(cartContent))

        // when : 같은 상품을 장바구니에 추가하면
        val newCart = cart.plusCartContent(cartContent)

        // then : 수량이 2가 된다
        assertEquals(newCart.quantityOf(product.id), 2)
    }

    @Test
    fun `삭제 수량이 보유 수량과 같으면 해당 상품이 제거된 Cart 를 반환한다`() {
        // given : 수량이 하나인 장바구니 내역과 Cart가 주어진다.
        val cartContent = CartContent(normalProduct("임시"), 1)
        val cart = Cart(listOf(cartContent))

        // when : 수량을 하나 뺄 때
        val newCart = cart.minusCartContent(cartContent)

        // then : 장바구니 내역에서 삭제된 Cart가 반환된다
        assertEquals(newCart.cartContentsSizeOf(), 0)
    }

    @Test
    fun `삭제 수량이 보유 수량보다 적으면 수량이 줄어든 Cart 를 반환한다`() {
        // given : 수량이 셋인 장바구니 내역과 Cart가 주어진다.
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 3)
        val cart = Cart(listOf(cartContent))

        // when : 수량을 하나 뺄 때
        val newCart = cart.minusCartContent(CartContent(product, 1))

        // then : 장바구니 내역에서 감소된 Cart가 반환된다
        assertEquals(newCart.quantityOf(product.id), 2)
    }

    @Test
    fun `삭제 수량이 보유 수량보다 많으면 예외를 발생시킨다`() {
        // given : 수량이 셋인 장바구니 내역과 Cart가 주어진다.
        val product = normalProduct("임시")
        val cartContent = CartContent(product, 3)
        val cart = Cart(listOf(cartContent))

        // when & then : 수량을 넷 뺄 때 예외가 발생한다
        assertThrows<IllegalArgumentException> {
            cart.minusCartContent(CartContent(product, 4))
        }
    }

    private fun normalProduct(name: String): Product = Product(
        name = name,
        price = Money(1000),
        imageUrl = "",
    )
}
