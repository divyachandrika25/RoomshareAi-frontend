package com.simats.roomshareairentalapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyVerificationScreen(
    verificationData: RoomShareVerificationData? = null,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onVerifyClick: (String?) -> Unit = {}
) {
    var isIDScanned by rememberSaveable { mutableStateOf(false) }
    var scannedImageUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var showOptions by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            scannedImageUriString = tempCameraUri.toString()
            isIDScanned = true
        }
    }

    // Function to create temporary image URI
    fun createImageUri(): Uri? {
        return try {
            val directory = File(context.cacheDir, "id_scans")
            if (!directory.exists()) directory.mkdirs()
            
            val file = File(directory, "id_scan_${System.currentTimeMillis()}.jpg")
            FileProvider.getUriForFile(
                context,
                "com.simats.roomshareairentalapp.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri()
            if (uri != null) {
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scannedImageUriString = uri.toString()
            isIDScanned = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Safety Check",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF0D1E3C)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5FE))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF0D1E3C)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                // Progress Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (index <= 2) Color(0xFF1E63FF) else Color(0xFFF1F5FE))
                        )
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (!isIDScanned) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please scan or upload your Identity document first")
                        }
                    } else {
                        onVerifyClick(scannedImageUriString)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E63FF)
                )
            ) {
                Text(
                    verificationData?.verifyButtonText ?: "Verify Identity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1E63FF))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = verificationData?.title ?: "Identity Verification",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D1E3C)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = verificationData?.subtitle ?: "Please scan or upload a government-issued ID (Aadhar, PAN, Passport) to continue.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Verification Items
                VerificationCard(
                    title = "AI Background Check",
                    subtitle = "Records verified automatically.",
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE8F0FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Color(0xFF1E63FF)
                            )
                        }
                    },
                    trailing = {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VerificationCard(
                    title = "Identity Document",
                    subtitle = if (isIDScanned) "Document captured successfully" else "Tap to scan or upload ID.",
                    icon = {
                        Surface(
                            onClick = { showOptions = true },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isIDScanned) Color(0xFFF1F5FE) else Color(0xFFF8F9FE),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isIDScanned && scannedImageUriString != null) {
                                    AsyncImage(
                                        model = scannedImageUriString?.toUri(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Outlined.CameraAlt,
                                        contentDescription = null,
                                        tint = if (isIDScanned) Color(0xFF1E63FF) else Color.LightGray
                                    )
                                }
                            }
                        }
                    },
                    trailing = if (isIDScanned) {
                        {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = "Scanned",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else null
                )
                
                if (showOptions) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (verificationData?.cameraEnabled != false) {
                            Button(
                                onClick = {
                                    showOptions = false
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        val uri = createImageUri()
                                        if (uri != null) {
                                            tempCameraUri = uri
                                            cameraLauncher.launch(uri)
                                        }
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5FE)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Outlined.CameraAlt, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Camera", color = Color(0xFF1E63FF), fontSize = 14.sp)
                            }
                        }
                        
                        if (verificationData?.galleryEnabled != false) {
                            Button(
                                onClick = {
                                    showOptions = false
                                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5FE)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Outlined.PhotoLibrary, null, tint = Color(0xFF1E63FF), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gallery", color = Color(0xFF1E63FF), fontSize = 14.sp)
                            }
                        }
                    }
                }
                
                if (isIDScanned && scannedImageUriString != null) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Document Preview",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0D1E3C)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        AsyncImage(
                            model = scannedImageUriString?.toUri(),
                            contentDescription = "ID Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    
                    TextButton(
                        onClick = { showOptions = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
                    ) {
                        Text("Change Document", color = Color(0xFF1E63FF), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VerificationCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5FE)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1E3C)
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SafetyVerificationScreenPreview() {
    RoomshareAIRentalAppTheme {
        SafetyVerificationScreen()
    }
}
