package woowacourse.shopping.feature.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import woowacourse.shopping.R
import woowacourse.shopping.domain.Product
import woowacourse.shopping.feature.common.ProductQuantitySelector
import woowacourse.shopping.feature.productdetail.ProductAppBar
import woowacourse.shopping.feature.productdetail.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    id: String,
    onCloseClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initialLoading(
            productId = id,
        )
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val product = uiState.product

    when {
        uiState.isLoading -> LoadingIndicator()
        product == null -> ProductDetailErrorScreen(onCloseClick)
        else -> ProductDetailContent(
            product = product,
            onCloseClick = onCloseClick,
            onAddToCartClick = onAddToCartClick,
            modifier = modifier,
        )
    }
}

@Composable
fun ProductDetailContent(
    product: Product,
    onCloseClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProductAppBar(
                onCloseClick = onCloseClick,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column {
                PreviewableAsyncImage(
                    imageUrl = product.imageUrl,
                    description = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
                Text(
                    text = product.name,
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
                )
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 18.dp),
                ) {
                    Text(
                        text = product.priceAmount().toString(),
                        fontWeight = FontWeight.W400,
                        fontSize = 20.sp,
                    )
                    ProductQuantitySelector(
                        quantity = 0,
                        modifier = Modifier.size(width = 126.dp, height = 42.dp),
                    )
                }
            }
            CartPutButton(
                onClick = onAddToCartClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun CartPutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xff04c09e))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        Text(
            stringResource(R.string.product_detail_select),
            fontWeight = FontWeight.W700,
            fontSize = 24.sp,
            color = Color.White,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailErrorScreen(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProductAppBar(
                onCloseClick = onCloseClick,
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.product_detail_error_description),
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(64.dp),
            )
            Text(
                text = stringResource(R.string.product_detail_entry_error),
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ProductScreenPreview() {
    ProductDetailScreen(
        onCloseClick = {},
        onAddToCartClick = {},
        id = "1",
    )
}

@Preview
@Composable
private fun ProductErrorScreenPreview() {
    ProductDetailErrorScreen(onCloseClick = {})
}
