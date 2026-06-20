package org.ukrida.diagnos.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import org.ukrida.diagnos.data.model.User
import org.ukrida.diagnos.viewmodel.UserViewModel
import java.io.File

@Composable
fun RegisterScreen(
    viewModel: UserViewModel,
    onRegisterSuccess: () -> Unit
) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pendaftaran Pengguna",
            style = MaterialTheme.typography.headlineMedium
        )

        // ================= PREVIEW FOTO =================
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto Profil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            } else {
                Surface(
                    modifier = Modifier.size(150.dp),
                    shape = CircleShape,
                    tonalElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
        }

        // ================= BUTTON GALERI =================
        Button(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A9A4D),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Photo, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pilih dari Galeri")
        }

        // ================= BUTTON KAMERA =================
        Button(
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
                        permissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A9A4D),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.CameraAlt, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ambil Foto")
        }
        // ================= NAMA =================
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Nama Lengkap")
            },
            leadingIcon = {
                Icon(Icons.Default.Badge, null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        // ================= USERNAME =================
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            },
            leadingIcon = {
                Icon(Icons.Default.Person, null)
            },
            modifier = Modifier.fillMaxWidth()
        )

        // ================= PASSWORD =================
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, null)
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        // ================= BUTTON REGISTER =================
        Button(
            onClick = {
                val user = User(
                    id = 0,
                    name = name,
                    username = username,
                    password = password,
                    // ROLE DEFAULT
                    role = role,
                    photo = imageUri?.toString()
                )
                viewModel.insert(user)
                onRegisterSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A9A4D),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("DAFTAR")
        }
    }
}
