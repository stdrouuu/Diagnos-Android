// View: Layar Profil pengguna untuk melihat informasi akun dan riwayat aktivitas
package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.ukrida.diagnos.R
import org.ukrida.diagnos.viewmodel.UserViewModel
import org.ukrida.diagnos.viewmodel.BookingViewModel
import org.ukrida.diagnos.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    navController: NavHostController,
    bookingViewModel: BookingViewModel = remember { BookingViewModel() },
    historyViewModel: HistoryViewModel = remember { HistoryViewModel() },
    onNavigateToHistory: () -> Unit = {},
    onLogout: () -> Unit
) {
    val currentUser = viewModel.currentUser.value
    val userName = currentUser?.name ?: "Estero"
    val userPhoto: Any = currentUser?.photo ?: R.drawable.images

    var showImagePreview by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        currentUser?.id?.let { userId ->
            if (userId > 0) {
                historyViewModel.getHistoryList(userId)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Sticky Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profil",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Edit icon on top-right
                        IconButton(
                            onClick = { navController.navigate("profileedit") },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profil",
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar Image (Clickable for preview)
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                                    .shadow(2.dp, CircleShape)
                                    .clickable { showImagePreview = true }
                            ) {
                                AsyncImage(
                                    model = userPhoto,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Name
                            Text(
                                text = userName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1F2937)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Member Badge
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFE6F7F5), RoundedCornerShape(100.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF3CB7A6),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "DIAGNOS MEMBER",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF3CB7A6),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Aktivitas Section
                SectionTitle(text = "Aktivitas")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    Column {
                        // Status Pesanan
                        // Status Pesanan — driven by pendingOrders (not historyList)
                        val pendingOrders = historyViewModel.pendingOrders.value
                        ProfileMenuItem(
                            icon = Icons.Default.Inventory,
                            title = "Status Pesanan",
                            badgeText = if (pendingOrders.isNotEmpty()) "Menunggu" else "Tidak Ada",
                            badgeColor = if (pendingOrders.isNotEmpty()) Color(0xFFFEF3C7) else Color(0xFFF3F4F6),
                            badgeTextColor = if (pendingOrders.isNotEmpty()) Color(0xFFD97706) else Color(0xFF6B7280),
                            onClick = { if (pendingOrders.isNotEmpty()) showHistoryDialog = true }
                        )
                        HorizontalDivider(color = Color(0xFFF9FAFB), thickness = 1.dp)
                        // Riwayat Pesanan — only shows completed orders via history page
                        ProfileMenuItem(
                            icon = Icons.Default.ReceiptLong,
                            title = "Riwayat Pemeriksaan",
                            onClick = { onNavigateToHistory() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Informasi Section
                SectionTitle(text = "Informasi")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        title = "Edit Profil",
                        onClick = {
                            navController.navigate("profileedit")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Bantuan Section
                SectionTitle(text = "Bantuan")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6))
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.HelpOutline,
                        title = "Pusat Bantuan",
                        onClick = {
                            showHelpDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Keluar Akun Button
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF86066),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color(0xFFF86066))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "KELUAR AKUN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }

    // ================= bigger preview dialog =================
    if (showImagePreview) {
        Dialog(onDismissRequest = { showImagePreview = false }) {
            Surface(
                modifier = Modifier
                    .size(320.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = userPhoto,
                        contentDescription = "Avatar Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { showImagePreview = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
    // ================= status pesanan dialog (Menunggu) =================
    if (showHistoryDialog) {
        val pendingOrders = historyViewModel.pendingOrders.value
        if (pendingOrders.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { showHistoryDialog = false },
                title = {
                    Text(
                        text = "Status Pesanan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1F2937)
                    )
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(pendingOrders) { order ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                    border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "Tes Lab:",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                order.title,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "Klinik:",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                order.clinicName,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "Jadwal:",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                order.date,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                "Status:",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                            val badgeColor = when (order.status) {
                                                "Menunggu" -> Color(0xFFFEF3C7)
                                                "Dikonfirmasi" -> Color(0xFFE0F2FE)
                                                "Sedang diuji" -> Color(0xFFF3E8FF)
                                                "Dibatalkan" -> Color(0xFFFEE2E2)
                                                else -> Color(0xFFF3F4F6)
                                            }
                                            val badgeTextColor = when (order.status) {
                                                "Menunggu" -> Color(0xFFD97706)
                                                "Dikonfirmasi" -> Color(0xFF0369A1)
                                                "Sedang diuji" -> Color(0xFF6B21A8)
                                                "Dibatalkan" -> Color(0xFFEF4444)
                                                else -> Color(0xFF374151)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .background(badgeColor, RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = order.status,
                                                    color = badgeTextColor,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                        
                                        val statusDesc = when (order.status) {
                                            "Menunggu" -> "Pesanan Anda sedang menunggu konfirmasi dari klinik."
                                            "Dikonfirmasi" -> "Pesanan telah dikonfirmasi. Silakan datang ke klinik sesuai jadwal."
                                            "Sedang diuji" -> "Pemeriksaan sedang berlangsung di laboratorium."
                                            "Dibatalkan" -> "Pemeriksaan ini dibatalkan oleh klinik."
                                            else -> "Pemeriksaan selesai. Hasil dapat diakses di Riwayat."
                                        }
                                        
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Text(
                                            text = statusDesc,
                                            fontSize = 11.sp,
                                            color = Color(0xFF9CA3AF),
                                            lineHeight = 15.sp
                                        )

                                        if (order.status == "Dibatalkan" && !order.cancelReason.isNullOrEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFFFEE2E2), RoundedCornerShape(8.dp))
                                                    .border(BorderStroke(0.5.dp, Color(0xFFFCA5A5)), RoundedCornerShape(8.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Alasan Pembatalan:",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFB91C1C)
                                                    )
                                                    Text(
                                                        text = order.cancelReason,
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF991B1B),
                                                        lineHeight = 14.sp
                                                    )
                                                }
                                            }
                                        }
                                        
                                        val hasReferral = !order.referralPhoto.isNullOrEmpty() && order.referralPhoto != "null"
                                        if (hasReferral) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFFFFEAEA), RoundedCornerShape(8.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Text(
                                                    text = "Pengingat: Silakan bawa surat rujukan dokter fisik Anda saat mengunjungi klinik.",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFFA6A71),
                                                    lineHeight = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showHistoryDialog = false }) {
                        Text(text = "Tutup", color = Color(0xFF3CB7A6), fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
    // ================= help dialog =================
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Text(
                    text = "Pusat Bantuan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1F2937)
                )
            },
            text = {
                Text(
                    text = "Layanan Pusat Bantuan sedang dalam pemeliharaan. Silakan hubungi kami kembali nanti.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text(text = "Oke", color = Color(0xFF3CB7A6), fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF1F2937),
        letterSpacing = 1.5.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, bottom = 12.dp)
    )
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    badgeText: String? = null,
    badgeColor: Color = Color.Transparent,
    badgeTextColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFE6F7F5), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF3CB7A6),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151),
            modifier = Modifier.weight(1f)
        )

        // Badge if present
        if (badgeText != null) {
            Box(
                modifier = Modifier
                    .background(badgeColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeTextColor,
                    letterSpacing = 0.5.sp
                )
            }
        } else {
            // Chevron arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
