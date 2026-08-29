// View: Layar Registrasi untuk pendaftaran akun pengguna baru
package org.ukrida.diagnos.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import org.ukrida.diagnos.R
import org.ukrida.diagnos.data.model.User
import org.ukrida.diagnos.viewmodel.UserViewModel
import java.io.File
import android.app.DatePickerDialog
import java.util.Calendar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowDropDown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: UserViewModel,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isTermsChecked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Laki-laki") }
    var dob by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // ROLE DEFAULT USER
    val role = "user"

    // URI gambar utama
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    // URI sementara kamera
    var cameraImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

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
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onRegisterSuccess() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF4B5563)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.logoname),
                    contentDescription = "Diagnos",
                    modifier = Modifier.height(28.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title "Daftar Akun"
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF3CAEA3))) {
                        append("Daftar ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF86E2D5))) {
                        append("Akun")
                    }
                },
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.2).sp,
                textAlign = TextAlign.Center
            )

            // Subtitle
            Text(
                text = "Mulai perjalanan kesehatan yang terpersonalisasi bersama kami.",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            )

            // Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(0.2.dp, shape = RoundedCornerShape(40.dp)),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFE5E7EB), Color(0xFFE5E7EB))
                    ),
                    width = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Photo Upload Section
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .shadow(0.2.dp, shape = CircleShape)
                            .background(Color.White, CircleShape)
                            .border(2.dp, Color.White, CircleShape),
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
                            Image(
                                painter = painterResource(id = R.drawable.images),
                                contentDescription = "Foto Profil Placeholder",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Camera & Gallery Buttons Row
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // INPUTS FORM
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Nama Lengkap
                        Column {
                            Text(
                                text = "NAMA LENGKAP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("Nama sesuai identitas", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Nama",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F5F7),
                                    unfocusedContainerColor = Color(0xFFF4F5F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Username
                        Column {
                            Text(
                                text = "USERNAME",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            TextField(
                                value = username,
                                onValueChange = { username = it },
                                placeholder = { Text("username_unik", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AlternateEmail,
                                        contentDescription = "Username",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F5F7),
                                    unfocusedContainerColor = Color(0xFFF4F5F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Kata Sandi
                        Column {
                            Text(
                                text = "KATA SANDI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("Minimal 8 karakter", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Kata Sandi",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                },
                                trailingIcon = {
                                    val image = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff

                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color(0xFF9CA3AF))
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F5F7),
                                    unfocusedContainerColor = Color(0xFFF4F5F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                                                }

                        // Alamat Email
                        Column {
                            Text(
                                text = "ALAMAT EMAIL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("contoh@email.com", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F5F7),
                                    unfocusedContainerColor = Color(0xFFF4F5F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Nomor Telepon
                        Column {
                            Text(
                                text = "NOMOR TELEPON",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF4F5F7), RoundedCornerShape(16.dp))
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
                                    placeholder = { Text("812 3456 7890", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Telepon",
                                            tint = Color(0xFF9CA3AF)
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF4F5F7),
                                        unfocusedContainerColor = Color(0xFFF4F5F7),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Jenis Kelamin
                        Column {
                            Text(
                                text = "JENIS KELAMIN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            var genderExpanded by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF4F5F7), RoundedCornerShape(16.dp))
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
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
                        Column {
                            Text(
                                text = "TANGGAL LAHIR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
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
                                    .background(Color(0xFFF4F5F7), RoundedCornerShape(16.dp))
                                    .clickable { datePickerDialog.show() }
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (dob.isEmpty()) "Pilih Tanggal Lahir" else dob,
                                        color = if (dob.isEmpty()) Color(0xFF9CA3AF) else Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF)
                                    )
                                }
                            }
                        }

                        // Alamat Lengkap
                        Column {
                            Text(
                                text = "ALAMAT LENGKAP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                            )
                            TextField(
                                value = address,
                                onValueChange = { address = it },
                                placeholder = { Text("Alamat lengkap tempat tinggal...", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Alamat",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                },
                                singleLine = false,
                                minLines = 3,
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF4F5F7),
                                    unfocusedContainerColor = Color(0xFFF4F5F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Terms and Conditions checkbox
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Checkbox(
                                checked = isTermsChecked,
                                onCheckedChange = { isTermsChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3CAEA3),
                                    uncheckedColor = Color(0xFF9CA3AF)
                                ),
                                modifier = Modifier.offset(y = (-8).dp)
                            )
                            Text(
                                text = buildAnnotatedString {
                                    append("Saya menyetujui ")
                                    withStyle(style = SpanStyle(color = Color(0xFF3CAEA3), fontWeight = FontWeight.Bold)) {
                                        append("Syarat & Ketentuan")
                                    }
                                    append(" serta ")
                                    withStyle(style = SpanStyle(color = Color(0xFF3CAEA3), fontWeight = FontWeight.Bold)) {
                                        append("Kebijakan Privasi")
                                    }
                                    append(" Diagnōs.")
                                },
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                lineHeight = 16.sp,
                                modifier = Modifier.clickable { isTermsChecked = !isTermsChecked }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Error Message validation display
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFFF86066),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Button Daftar
            Button(
                onClick = {
                    if (name.isBlank() || username.isBlank() || password.isBlank() || email.isBlank() || phone.isBlank() || dob.isBlank() || address.isBlank()) {
                        errorMessage = "Semua field input harus diisi!"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        errorMessage = "Format alamat email tidak valid!"
                    } else if (!isTermsChecked) {
                        errorMessage = "Anda harus menyetujui Syarat & Ketentuan!"
                    } else {
                        errorMessage = null
                        val user = User(
                            id = 0,
                            name = name,
                            username = username,
                            password = password,
                            role = role,
                            photo = imageUri?.toString(),
                            email = email,
                            phone = phone,
                            gender = if (gender == "Laki-laki") "L" else "P",
                            dob = dob,
                            address = address
                        )
                        viewModel.insert(user)
                        onRegisterSuccess() // Redirect back to login page
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF39B3A3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(0.2.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "Daftar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Redirect back to login page link
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sudah memiliki akun? ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Masuk di sini",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3CAEA3),
                    modifier = Modifier.clickable { onRegisterSuccess() }
                )
            }
        }
    }
}
