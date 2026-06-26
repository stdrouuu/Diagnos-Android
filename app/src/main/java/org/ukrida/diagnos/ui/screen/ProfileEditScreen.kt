package org.ukrida.diagnos.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ukrida.diagnos.R
import org.ukrida.diagnos.data.model.User
import org.ukrida.diagnos.viewmodel.UserViewModel
import androidx.navigation.NavHostController
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: UserViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentUser = viewModel.currentUser.value

    var name by remember { mutableStateOf(currentUser?.name ?: "Estero") }
    var email by remember { mutableStateOf(currentUser?.email ?: "estero@diagnos.co.id") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "81234567890") }
    var gender by remember { mutableStateOf(if (currentUser?.gender == "P") "Perempuan" else "Laki-laki") }
    var dob by remember { mutableStateOf(currentUser?.dob ?: "1995-08-15") }
    var address by remember { mutableStateOf(currentUser?.address ?: "The Gallery Blok 8, No. EG, Jl. Pantai Indah Utara, Jakarta Utara") }
    
    // Profile photo image Uri state
    var imageUri by remember {
        mutableStateOf<Uri?>(currentUser?.photo?.let { Uri.parse(it) })
    }

    // Temporary Camera URI
    var cameraImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isSaving by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    // ================= GALERI =================
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
        }
    }

    // ================= KAMERA =================
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri
        }
    }

    // ================= PERMISSION =================
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(
                context.cacheDir,
                "camera_photo.jpg"
            )
            cameraImageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            cameraImageUri?.let {
                cameraLauncher.launch(it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Sticky Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .background(Color(0xFFFAFAFA))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Edit Profil",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )
            }

            // Form Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp)
            ) {
                // Profile Avatar Photo Upload Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .shadow(2.dp, shape = CircleShape)
                            .background(Color.White, CircleShape)
                            .border(4.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Foto Profil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = R.drawable.images,
                                contentDescription = "Foto Profil Default",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gallery & Camera Buttons Row (SAME styling as in Register screen)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4B5563)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = "Galeri",
                                tint = Color(0xFF3CAEA3),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galeri", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }

                        OutlinedButton(
                            onClick = {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        val file = File(
                                            context.cacheDir,
                                            "camera_photo.jpg"
                                        )
                                        cameraImageUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                        cameraImageUri?.let {
                                            cameraLauncher.launch(it)
                                        }
                                    }
                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4B5563)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Kamera",
                                tint = Color(0xFF3CAEA3),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kamera", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Inputs Forms
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Nama Lengkap
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "NAMA LENGKAP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Masukkan nama lengkap", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color(0xFF3CB7A6),
                                unfocusedIndicatorColor = Color(0xFFE5E7EB),
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Alamat Email
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "ALAMAT EMAIL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Masukkan alamat email", color = Color(0xFF9CA3AF)) },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color(0xFF3CB7A6),
                                unfocusedIndicatorColor = Color(0xFFE5E7EB),
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Nomor Telepon
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "NOMOR TELEPON",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+62",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4B5563)
                                )
                            }
                            TextField(
                                value = phone,
                                onValueChange = { phone = it },
                                placeholder = { Text("812 3456 7890", color = Color(0xFF9CA3AF)) },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color(0xFF3CB7A6),
                                    unfocusedIndicatorColor = Color(0xFFE5E7EB),
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Gender & DOB Grid
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Jenis Kelamin
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "JENIS KELAMIN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            var genderExpanded by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                                    .clickable { genderExpanded = true }
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = gender,
                                        color = Color.Black,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF)
                                    )
                                }
                                DropdownMenu(
                                    expanded = genderExpanded,
                                    onDismissRequest = { genderExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Laki-laki") },
                                        onClick = {
                                            gender = "Laki-laki"
                                            genderExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Perempuan") },
                                        onClick = {
                                            gender = "Perempuan"
                                            genderExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Tanggal Lahir
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "TANGGAL LAHIR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            val calendar = Calendar.getInstance()
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    dob = "$year-${month + 1}-$dayOfMonth"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                                    .clickable { datePickerDialog.show() }
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (dob.isEmpty()) "Pilih Tanggal" else dob,
                                        color = if (dob.isEmpty()) Color(0xFF9CA3AF) else Color.Black,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Alamat Lengkap
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "ALAMAT LENGKAP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        TextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("Masukkan alamat lengkap...", color = Color(0xFF9CA3AF)) },
                            singleLine = false,
                            minLines = 3,
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color(0xFF3CB7A6),
                                unfocusedIndicatorColor = Color(0xFFE5E7EB),
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Sticky Bottom Save Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color(0xFFF3F4F6))
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Button(
                onClick = {
                    if (currentUser != null && !isSaving) {
                        coroutineScope.launch {
                            isSaving = true
                            delay(1500) // simulated loading delay
                            isSaving = false
                            
                            val updatedUser = currentUser.copy(
                                name = name,
                                email = email,
                                phone = phone,
                                gender = if (gender == "Perempuan") "P" else "L",
                                dob = dob,
                                address = address,
                                photo = imageUri?.toString()
                            )
                            viewModel.update(updatedUser)
                            
                            showToast = true
                            // Auto hide toast after 3 seconds
                            delay(3000)
                            showToast = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3CB7A6),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Menyimpan...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else {
                        Text(
                            text = "Simpan Perubahan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Floating Success Toast Notification
        if (showToast) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3CB7A6), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Profil berhasil diperbarui!",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
