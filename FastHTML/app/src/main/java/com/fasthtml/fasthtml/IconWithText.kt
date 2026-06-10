package com.fasthtml.fasthtml

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 1. Tambahkan field 'route' untuk navigasi
data class CategoryItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun KategoriMateri(
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit = {} // Callback untuk menangani klik
) {
    // 2. Daftar data menu lengkap dengan route-nya
    val categories = listOf(
        CategoryItem(title = "Dasar", icon = Icons.Filled.Code, route = "dasar"),
        CategoryItem(title = "Elemen", icon = Icons.Filled.Code, route = "elemen"),
        CategoryItem(title = "Attribut", icon = Icons.Filled.Code, route = "attribut"),
        CategoryItem(title = "Tabel", icon = Icons.Filled.Code, route = "tabel"),
        CategoryItem(title = "Form", icon = Icons.Filled.Code, route = "form")
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 3. Loop untuk menampilkan setiap item
        categories.forEach { category ->
            CategoryButton(
                item = category,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(category.route) } // Teruskan route saat diklik
            )
        }
    }
}

@Composable
fun CategoryButton(
    item: CategoryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Terima callback klik dari parent
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onClick, // Jalankan fungsi navigasi
            shape = RoundedCornerShape(size = 4.dp),
            modifier = Modifier.requiredSize(52.dp),
            contentPadding = PaddingValues(all = 0.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title
            )
        }
        Text(text = item.title)
    }
}

@Preview(showBackground = true)
@Composable
fun KategoriMateriPreview() {
    KategoriMateri()
}
