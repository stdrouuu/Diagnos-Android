package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ukrida.diagnos.R
import org.ukrida.diagnos.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    onNavigateToListTest: () -> Unit
) {
    val currentUser = userViewModel.currentUser.value
    val userName = currentUser?.name ?: "Guest"

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Welcome Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "SELAMAT PAGI,",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9CA3AF),
                letterSpacing = 1.5.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = userName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3CB7A6)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .offset(y = 4.dp)
                        .background(Color(0xFFF86066), CircleShape)
                )
            }
            Text(
                text = "Siap untuk mengoptimalkan kesehatan Anda hari ini di Diagnōs?",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 6.dp),
                lineHeight = 16.sp
            )
            Text(
                text = buildAnnotatedString {
                    append("Terakhir Test Lab: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))) {
                        append("12/11/26")
                    }
                },
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Search Bar Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari tes lab...", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            )
            Button(
                onClick = { /* Action search if needed */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CB7A6)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(52.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text("Cari", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Quick Actions Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Action 1
            QuickActionButton(
                icon = Icons.Default.Science,
                label = "Lihat Hasil Tes Lab",
                modifier = Modifier.weight(1f)
            )
            // Action 2 (with Gratis badge)
            QuickActionButton(
                icon = Icons.Default.Forum,
                label = "Konsultasi WA",
                modifier = Modifier.weight(1f),
                showGratisBadge = true
            )
            // Action 3
            QuickActionButton(
                icon = Icons.AutoMirrored.Filled.Assignment,
                label = "Riwayat Pemeriksaan",
                modifier = Modifier.weight(1f)
            )
        }

        // Promo Banners Row
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Banner 1
                PromoBannerCard(
                    title = buildAnnotatedString {
                        append("Hadiah Terindah adalah ")
                        withStyle(style = SpanStyle(color = Color(0xFF3CB7A6), fontWeight = FontWeight.ExtraBold)) {
                            append("Tubuh yang Sehat!")
                        }
                    },
                    discountText = "Hemat 12%",
                    discountDesc = "Semua Pemeriksaan Lab",
                    minTransaction = "*Min. Transaksi Rp2Jt"
                )
                // Banner 2
                PromoBannerCard(
                    title = buildAnnotatedString {
                        append("Cek Kesehatan Berkala ")
                        withStyle(style = SpanStyle(color = Color(0xFF3CB7A6), fontWeight = FontWeight.ExtraBold)) {
                            append("Mulai Sekarang!")
                        }
                    },
                    discountText = "Hemat 15%",
                    discountDesc = "Khusus Pengguna Baru",
                    minTransaction = "*Tanpa Min. Transaksi"
                )
            }

            // Dots indicator
            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).background(Color(0xFF3CB7A6), CircleShape))
                Box(modifier = Modifier.size(6.dp).background(Color(0xFFE5E7EB), CircleShape))
            }
        }

        // Popular Test Lab Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "PILIHAN TERBAIK",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF9CA3AF),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Tes Lab Populer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }
                Row(
                    modifier = Modifier.clickable { onNavigateToListTest() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lihat Semua",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "See All",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PopularTestCard(
                    title = "Cek Jantung Dasar",
                    desc = "Total, LDL, HDL & Trigliserida",
                    buttonColor = Color(0xFFF86066)
                )
                PopularTestCard(
                    title = "Cek Gula Darah Lengkap",
                    desc = "Total, LDL, HDL & Trigliserida",
                    buttonColor = Color(0xFFFFA92A)
                )
                PopularTestCard(
                    title = "Cek Kolesterol Total",
                    desc = "Total, LDL, HDL & Trigliserida",
                    buttonColor = Color(0xFF40B5A7)
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    showGratisBadge: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { /* Action quick button */ }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF3CB7A6),
                    modifier = Modifier.size(28.dp)
                )
            }
            if (showGratisBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 8.dp)
                        .background(Color(0xFFF86066), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "GRATIS",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B5563),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun PromoBannerCard(
    title: androidx.compose.ui.text.AnnotatedString,
    discountText: String,
    discountDesc: String,
    minTransaction: String
) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(7f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151),
                    lineHeight = 18.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Hemat ")
                            withStyle(style = SpanStyle(color = Color(0xFFF86066), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)) {
                                append(discountText.substringAfter("Hemat "))
                            }
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = discountDesc,
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = minTransaction,
                        fontSize = 8.sp,
                        color = Color(0xFF9CA3AF),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxHeight()
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Cloud Character",
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun PopularTestCard(
    title: String,
    desc: String,
    buttonColor: Color
) {
    Card(
        modifier = Modifier
            .width(175.dp)
            .height(192.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = desc,
                    fontSize = 10.sp,
                    color = Color(0xFF9CA3AF),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 13.sp
                )
            }
            Button(
                onClick = { /* Pesan action */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Text(
                    text = "Pesan",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
