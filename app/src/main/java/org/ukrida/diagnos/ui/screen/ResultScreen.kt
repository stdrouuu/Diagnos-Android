package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ukrida.diagnos.viewmodel.ResultViewModel
import org.ukrida.diagnos.viewmodel.BookingViewModel
import org.ukrida.diagnos.data.model.TestParameterResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    testId: Int,
    resultViewModel: ResultViewModel,
    bookingViewModel: BookingViewModel,
    onBack: () -> Unit
) {
    val test = bookingViewModel.allTests.find { it.id == testId } ?: bookingViewModel.allTests[0]

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pemeriksaan Terakhir",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFAFAFA)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Title Header
            Text(
                text = "Hasil Pemeriksaan Lab",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // Results Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Test Title Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = test.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF3CB7A6)
                        )
                    }

                    // Table with Horizontal Scroll
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.width(660.dp)) {
                            // Table Header Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(vertical = 12.dp, horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "NAMA PEMERIKSAAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.width(200.dp)
                                )
                                Text(
                                    text = "HASIL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(
                                    text = "NILAI RUJUKAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.width(100.dp)
                                )
                                Text(
                                    text = "SATUAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(
                                    text = "KETERANGAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.width(200.dp)
                                )
                            }

                            // Dynamic tables based on testId
                            when (testId) {
                                1 -> {
                                    // result1: Darah Rutin, Nilai-Nilai MC, Hitung Jenis Leukosit
                                    TableSectionHeader("DARAH RUTIN")
                                    resultViewModel.getDarahRutinList().forEach { TableRow(it) }

                                    TableSectionHeader("NILAI-NILAI MC")
                                    resultViewModel.getNilaiMcList().forEach { TableRow(it) }

                                    TableSectionHeader("HITUNG JENIS LEUKOSIT")
                                    resultViewModel.getHitungJenisLeukositList().forEach { TableRow(it) }
                                }
                                2 -> {
                                    // result2: Hitung Jenis Leukosit only
                                    TableSectionHeader("HITUNG JENIS LEUKOSIT")
                                    resultViewModel.getHitungJenisLeukositList().forEach { TableRow(it) }
                                }
                                3 -> {
                                    // result3: Darah Rutin only
                                    TableSectionHeader("DARAH RUTIN")
                                    resultViewModel.getDarahRutinList().forEach { TableRow(it) }
                                }
                                else -> {
                                    // Fallback: Darah Rutin
                                    TableSectionHeader("DARAH RUTIN")
                                    resultViewModel.getDarahRutinList().forEach { TableRow(it) }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Download PDF Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    onClick = { /* Action Unduh PDF */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CB7A6)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Unduh PDF",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "UNDUH PDF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quote Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(0.5.dp, Color(0xFFE5E7EB)), shape = RoundedCornerShape(0.dp))
                    .padding(top = 24.dp, bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\"Dari semua itu, jika kepuasan tubuh ingin sehat, Anda harus memulainya dengan menyembuhkan jiwa.\"",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "~ Socrates ~",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun TableSectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6))
            .padding(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF374151),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun TableRow(param: TestParameterResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(0.5.dp, Color(0xFFF9FAFB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name (indented if it is a bullet sub-item)
            Row(
                modifier = Modifier.width(200.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (param.isBullet) {
                    Text(
                        text = "• ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                Text(
                    text = param.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )
            }

            // Result (bold and red if abnormal)
            Text(
                text = param.resultValue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (param.isOut) Color(0xFFEF4444) else Color(0xFF111827),
                modifier = Modifier.width(80.dp)
            )

            // Reference Range
            Text(
                text = param.referenceRange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280),
                modifier = Modifier.width(100.dp)
            )

            // Unit
            Text(
                text = param.unit,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280),
                modifier = Modifier.width(80.dp)
            )

            // Note/Keterangan
            Text(
                text = param.note,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280),
                modifier = Modifier.width(200.dp),
                lineHeight = 14.sp
            )
        }
    }
}
