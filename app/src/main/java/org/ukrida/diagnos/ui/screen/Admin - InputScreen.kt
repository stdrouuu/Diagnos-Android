package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.ukrida.diagnos.data.model.AdminBooking
import org.ukrida.diagnos.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminInputScreen(
    viewModel: AdminViewModel,
    navController: NavController,
    onLogout: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getBookings()
    }
    val needInput = viewModel.bookings.value.filter { it.status == "Sedang diuji" && it.resultStatus == "Menunggu Hasil" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Input Hasil Lab",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1A2E35)
                        )
                        Text(
                            text = "Pilih pasien untuk menginput nilai hasil",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFFFF788F)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        bottomBar = {
            AdminBottomNav(navController = navController, currentRoute = "admin-input")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7F6))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (needInput.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 24.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Belum ada pasien yang menunggu input hasil laboratorium.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(needInput) { booking ->
                        InputQueueCard(
                            booking = booking,
                            onInputClick = { viewModel.selectedBookingForInput.value = booking }
                        )
                    }
                }
            }
        }
    }

    // Modal Input Hasil Dialog
    if (viewModel.selectedBookingForInput.value != null) {
        val booking = viewModel.selectedBookingForInput.value!!
        val isLeukosit = booking.testName.lowercase().contains("leukosit") || booking.testName.lowercase().contains("hitung jenis")
        val isDarahMC = booking.testName.lowercase().contains("darah rutin") || booking.testName.lowercase().contains("mc")

        // Group each test to its corresponding categories
        val groups = when {
            isLeukosit -> listOf(
                "Hitung Jenis Leukosit" to listOf("Basofil", "Eosinofil", "Neutrofil", "Limfosit", "Monosit")
            )
            isDarahMC -> listOf(
                "Darah Rutin" to listOf("Hemoglobin", "Hematokrit", "Eritrosit", "Trombosit", "RDW-CV", "Leukosit", "LED"),
                "Nilai-Nilai MC" to listOf("MCV", "MCH", "MCHC")
            )
            else -> listOf( // Hematologi Lengkap
                "Darah Rutin" to listOf("Hemoglobin", "Hematokrit", "Eritrosit", "Trombosit", "RDW-CV", "Leukosit", "LED"),
                "Nilai-Nilai MC" to listOf("MCV", "MCH", "MCHC"),
                "Hitung Jenis Leukosit" to listOf("Basofil", "Eosinofil", "Neutrofil", "Limfosit", "Monosit")
            )
        }

        InputResultModal(
            booking = booking,
            groups = groups,
            onDismiss = { viewModel.selectedBookingForInput.value = null },
            onSave = { resultsMap -> viewModel.saveInputResults(booking.id, resultsMap) }
        )
    }
}

@Composable
fun InputQueueCard(booking: AdminBooking, onInputClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.patientName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A2E35)
                    )
                    Text(
                        text = booking.testName,
                        fontSize = 11.sp,
                        color = Color(0xFF42B5A7),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${booking.id} · Jadwal: ${booking.date}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onInputClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42B5A7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text("Input Nilai Hasil Tes", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResultModal(
    booking: AdminBooking,
    groups: List<Pair<String, List<String>>>,
    onDismiss: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    val keys = remember(groups) { groups.flatMap { it.second } }
    // Stores inputs & validation error messages
    val inputs = remember { mutableStateMapOf<String, String>().apply { keys.forEach { put(it, "") } } }
    val errors = remember { mutableStateMapOf<String, String?>().apply { keys.forEach { put(it, null) } } }

    val decimalRegex = Regex("^-?\\d+\\.\\d+$")
    val integerOrDecimalRegex = Regex("^-?\\d+(\\.\\d+)?$")
    val leukositKeys = listOf("Basofil", "Eosinofil", "Neutrofil", "Limfosit", "Monosit")

    fun validateField(key: String, value: String): Boolean {
        if (value.trim().isEmpty()) {
            errors[key] = "Kolom ini wajib diisi"
            return false
        }
        val isLeukositKey = leukositKeys.contains(key)
        val matches = if (isLeukositKey) {
            integerOrDecimalRegex.matches(value.trim())
        } else {
            decimalRegex.matches(value.trim())
        }

        if (!matches) {
            errors[key] = if (isLeukositKey) {
                "Harus berupa angka (contoh: 60 atau 32.5)"
            } else {
                "Harus bilangan desimal (contoh: 12.0)"
            }
            return false
        } else {
            errors[key] = null
            return true
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Input Hasil Pemeriksaan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A2E35)
                    )
                    Text(
                        text = "${booking.patientName} - ${booking.testName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF42B5A7)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Clear, contentDescription = "Tutup", tint = Color.Gray)
                }
            }

            // Input Groups Cards
            groups.forEach { (groupName, groupKeys) ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Group Header with beautiful indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 4.dp, height = 16.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF42B5A7))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = groupName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A2E35)
                        )
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFE2E8F0))),
                            width = 0.5.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            groupKeys.forEach { key ->
                                val unit = getUnitForKey(key)
                                val refRange = getRefRangeForKey(key)

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = key,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1A2E35)
                                        )
                                        Text(
                                            text = "Ruj: $refRange",
                                            fontSize = 9.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    val errorMsg = errors[key]

                                    OutlinedTextField(
                                        value = inputs[key] ?: "",
                                        onValueChange = { newValue ->
                                            inputs[key] = newValue
                                            validateField(key, newValue)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text(text = getPlaceholderForKey(key), fontSize = 11.sp) },
                                        suffix = {
                                            Text(
                                                text = unit,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray
                                            )
                                        },
                                        isError = errorMsg != null,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF42B5A7),
                                            unfocusedBorderColor = Color(0xFFCBD5E1),
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White
                                        )
                                    )

                                    if (errorMsg != null) {
                                        Text(
                                            text = errorMsg,
                                            color = Color.Red,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Action Button
            Button(
                onClick = {
                    var allValid = true
                    keys.forEach { key ->
                        val isValid = validateField(key, inputs[key] ?: "")
                        if (!isValid) allValid = false
                    }
                    if (allValid) {
                        onSave(inputs)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42B5A7)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Simpan & Rilis Hasil Lab", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

fun getUnitForKey(key: String): String {
    return when (key) {
        "Hemoglobin", "MCHC" -> "g/dL"
        "Hematokrit", "Basofil", "Eosinofil", "Neutrofil", "Limfosit", "Monosit", "RDW-CV" -> "%"
        "Eritrosit" -> "10^6/uL"
        "MCV" -> "fL"
        "MCH" -> "pg"
        "Trombosit", "Leukosit" -> "10^3"
        "LED" -> "mm/jam"
        else -> ""
    }
}

fun getRefRangeForKey(key: String): String {
    return when (key) {
        "Hemoglobin" -> "11.7 - 15.5"
        "Hematokrit" -> "35 - 47"
        "Eritrosit" -> "3.8 - 5.2"
        "MCV" -> "80 - 100"
        "MCH" -> "26 - 34"
        "MCHC" -> "32 - 36"
        "RDW-CV" -> "11.5 - 14.5"
        "Trombosit" -> "150 - 440"
        "Leukosit" -> "3.6 - 11"
        "LED" -> "0 - 30"
        "Basofil" -> "0.0 - 1.0"
        "Eosinofil" -> "2.0 - 4.0"
        "Neutrofil" -> "50.0 - 70.0"
        "Limfosit" -> "25.0 - 40.0"
        "Monosit" -> "2.0 - 8.0"
        else -> ""
    }
}

fun getPlaceholderForKey(key: String): String {
    return when (key) {
        "Hemoglobin" -> "Contoh: 14.8"
        "Hematokrit" -> "Contoh: 44.5"
        "Eritrosit" -> "Contoh: 4.89"
        "MCV" -> "91.0"
        "MCH" -> "30.3"
        "MCHC" -> "33.3"
        "RDW-CV" -> "13.1"
        "Trombosit" -> "296.0"
        "Leukosit" -> "5.5"
        "LED" -> "Contoh: 17.0"
        "Basofil" -> "0.4"
        "Eosinofil" -> "2.5"
        "Neutrofil" -> "60.0"
        "Limfosit" -> "32.5"
        "Monosit" -> "4.9"
        else -> "0.0"
    }
}
