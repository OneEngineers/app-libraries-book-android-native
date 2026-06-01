package com.ones.assistant.presentation.views.users


import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.ones.assistant.R
import com.ones.assistant.presentation.components.resolveProfileImageModel
import com.ones.assistant.presentation.viewmodel.ProfileViewModel
import com.ones.assistant.utilities.uriToBase64

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProfileScreen(
                    onBackClick = { finish() },
                    onLogoutClick = { /* Handle logout */ },
                    onSettingsClick = { /* Handle settings navigation */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    
    val uiState = profileUiState
    var showEditDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Close dialog when update succeeds
    val lastSuccessNonce = remember { mutableIntStateOf(uiState.updateSuccessNonce) }
    LaunchedEffect(uiState.updateSuccessNonce) {
        if (uiState.updateSuccessNonce != lastSuccessNonce.intValue) {
            lastSuccessNonce.intValue = uiState.updateSuccessNonce
            showEditDialog = false
            snackbarHostState.showSnackbar("Profile updated")
        }
    }

    LaunchedEffect(uiState.updateErrorMessage) {
        val msg = uiState.updateErrorMessage
        if (!msg.isNullOrBlank()) snackbarHostState.showSnackbar(msg)
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Red
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Error: ${uiState.errorMessage}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { profileViewModel.retry() },
                            modifier = Modifier
                                .height(48.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7B68EE)
                            )
                        ) {
                            Text(
                                text = "Retry",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                uiState.user != null -> {
                    ProfileContent(
                        user = uiState.user,
                        onLogoutClick = onLogoutClick,
                        onHistoryClick = onHistoryClick,
                        onEditProfileClick = { showEditDialog = true }
                    )
                }
            }

            if (uiState.isUpdating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (showEditDialog && uiState.user != null) {
        EditProfileDialog(
            currentName = uiState.user.name,
            onDismiss = { showEditDialog = false },
            onSave = { newName, imageBase64 ->
                profileViewModel.updateProfile(token = null, displayName = newName, imageProfile = imageBase64)
            },
            isSaving = uiState.isUpdating
        )
    }
}

@Composable
private fun ProfileContent(
    user: com.ones.assistant.data.model.User,
    onLogoutClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val model = remember(user.profileImageUrl) {
                    resolveProfileImageModel(user.profileImageUrl)
                }

                if (model == null) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(model = model),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // User Name
                Text(
                    text = user.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // User Email
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Member Since
                Text(
                    text = "Member since ${formatDate(user.createdAt)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        // Profile Options
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ProfileOptionItem(
                icon = Icons.Default.Person,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                onClick = onEditProfileClick
            )
            
            ProfileOptionItem(
                icon = Icons.Default.Book,
                title = "My Books",
                subtitle = "View borrowed and reserved books",
                onClick = { /* Handle my books */ }
            )
            
            ProfileOptionItem(
                icon = Icons.Default.History,
                title = "Reading History",
                subtitle = "Books you've read",
                onClick = onHistoryClick
            )
            
            ProfileOptionItem(
                icon = Icons.AutoMirrored.Filled.Help,
                title = "Help & Support",
                subtitle = "Get help and contact support",
                onClick = { /* Handle help */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Logout Button
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onSave: (displayName: String?, imageBase64: String?) -> Unit,
    isSaving: Boolean
) {
    val context = LocalContext.current
    var displayName by remember { mutableStateOf(currentName) }
    var selectedImageBase64 by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        try {
            imageError = null
            selectedImageBase64 = uriToBase64(context.contentResolver, uri)
        } catch (e: Exception) {
            imageError = e.message ?: "Failed to read image"
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        title = { Text("Edit profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    singleLine = true,
                    label = { Text("Display name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { pickImageLauncher.launch("image/*") },
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedImageBase64 != null) "Change photo" else "Choose photo")
                }
                if (imageError != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(text = imageError ?: "", color = Color.Red)
                }
                if (selectedImageBase64 != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Photo selected",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val name = displayName.trim().ifBlank { null }
                    onSave(name, selectedImageBase64)
                },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) { Text("Cancel") }
        }
    )
}

private fun formatDate(dateString: String): String {
    return try {
        val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
        val formatter = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.US)
        val date = parser.parse(dateString)
        if (date != null) formatter.format(date) else "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1A237E),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            onBackClick = {},
            onLogoutClick = {},
            onSettingsClick = {}
        )
    }
}
