package me.tx.composetester.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Night = Color(0xFF091423)
private val Teal = Color(0xFF6FE6D2)
private val MutedText = Color(0xFFA6B5C5)

@Composable
fun LoginScreen(
    isLoading: Boolean,
    message: String?,
    loginSucceeded: Boolean,
    onLogin: (String, String) -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var validationMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Night)
    ) {
        LoginBackdrop()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF69E5D0), Color(0xFF4CA8D8))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    color = Night,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Aurora",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "欢迎回来，请登录您的账号",
                color = MutedText,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(38.dp))

            LoginInput(
                value = username,
                onValueChange = {
                    username = it
                    validationMessage = null
                },
                label = "账号",
                placeholder = "请输入账号",
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(16.dp))
            LoginInput(
                value = password,
                onValueChange = {
                    password = it
                    validationMessage = null
                },
                label = "密码",
                placeholder = "请输入密码",
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )
            if (validationMessage != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = validationMessage.orEmpty(),
                    color = Color(0xFFFFA8A8),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (message != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = message,
                    color = if (loginSucceeded) Teal else Color(0xFFFFA8A8),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        validationMessage = "请输入账号和密码"
                    } else {
                        validationMessage = null
                        onLogin(username.trim(), password)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal,
                    contentColor = Night,
                    disabledContainerColor = Teal.copy(alpha = 0.55f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Night,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("登 录", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(34.dp))
            Text(
                text = "© 2026 Aurora. All rights reserved.",
                color = MutedText.copy(alpha = 0.7f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoginInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color(0xFFE6EDF5),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(9.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(placeholder, color = MutedText.copy(alpha = 0.7f)) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Teal,
                unfocusedBorderColor = Color(0xFF33475B),
                focusedContainerColor = Color(0xFF112236).copy(alpha = 0.78f),
                unfocusedContainerColor = Color(0xFF112236).copy(alpha = 0.78f),
                cursorColor = Teal
            )
        )
    }
}

@Composable
private fun LoginBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF10263A), Night, Color(0xFF07101D))
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF227C83).copy(alpha = 0.36f), Color.Transparent)
            ),
            radius = size.width * 0.62f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.96f, size.height * 0.12f)
        )
        drawCircle(
            color = Color(0xFF6794D8).copy(alpha = 0.09f),
            radius = size.width * 0.36f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.03f, size.height * 0.86f),
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF6794D8).copy(alpha = 0.07f),
            radius = size.width * 0.48f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.03f, size.height * 0.86f),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}
