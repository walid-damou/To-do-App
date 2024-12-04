package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.dp
import eilco.mobile.To_do.R
import eilco.mobile.To_do.ui.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen(onLoginSuccess: () -> Unit,
                viewModel: ThemeViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
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
                    val emailInput = email.value
                    val passwordInput = password.value
                    if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                        val auth = FirebaseAuth.getInstance()
                        auth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("Login successful!")
                                    // Navigate to next screen or show success message
                                } else {
                                    println("Error: ${task.exception?.message}")
                                    // Show error message to the user
                                }
                            }
                    } else {
                        println("Email or password cannot be empty.")
                        // Show error message for empty fields
                    }
                },
                onClick = {
                    val userId = "user7"
                    viewModel.fetchThemeColor(userId) // Fetch theme color dynamically
                    onLoginSuccess() // Navigate to the next screen
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "or continue with",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Handle Facebook Login */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook Login"
                    )
                }
                IconButton(onClick = { /* Handle Google Login */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Login"
                    )
                }
            }
        }
    }
}
