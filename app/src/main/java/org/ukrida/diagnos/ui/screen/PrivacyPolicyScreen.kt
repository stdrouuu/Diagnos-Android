// View: Layar Kebijakan Privasi (Privacy Policy) Diagnōs
package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Sticky Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color(0xFF1F2937)
                    )
                }

                Text(
                    text = "Kebijakan Privasi",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Banner Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFE6F7F5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF3CAEA3),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Kebijakan Privasi Diagnōs",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Terakhir diperbarui: 30 Agustus 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF3CAEA3)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Privasi dan perlindungan data pribadi Anda adalah prioritas utama kami dalam memberikan layanan kesehatan digital yang aman dan terpercaya.",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 1: Pendahuluan
                PrivacyPolicySection(
                    icon = Icons.Default.VerifiedUser,
                    title = "1. Pendahuluan",
                    content = "Selamat datang di aplikasi Diagnōs. Kebijakan Privasi ini menjelaskan bagaimana kami mengumpulkan, menggunakan, menyimpan, dan melindungi informasi pribadi pengguna saat mendaftar, menggunakan layanan pemesanan tes laboratorium, serta mengunggah surat rujukan medis."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Data yang Diumpulkan
                PrivacyPolicySection(
                    icon = Icons.Default.Person,
                    title = "2. Informasi yang Kami Kumpulkan",
                    content = "Saat Anda membuat akun dan menggunakan aplikasi Diagnōs, kami mengumpulkan beberapa data pribadi yang Anda berikan secara sadar, antara lain:",
                    bulletPoints = listOf(
                        "Identitas Diri: Nama lengkap, Username, Jenis Kelamin, dan Tanggal Lahir.",
                        "Kontak & Komunikasi: Alamat Email dan Nomor Telepon (+62).",
                        "Domisili: Alamat lengkap tempat tinggal untuk keperluan administrasi klinik.",
                        "Data Kesehatan & Medis: Foto surat rujukan dokter (jika ada), riwayat pemesanan tes lab, dan hasil pemeriksaan laboratorium.",
                        "Kredensial Akun: Kata sandi (password) yang tersimpan secara terenkripsi."
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Tujuan Penggunaan Data
                PrivacyPolicySection(
                    icon = Icons.Default.CheckCircle,
                    title = "3. Tujuan Penggunaan Informasi",
                    content = "Data pribadi yang kami kumpulkan digunakan secara khusus untuk keperluan operasional dan pelayanan kesehatan Anda:",
                    bulletPoints = listOf(
                        "Memproses pendaftaran dan verifikasi otentikasi akun pengguna.",
                        "Mengatur dan mengonfirmasi jadwal janji temu pemeriksaan laboratorium di klinik mitra.",
                        "Menampilkan riwayat serta sertifikat hasil pemeriksaan medis secara pribadi dan aman.",
                        "Menghubungi Anda terkait pengingat jadwal, pembatalan, atau pembaruan status pesanan."
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Keamanan & Penyimpanan
                PrivacyPolicySection(
                    icon = Icons.Default.Lock,
                    title = "4. Penyimpanan & Keamanan Data",
                    content = "Kami menerapkan standar enkripsi dan sistem keamanan basis data untuk melindungi informasi pribadi Anda dari akses tidak sah, kebocoran, atau pengubahan. Seluruh rekam medis dan data diri disimpan dalam server aman yang mematuhi regulasi privasi data kesehatan."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 5: Pembagian Pihak Ketiga
                PrivacyPolicySection(
                    icon = Icons.Default.Security,
                    title = "5. Pembagian Data ke Pihak Ketiga",
                    content = "Diagnōs TIDAK AKAN PERNAH menjual, menyewakan, atau membagikan data pribadi Anda kepada pihak ketiga untuk kepentingan komersial atau iklan. Data Anda hanya dibagikan secara terbatas kepada klinik mitra resmi tempat Anda mendaftar pemeriksaan laboratorium."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 6: Hak Pengguna & Hapus Akun
                PrivacyPolicySection(
                    icon = Icons.Default.Shield,
                    title = "6. Hak Pengguna & Penghapusan Akun",
                    content = "Sebagai pemilik data pribadi, Anda memiliki hak penuh untuk mengendalikan data Anda:",
                    bulletPoints = listOf(
                        "Pembaruan Data: Anda dapat memperbarui informasi nama, email, telepon, dan alamat kapan saja melalui menu Edit Profil.",
                        "Penghapusan Akun & Data (Right to be Forgotten): Anda berhak menghapus akun beserta seluruh riwayat pesanan dan pemeriksaan Anda secara permanen dari sistem kami secara langsung melalui tombol 'HAPUS AKUN' pada halaman Profil."
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 7: Persetujuan Pengguna
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F5)),
                    border = BorderStroke(1.dp, Color(0xFFBBECE6))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "7. Persetujuan Kebijakan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dengan mencentang kotak persetujuan pada halaman pendaftaran akun, Anda menyatakan telah membaca, memahami, dan menyetujui seluruh ketentuan dalam Kebijakan Privasi Diagnōs ini.",
                            fontSize = 12.sp,
                            color = Color(0xFF374151),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Close Button
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3CAEA3),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Saya Mengerti",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicySection(
    icon: ImageVector,
    title: String,
    content: String,
    bulletPoints: List<String> = emptyList()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFE6F7F5), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF3CAEA3),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                fontSize = 12.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )

            if (bulletPoints.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bulletPoints.forEach { point ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF3CAEA3), CircleShape)
                            )
                            Text(
                                text = point,
                                fontSize = 12.sp,
                                color = Color(0xFF374151),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
