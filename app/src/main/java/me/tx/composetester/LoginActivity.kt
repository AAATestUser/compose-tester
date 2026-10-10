package me.tx.composetester

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import me.tx.composetester.base.ComposHttpClient
import me.tx.composetester.biz.BizRepository
import me.tx.composetester.biz.LoginRequest
import me.tx.composetester.biz.LoginResponse
import me.tx.composetester.ui.LoginScreen
import me.tx.composetester.ui.theme.ComposeTesterTheme

class LoginActivity : ComponentActivity() {
    private var isLoading by mutableStateOf(false)
    private var loginMessage by mutableStateOf<String?>(null)
    private var loginSucceeded by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTesterTheme(dynamicColor = false) {
                LoginScreen(
                    isLoading = isLoading,
                    message = loginMessage,
                    loginSucceeded = loginSucceeded,
                    onLogin = ::login
                )
            }
        }
    }

    @Preview(showBackground = true, name = "tx")
    @Composable
    fun GreetingPreview() {
        ComposeTesterTheme(dynamicColor = false) {
            LoginScreen(
                isLoading = isLoading,
                message = loginMessage,
                loginSucceeded = loginSucceeded,
                onLogin = ::login
            )
        }
    }

    private fun login(username: String, password: String) {
        if (isLoading) return
        isLoading = true
        loginMessage = null
        loginSucceeded = false
        BizRepository.login(
            LoginRequest(username, password),
            ::onLoginResult,
            ::onLoginFailed
        )
    }

    private fun onLoginResult(rsp: LoginResponse) {
        loginSucceeded = rsp.token.isNotBlank()
        if(loginSucceeded){
            ComposHttpClient.setHeader("token",rsp.token)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        loginMessage = if (loginSucceeded) "登录成功" else "登录失败，请检查账号密码后重试"
        isLoading = false
    }

    private fun onLoginFailed(code: Int, message: String) {
        loginSucceeded = false
        loginMessage = "登录失败（$code）：$message"
        isLoading = false
    }
}
