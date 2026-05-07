package woowacourse.shopping.domain
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
