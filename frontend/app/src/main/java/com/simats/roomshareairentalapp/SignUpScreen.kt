package com.simats.roomshareairentalapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.simats.roomshareairentalapp.ui.theme.RoomshareAIRentalAppTheme
import kotlinx.coroutines.launch
import org.json.JSONObject
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onCreateAccountClick: (String) -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var triedToSubmit by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validateEmail(mail: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    fun validatePassword(pass: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$"
        return pass.matches(passwordPattern.toRegex())
    }

    fun validateAge(a: String): Boolean {
        val ageInt = a.toIntOrNull() ?: return false
        return ageInt in 18..100
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppTheme.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Premium Back Navigation
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, AppTheme.Divider, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AppTheme.TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Establish Identity",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = AppTheme.TextPrimary,
                    letterSpacing = (-1.5).sp
                )
                Text(
                    text = "Begin your journey with a verified profile.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            SignUpTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "FULL IDENTITY NAME",
                placeholder = "e.g. Alexander Johnson",
                leadingIcon = Icons.Outlined.Person,
                isError = triedToSubmit && fullName.isBlank()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                SignUpGenderDropdown(
                    value = gender,
                    onValueChange = { gender = it },
                    label = "GENDER",
                    options = listOf("Male", "Female", "Prefer not to say"),
                    leadingIcon = Icons.Outlined.Transgender,
                    modifier = Modifier.weight(1.1f),
                    isError = triedToSubmit && gender.isBlank()
                )
                Spacer(modifier = Modifier.width(16.dp))
                SignUpTextField(
                    value = age,
                    onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                    label = "AGE",
                    placeholder = "Number",
                    leadingIcon = Icons.Outlined.CalendarToday,
                    modifier = Modifier.weight(0.9f),
                    isError = triedToSubmit && !validateAge(age),
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SignUpTextField(
                value = occupation,
                onValueChange = { occupation = it },
                label = "PROFESSIONAL ROLE",
                placeholder = "e.g. Software Architect",
                leadingIcon = Icons.Outlined.WorkOutline,
                isError = triedToSubmit && occupation.isBlank()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignUpTextField(
                value = address,
                onValueChange = { address = it },
                label = "GEOGRAPHIC REGION",
                placeholder = "City, Region",
                leadingIcon = Icons.Outlined.LocationOn,
                isError = triedToSubmit && address.isBlank()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignUpTextField(
                value = email,
                onValueChange = { email = it },
                label = "COMMUNICATION NODE (EMAIL)",
                placeholder = "identity@example.com",
                leadingIcon = Icons.Outlined.Email,
                isError = triedToSubmit && (email.isBlank() || !validateEmail(email)),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignUpTextField(
                value = password,
                onValueChange = { password = it },
                label = "SECURITY PROTOCOL (PASSWORD)",
                placeholder = "••••••••",
                leadingIcon = Icons.Outlined.VpnKey,
                isPassword = true,
                isError = triedToSubmit && !validatePassword(password)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    triedToSubmit = true
                    if (fullName.isBlank() || gender.isBlank() || !validateAge(age) || 
                        occupation.isBlank() || address.isBlank() || 
                        !validateEmail(email) || !validatePassword(password)) {
                        
                        val errorMsg = when {
                            fullName.isBlank() -> "Name is required"
                            gender.isBlank() -> "Gender is required"
                            !validateAge(age) -> "Age must be between 18-100"
                            occupation.isBlank() -> "Occupation is required"
                            address.isBlank() -> "Address is required"
                            !validateEmail(email) -> "Valid email is required"
                            !validatePassword(password) -> "Password must be at least 6 characters, with uppercase, lowercase, number, and special character"
                            else -> "Please validate all fields"
                        }
                        scope.launch { snackbarHostState.showSnackbar(errorMsg) }
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        try {
                            val request = RegisterRequest(
                                username = email.trim(),
                                fullName = fullName.trim(),
                                gender = gender,
                                age = age.toIntOrNull() ?: 0,
                                occupation = occupation.trim(),
                                email = email.trim(),
                                address = address.trim(),
                                password = password,
                                confirmPassword = password
                            )
                            val response = RetrofitClient.instance.register(request)
                            if (response.isSuccessful) {
                                onCreateAccountClick(email.trim())
                            } else {
                                val errorBody = response.errorBody()?.string() ?: ""
                                val message = try { JSONObject(errorBody).optString("error", "Registration failed") } catch(_:Exception) { "Registration failed" }
                                snackbarHostState.showSnackbar(message)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Network architecture failure")
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.Primary),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Establish Profile", fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Previously established? ", color = AppTheme.TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Sign In",
                    color = AppTheme.Primary,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    keyboardType: androidx.compose.ui.text.input.KeyboardType = androidx.compose.ui.text.input.KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(
            text = label, 
            fontSize = 10.sp, 
            fontWeight = FontWeight.Black, 
            color = AppTheme.TextTertiary, 
            modifier = Modifier.padding(bottom = 8.dp),
            letterSpacing = 1.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = AppTheme.TextTertiary, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(leadingIcon, null, tint = AppTheme.PrimaryAlpha40, modifier = Modifier.size(20.dp)) },
            shape = RoundedCornerShape(AppTheme.RadiusMd),
            singleLine = true,
            isError = isError,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = AppTheme.Primary,
                unfocusedBorderColor = AppTheme.Divider,
                errorBorderColor = AppTheme.Error,
                focusedTextColor = AppTheme.TextPrimary,
                unfocusedTextColor = AppTheme.TextPrimary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpGenderDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label, 
            fontSize = 10.sp, 
            fontWeight = FontWeight.Black, 
            color = AppTheme.TextTertiary, 
            modifier = Modifier.padding(bottom = 8.dp),
            letterSpacing = 1.sp
        )
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = if (value.isEmpty()) "Select" else value,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                leadingIcon = { Icon(leadingIcon, null, tint = AppTheme.PrimaryAlpha40, modifier = Modifier.size(20.dp)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(AppTheme.RadiusMd),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AppTheme.Primary,
                    unfocusedBorderColor = AppTheme.Divider,
                    errorBorderColor = AppTheme.Error,
                    focusedTextColor = if (value.isEmpty()) AppTheme.TextTertiary else AppTheme.TextPrimary,
                    unfocusedTextColor = if (value.isEmpty()) AppTheme.TextTertiary else AppTheme.TextPrimary
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption, fontWeight = FontWeight.Bold, color = AppTheme.TextPrimary) },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    RoomshareAIRentalAppTheme {
        SignUpScreen()
    }
}

