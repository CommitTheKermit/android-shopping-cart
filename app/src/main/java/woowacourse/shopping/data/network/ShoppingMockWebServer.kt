package woowacourse.shopping.data.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import woowacourse.shopping.constants.MockData
import woowacourse.shopping.data.ProductDto

private fun startMockWebServer() {
    val mockWebServer = MockWebServer()
    mockWebServer.url("/")
    val products = MockData.MOCK_PRODUCTS

    val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/products" -> {
                    MockResponse()
                        .setHeader("Content-Type", "application/json")
                        .setResponseCode(200)
                        .setBody(
                            Json.encodeToString(
                                products.map {
                                    ProductDto(
                                        id = it.id.toInt(),
                                        name = it.name,
                                        price = it.priceAmount(),
                                        imageUrl = it.imageUrl,
                                    )
                                },
                            ),
                        )
                }

//                "/products/1" -> {
//                    MockResponse()
//                        .setHeader("Content-Type", "application/json")
//                        .setResponseCode(200)
//                        .setBody(product)
//                }
//
//                "/cart-items" -> {
//                    MockResponse()
//                        .setHeader("Content-Type", "application/json")
//                        .setResponseCode(200)
//                        .setBody(cartItems)
//                }

                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }

    mockWebServer.dispatcher = dispatcher
}
