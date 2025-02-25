import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(navController: NavController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var firstName by remember { mutableStateOf("Loading...") }
    var lastName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var isSaving by remember { mutableStateOf(false) }
    var saveMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Fetch user data from Firebase
    LaunchedEffect(userId) {
        userId?.let {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(it)
            userRef.get().addOnSuccessListener { snapshot ->
                firstName = snapshot.child("firstName").getValue(String::class.java) ?: "No Name"
                lastName = snapshot.child("lastName").getValue(String::class.java) ?: "No Name"
                email = snapshot.child("email").getValue(String::class.java) ?: "No Email"
            }.addOnFailureListener {
                firstName = "Failed to load"
                lastName = "Failed to load"
                email = "Failed to load"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Account", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // First Name Field
            Text("First Name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter first name") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Last Name Field
            Text("Last Name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter last name") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email Field
            Text("Email", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("name@example.com") },
                enabled = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Change Password Button
            Text("Password", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            OutlinedButton(
                onClick = { /* Implement password change functionality */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Change Password")
            }

            Spacer(modifier = Modifier.height(52.dp))

            // Save Changes Button
            Button(
                onClick = {
                    if (userId != null) {
                        isSaving = true
                        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                        coroutineScope.launch {
                            userRef.child("firstName").setValue(firstName)
                            userRef.child("lastName").setValue(lastName).addOnSuccessListener {
                                isSaving = false
                                saveMessage = "Changes saved successfully!"
                            }.addOnFailureListener {
                                isSaving = false
                                saveMessage = "Failed to save changes."
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2A9D8F))
            ) {
                Text("Save Changes", color = Color.White, fontSize = 16.sp)
            }

            if (isSaving) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (saveMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(saveMessage, color = if (saveMessage.contains("successfully")) Color.Green else Color.Red)
            }
        }
    }
}
