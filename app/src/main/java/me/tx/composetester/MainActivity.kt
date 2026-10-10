package me.tx.composetester

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import me.tx.composetester.biz.BizRepository
import me.tx.composetester.biz.User
import me.tx.composetester.ui.HomeScreen
import me.tx.composetester.ui.LoginScreen
import me.tx.composetester.ui.theme.ComposeTesterTheme

class MainActivity : ComponentActivity() {
    private var users by mutableStateOf<List<User>>(emptyList())
    private var nextPage by mutableStateOf(1)
    private var isLoading by mutableStateOf(false)
    private var hasMore by mutableStateOf(true)
    private var loadError by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTesterTheme(dynamicColor = false) {
                HomeScreen(
                    users = users,
                    isLoading = isLoading,
                    hasMore = hasMore,
                    loadError = loadError,
                    onLoadMore = ::loadNextPage,
                    onRetry = ::loadNextPage,
                    onMenuSelected = ::onMenuSelected
                )
            }
        }
        loadNextPage()
    }

    private fun loadNextPage() {
        if (isLoading || !hasMore) return
        isLoading = true
        loadError = null
        BizRepository.userList(
            page = nextPage,
            size = 20,
            onResult = ::onUsersLoaded,
            onFailed = ::onUsersLoadFailed
        )
    }

    private fun onUsersLoaded(pageUsers: List<User>) {
        users = users + pageUsers
        nextPage += 1
        hasMore = pageUsers.size == 20
        isLoading = false
    }

    private fun onUsersLoadFailed(code: Int, message: String) {
        loadError = "加载失败（$code）：$message"
        isLoading = false
    }

    private fun onMenuSelected(item: String) {
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show()
    }

    @Preview(showBackground = true, name = "tx")
    @Composable
    fun GreetingPreview() {
        ComposeTesterTheme(dynamicColor = false) {
            users = listOf(
                User("User 1", "User 1", 18, "123123123"),
                User("User 2", "User 2", 18, "123123123"),
                User("User 3", "User 3", 18, "123123123")
            )
            HomeScreen(
                users = users,
                isLoading = isLoading,
                hasMore = hasMore,
                loadError = loadError,
                onLoadMore = ::loadNextPage,
                onRetry = ::loadNextPage,
                onMenuSelected = ::onMenuSelected
            )
        }
    }
}
