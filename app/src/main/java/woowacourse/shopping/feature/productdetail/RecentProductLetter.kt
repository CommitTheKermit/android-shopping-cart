package woowacourse.shopping.feature.productdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.shopping.feature.common.state.ProductDetailUiModel

@Composable
fun RecentProductLetter(
    productDetailUiModel: ProductDetailUiModel,
    onClickRecentProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xffaaaaaa), shape = RoundedCornerShape(8.dp))
            .clickable(onClick = { onClickRecentProduct(productDetailUiModel.id) }),
    ) {
        Column(Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Text(
                "마지막으로 본 상품",
                fontWeight = FontWeight.W700,
                fontSize = 12.sp,
                color = Color(0xff04c09e),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                productDetailUiModel.name,
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
                color = Color(0xff555555),
            )
        }
    }
}

@Preview
@Composable
fun RecentProductLetterPreview(modifier: Modifier = Modifier) {
    RecentProductLetter(
        productDetailUiModel = ProductDetailUiModel(
            name = "asdqwe",
            price = 1000,
            imageUrl = "",
            id = "",
            quantity = 0,
        ),
        onClickRecentProduct = {},
    )
}
