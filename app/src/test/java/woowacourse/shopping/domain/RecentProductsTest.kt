package woowacourse.shopping.domain

import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecentProducts(private val products: List<Product>) {
    fun addProduct(newProduct: Product): RecentProducts {
        var newProducts = products
        if (newProducts.size >= 10) {
            newProducts = newProducts.take(9)
        }

        val duplicate = newProducts.firstOrNull {
            it.id == newProduct.id
        }
        if (duplicate != null) {
            newProducts = products.filter { it.id != newProduct.id }
        }
        return RecentProducts(listOf(newProduct) + newProducts)
    }

    fun mostRecentProduct(): Product {
        return products.first()
    }

    fun sizeOf(): Int {
        return products.size
    }
}

class RecentProductsTest {
    @Test
    fun `상품을 추가하면 가장 최신 위치에 들어간다`() {
        // given : 목록이 이미 있는 RecentProducts가 주어진다
        val recentProducts = RecentProducts(
            products = listOf(normalProduct(), normalProduct("1"), normalProduct("2")),
        )

        // when : 상품을 추가할 때
        val newProduct = normalProduct("최신")
        val newRecentProducts = recentProducts.addProduct(newProduct)

        // then : 가장 최신 위치에 들어간, RecentProducts가 반환된다
        assertEquals(newRecentProducts.mostRecentProduct().id, newProduct.id)
    }

    @Test
    fun `동일 상품을 다시 추가하면 중복 없이 가장 최신 위치로 이동한다`() {
        // given : 목록이 이미 있는 RecentProducts가 주어진다
        val newProduct = normalProduct("최신")
        val recentProducts = RecentProducts(
            products = listOf(normalProduct("1"), normalProduct("2"), newProduct),
        )

        // when : 동일한 상품을 추가할 때
        val newRecentProducts = recentProducts.addProduct(newProduct)

        // then : 가장 최신 위치에 들어간, RecentProducts가 반환된다
        assertEquals(newRecentProducts.mostRecentProduct().id, newProduct.id)
    }

    @Test
    fun `11번째 상품을 추가하면 가장 오래된 상품이 밀려나 항상 10개 이하로 유지된다`() {
        // given : 목록의 크기가 10개인 RecentProducts가 주어진다
        val recentProducts = RecentProducts(
            products = listOf(
                normalProduct("0"),
                normalProduct("1"),
                normalProduct("2"),
                normalProduct("3"),
                normalProduct("4"),
                normalProduct("5"),
                normalProduct("6"),
                normalProduct("7"),
                normalProduct("8"),
                normalProduct("9"),
            ),
        )

        // when : 상품을 추가할 때
        val newProduct = normalProduct("10")
        val newRecentProducts = recentProducts.addProduct(newProduct)

        // then : 가장 오래된 상품이 제거되고 10개 이하로 유지된다.
        assertEquals(newRecentProducts.sizeOf(), 10)
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
