package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import eilco.mobile.To_do.ui.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun CreateAccountScreen(
    onFinish: () -> Unit,
    viewModel: ThemeViewModel
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
                        if (password.value == confirmPassword.value && email.value.isNotEmpty()) {
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
                        } else {
                            message.value = "Passwords do not match or fields are empty."
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
