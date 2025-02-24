package eilco.mobile.To_do.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import eilco.mobile.To_do.ui.ThemeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import eilco.mobile.To_do.R

@Composable
fun ProfileScreen(navController: NavController, viewModel: ThemeViewModel) {
    var isDarkMode by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var hasNavigated by remember { mutableStateOf(false) }

    // Navigate to login only once if the user is logged out
    LaunchedEffect(userId) {
        if (userId == null && !hasNavigated) {
            hasNavigated = true
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true } // Clear back stack
            }
        }
    }

    if (userId == null) return

    var firstName by remember { mutableStateOf("Loading...") }
    var lastName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        val userData = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
        userData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    firstName = snapshot.child("firstName").getValue(String::class.java) ?: "No Name"
                    lastName = snapshot.child("lastName").getValue(String::class.java) ?: "No Name"
                    email = snapshot.child("email").getValue(String::class.java) ?: "No Email"
                    profileImageUrl = snapshot.child("profilePicture").getValue(String::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                firstName = "Error loading"
            }
        })
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("inbox") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.saad),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("$firstName $lastName", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(email, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                SettingsItem(Icons.Default.Person, "Account") {}
                SettingsItem(Icons.Default.Palette, "Theme") {}
                SettingsItem(Icons.Default.Apps, "App Icon") {}
                SettingsItem(Icons.Default.Work, "Productivity") {}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDarkMode = !isDarkMode }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Brightness6, contentDescription = "Mode")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Change Mode", fontSize = 16.sp, modifier = Modifier.weight(1f))
                    Switch(checked = isDarkMode, onCheckedChange = { isDarkMode = it })
                }
                Divider()
                SettingsItem(Icons.Default.PrivacyTip, "Privacy Policy") {}
                SettingsItem(Icons.Default.Help, "Help Center") {}
                SettingsItem(Icons.Default.ExitToApp, "Log Out") {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login")
                }
            }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
    }
    Divider()
}
