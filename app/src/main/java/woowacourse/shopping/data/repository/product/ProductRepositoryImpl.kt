package woowacourse.shopping.data.repository.product

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import woowacourse.shopping.data.network.ProductDto
import woowacourse.shopping.domain.Product

class ProductRepositoryImpl(
    private val client: OkHttpClient,
    private val baseUrl: HttpUrl,
    private val json: Json,
) : ProductRepository {
    override suspend fun loadProducts(
        startIndex: Int,
        pageSize: Int,
    ): List<Product> = withContext(Dispatchers.IO) {
        val url = baseUrl.newBuilder()
            .addPathSegment("products")
            .addQueryParameter("startIndex", startIndex.toString())
            .addQueryParameter("pageSize", pageSize.toString())
            .build()
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "products 요청 실패: ${response.code}" }

            val body = response.body?.string().orEmpty()
            json.decodeFromString<List<ProductDto>>(body)
                .take(pageSize)
                .map(ProductDto::toDomain)
        }
    }
}
