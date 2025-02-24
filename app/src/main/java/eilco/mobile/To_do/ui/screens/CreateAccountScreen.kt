package eilco.mobile.To_do.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.google.firebase.database.FirebaseDatabase
import eilco.mobile.To_do.ui.ThemeViewModel

@Composable
fun CreateAccountScreen(
    onFinish: () -> Unit,
    viewModel: ThemeViewModel,
    navController: NavController
) {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

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
                    text = "Create Account",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = firstName.value,
                    onValueChange = { firstName.value = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName.value,
                    onValueChange = { lastName.value = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
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
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                            message.value = "Please enter a valid email address."
                        } else if (password.value != confirmPassword.value) {
                            message.value = "Passwords do not match."
                        } else if (password.value.length < 6) {
                            message.value = "Password must be at least 6 characters."
                        } else {
                            isLoading.value = true
                            val auth = FirebaseAuth.getInstance()
                            auth.createUserWithEmailAndPassword(email.value.trim(), password.value.trim())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = task.result?.user?.uid
                                        if (userId != null) {
                                            saveUserToDatabase(
                                                userId = userId,
                                                firstName = firstName.value,
                                                lastName = lastName.value,
                                                email = email.value,
                                                onSuccess = {
                                                    message.value = "Account created successfully!"
                                                    isLoading.value = false
                                                    onFinish()
                                                },
                                                onError = { error ->
                                                    message.value = "Failed to save user: $error"
                                                    isLoading.value = false
                                                }
                                            )
                                        } else {
                                            message.value = "User ID is null."
                                            isLoading.value = false
                                        }
                                    } else {
                                        message.value = task.exception?.message ?: "Failed to create account."
                                        isLoading.value = false
                                    }
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (message.value.isNotEmpty()) {
                    Text(
                        text = message.value,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account? Log In",
                    modifier = Modifier.clickable { navController.navigate("login") },
                    style = MaterialTheme.typography.body2.copy(
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

private fun saveUserToDatabase(
    userId: String,
    firstName: String,
    lastName: String,
    email: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("users/$userId")

    val user = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "email" to email,
        "theme" to ""
    )

    userRef.setValue(user)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onError(exception.message ?: "Unknown error") }
}