package woowacourse.shopping.ui.productlist

import android.R.attr.bottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.shopping.R
import woowacourse.shopping.ui.common.ProductQuantitySelector

@Composable
fun ProductItem(
    imageUrl: String,
    name: String,
    price: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box {
            PreviewableAsyncImage(
                imageUrl = imageUrl,
                description = name,
                modifier = Modifier.aspectRatio(1f),
            )
//            ProductInitialAddButton(modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 4.dp, end = 4.dp))
            ProductQuantitySelector(
                modifier = Modifier
                    .height(42.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 6.dp, start = 4.dp, end = 4.dp),
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = name,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = price,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            color = Color(0xff555555),
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview
@Composable
private fun PreviewProduct() {
    ProductItem(
        imageUrl = "asd",
        name = "Pet보틀-정사각형 50000ml",
        price = "12,000원",
        modifier = Modifier
            .width(160.dp)
            .padding(horizontal = 16.dp),
    )
}

@Composable
@Preview
fun ProductInitialAddButton(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_product_description),
            modifier = Modifier.size(40.dp),
            tint = Color(0xff555555),
        )
    }
}
