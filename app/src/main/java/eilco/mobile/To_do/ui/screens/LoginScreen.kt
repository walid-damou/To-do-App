package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: ThemeViewModel
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading.value) {
            CircularProgressIndicator()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val emailInput = email.value.trim()
                        val passwordInput = password.value.trim()

                        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                            val auth = FirebaseAuth.getInstance()
                            auth.signInWithEmailAndPassword(email.value, password.value)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                                        if (userId != null) {
                                            viewModel.fetchUserId()
                                            viewModel.fetchThemeColor(userId)
                                            onLoginSuccess()
                                        } else {
                                            errorMessage.value = "Failed to retrieve user ID."
                                        }
                                    } else {
                                        errorMessage.value = task.exception?.message ?: "Failed to login."
                                    }
                                }
                        } else {
                            errorMessage.value = "Email or password cannot be empty."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                if (errorMessage.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage.value,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
