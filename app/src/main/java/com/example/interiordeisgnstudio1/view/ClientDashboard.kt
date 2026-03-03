package com.example.interiordeisgnstudio1.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.example.interiordeisgnstudio1.Model.*
import com.example.interiordeisgnstudio1.R
import com.example.interiordeisgnstudio1.viewmodel.ClientViewModel

// ── Brand Colors ────────────────────────────────────────────────────────────
val ClientBrownDeep      = Color(0xFF5C3D2E)
val ClientBrownMid       = Color(0xFF8B6351)
val ClientTerracotta     = Color(0xFFC1714F)
val ClientSand           = Color(0xFFF5EFE6)
val ClientWarmWhite      = Color(0xFFFAF8F5)
val ClientCardWhite      = Color.White
val ClientTextMuted      = Color(0xFF9B8878)
val ClientCharcoal       = Color(0xFF2C2C2C)

@Composable
fun ClientDashboard() {
    val navController = rememberNavController()
    val viewModel: ClientViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(viewModel) }
            composable("designs") { DesignsScreen(viewModel) }
            composable("timeline") { TimelineScreen(viewModel) }
            composable("profile") { ProfileScreen(navController, viewModel) }
            composable("edit_profile") { EditProfileScreen(navController, viewModel) }
            composable("payments") { PaymentHistoryScreen(viewModel) }
            composable("notifications") { NotificationScreen(viewModel) }
        }
    }
}

@Composable
fun NotificationScreen(viewModel: ClientViewModel) {
    val notifications by viewModel.notifications.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().background(ClientWarmWhite).padding(16.dp)) {
        Text("Notifications", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet.", color = ClientTextMuted)
            }
        } else {
            LazyColumn {
                items(notifications) { notification ->
                    NotificationItem(notification) {
                        viewModel.markNotificationAsRead(notification.id)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) ClientCardWhite else ClientSand.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(if (notification.isRead) 1.dp else 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (notification.isRead) Color.Transparent else ClientTerracotta)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(notification.title, fontWeight = FontWeight.Bold, color = ClientCharcoal)
                Text(notification.message, fontSize = 13.sp, color = ClientCharcoal.copy(alpha = 0.8f))
                Text(notification.timestamp, fontSize = 11.sp, color = ClientTextMuted)
            }
        }
    }
}

@Composable
fun PaymentHistoryScreen(viewModel: ClientViewModel) {
    val payments by viewModel.payments.collectAsState()
    var showPaymentDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(ClientWarmWhite).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Payment History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Button(
                onClick = { showPaymentDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = ClientBrownDeep),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Make Payment")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (payments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No payment history found.", color = ClientTextMuted)
            }
        } else {
            LazyColumn {
                items(payments) { payment ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = ClientCardWhite),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payment, null, tint = ClientBrownMid)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(payment.description, fontWeight = FontWeight.Bold)
                                Text(payment.date, fontSize = 12.sp, color = ClientTextMuted)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("$${payment.amount.toInt()}", fontWeight = FontWeight.Bold, color = ClientCharcoal)
                                Text(payment.status, fontSize = 11.sp, color = if (payment.status == "Success") Color(0xFF4CAF50) else ClientTerracotta)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPaymentDialog) {
        var amount by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Make a Payment") },
            text = {
                Column {
                    OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount ($)") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.processPayment(amount.toDoubleOrNull() ?: 0.0, desc) { success ->
                        if (success) Toast.makeText(context, "Payment Recorded!", Toast.LENGTH_SHORT).show()
                    }
                    showPaymentDialog = false
                }) { Text("Pay Now") }
            }
        )
    }
}

@Composable
fun EditProfileScreen(navController: NavHostController, viewModel: ClientViewModel) {
    val user by viewModel.user.collectAsState()
    var name by remember { mutableStateOf(user?.firstName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClientWarmWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ClientCharcoal)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val updatedUser = user?.copy(firstName = name, email = email) ?: UserModel(firstName  = name, email = email)
                viewModel.updateUser(updatedUser) { success ->
                    if (success) {
                        Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ClientBrownDeep),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Changes")
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomItem("home", "Home", Icons.Default.Home),
        BottomItem("designs", "Designs", Icons.Default.Palette),
        BottomItem("timeline", "Timeline", Icons.Default.Schedule),
        BottomItem("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar(containerColor = ClientCardWhite) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route || (item.route == "profile" && (currentRoute == "edit_profile" || currentRoute == "payments" || currentRoute == "notifications")),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, null) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ClientBrownDeep,
                    selectedTextColor = ClientBrownDeep,
                    indicatorColor = ClientSand
                )
            )
        }
    }
}

data class BottomItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun HomeScreen(viewModel: ClientViewModel) {
    val project by viewModel.project.collectAsState()
    var showContactDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClientWarmWhite)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(ClientBrownDeep, ClientBrownMid)))
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val user by viewModel.user.collectAsState()
                Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(ClientSand), contentAlignment = Alignment.Center) {
                    Text(user?.firstName?.take(2)?.uppercase() ?: "JD", color = ClientBrownDeep, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Welcome back,", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Text("${user?.firstName ?: "Client"} 👋", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = project?.projectName ?: "Luxury Apartment Project", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = ClientCharcoal)
            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = ClientCardWhite), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = ClientTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Project Progress", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val progressValue = project?.progress?.toFloat() ?: 0.72f
                    LinearProgressIndicator(progress = { progressValue }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = ClientBrownDeep, trackColor = ClientSand)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("${(progressValue * 100).toInt()}% Completed", fontSize = 14.sp, color = ClientCharcoal)
                        Text("Material Selection", fontSize = 14.sp, color = ClientBrownMid, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Financial Overview", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ClientCharcoal)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                MiniStatCard("Total", "$${(project?.totalBudget ?: 45000.0).toInt() / 1000}K", Icons.Default.Payments, Modifier.weight(1f))
                MiniStatCard("Paid", "$${(project?.paidAmount ?: 30000.0).toInt() / 1000}K", Icons.Default.CheckCircle, Modifier.weight(1f))
                MiniStatCard("Bal.", "$${(project?.remainingAmount ?: 15000.0).toInt() / 1000}K", Icons.Default.AccountBalanceWallet, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent Updates", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ClientCharcoal)
            Spacer(modifier = Modifier.height(12.dp))
            val updates = project?.recentUpdates ?: emptyList()
            if (updates.isEmpty()) {
                Text("No recent updates", fontSize = 14.sp, color = ClientTextMuted, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                updates.forEach { update -> UpdateItem(update.text, update.time) { showUpdateDialog = update.text } }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { showContactDialog = true }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = ClientBrownDeep)) {
                Icon(Icons.AutoMirrored.Filled.Chat, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Designer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showContactDialog) {
        AlertDialog(onDismissRequest = { showContactDialog = false }, title = { Text("Contact Designer") }, text = { Text("How would you like to contact the designer?") }, confirmButton = { Button(onClick = { showContactDialog = false }) { Text("Email") } }, dismissButton = { Button(onClick = { showContactDialog = false }) { Text("Call") } })
    }
    if (showUpdateDialog != null) {
        AlertDialog(onDismissRequest = { showUpdateDialog = null }, title = { Text("Update Details") }, text = { Text(showUpdateDialog ?: "") }, confirmButton = { Button(onClick = { showUpdateDialog = null }) { Text("OK") } })
    }
}

@Composable
fun MiniStatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Card(modifier = modifier.padding(horizontal = 4.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = ClientCardWhite), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, modifier = Modifier.size(20.dp), tint = ClientBrownMid)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ClientCharcoal)
            Text(label, fontSize = 11.sp, color = ClientTextMuted)
        }
    }
}

@Composable
fun UpdateItem(text: String, time: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = ClientCardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ClientTerracotta))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text, fontSize = 14.sp, color = ClientCharcoal, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(time, fontSize = 11.sp, color = ClientTextMuted)
            }
        }
    }
}

@Composable
fun DesignsScreen(viewModel: ClientViewModel) {
    val designs by viewModel.designs.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val context = LocalContext.current
    var showUploadDialog by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        showUploadDialog = uri
    }

    Column(modifier = Modifier.fillMaxSize().background(ClientWarmWhite).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Design Moodboards", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(Icons.Default.AddPhotoAlternate, "Upload", tint = ClientBrownDeep)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (isUploading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = ClientBrownDeep)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Uploading...", color = ClientBrownDeep)
                }
            }
        } else if (designs.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("No designs found.", color = ClientTextMuted)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { 
                    launcher.launch("image/*")
                }, colors = ButtonDefaults.buttonColors(containerColor = ClientBrownDeep)) {
                    Text("Upload Your First Design")
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(designs) { design ->
                    DesignGridItem(design)
                }
            }
        }
    }

    if (showUploadDialog != null) {
        var title by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showUploadDialog = null },
            title = { Text("Design Title") },
            text = {
                OutlinedTextField(
                    value = title, 
                    onValueChange = { title = it }, 
                    label = { Text("e.g. Modern Living Room") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    enabled = title.isNotBlank(),
                    onClick = {
                        showUploadDialog?.let { uri ->
                            viewModel.uploadDesign(title, uri) { success ->
                                if (success) {
                                    Toast.makeText(context, "Upload Success!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        showUploadDialog = null
                    }
                ) { Text("Upload") }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun DesignGridItem(design: Design) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(0.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ClientCardWhite),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(modifier = Modifier.weight(1f).fillMaxWidth().background(ClientSand)) {
                if (design.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = design.imageUrl,
                        contentDescription = design.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Map titles to local resources for visual quality
                    val imageRes = when {
                        design.title.contains("living", true) -> R.drawable.livingroom
                        design.title.contains("bed", true) -> R.drawable.bedroom
                        design.title.contains("dinn", true) -> R.drawable.dinning
                        else -> R.drawable.livingroom
                    }
                    Image(
                        painter = painterResource(id = imageRes), 
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(design.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Moodboard", fontSize = 11.sp, color = ClientTextMuted)
            }
        }
    }
}

@Composable
fun TimelineScreen(viewModel: ClientViewModel) {
    val timeline by viewModel.timeline.collectAsState()
    val context = LocalContext.current
    var showAddProductDialog by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(ClientWarmWhite).padding(16.dp)) {
        Text("Project Timeline", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (timeline.isEmpty()) {
            EmptyTimelinePlaceholder {
                viewModel.initializeDefaultTimeline()
                Toast.makeText(context, "Timeline Initialized!", Toast.LENGTH_SHORT).show()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(timeline) { phase ->
                    TimelineItem(
                        phase = phase,
                        onStatusChange = { newStatus ->
                            viewModel.updateTimelineStatus(phase.title, newStatus)
                            Toast.makeText(context, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()
                        },
                        onAddProductClick = { showAddProductDialog = phase.title }
                    )
                }
            }
        }
    }

    if (showAddProductDialog != null) {
        AddProductDialog(
            onDismiss = { showAddProductDialog = null },
            onAdd = { name, price ->
                viewModel.addProductToPhase(
                    showAddProductDialog!!, 
                    ProductModel(name = name, price = price.toDoubleOrNull() ?: 0.0)
                )
                Toast.makeText(context, "Added $name to ${showAddProductDialog}", Toast.LENGTH_SHORT).show()
                showAddProductDialog = null
            }
        )
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Product to Phase") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
            }
        },
        confirmButton = {
            Button(onClick = { onAdd(name, price) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EmptyTimelinePlaceholder(onInitialize: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
        colors = CardDefaults.cardColors(containerColor = ClientCardWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.AutoMirrored.Filled.EventNote, null, modifier = Modifier.size(48.dp), tint = ClientBrownMid)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No timeline found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Your project timeline is currently empty.", color = ClientTextMuted, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onInitialize,
                colors = ButtonDefaults.buttonColors(containerColor = ClientBrownDeep),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Initialize Default Timeline")
            }
        }
    }
}

@Composable
fun TimelineItem(
    phase: ProjectTimeline,
    onStatusChange: (String) -> Unit,
    onAddProductClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ClientCardWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val color = when(phase.status) {
                    "Completed" -> ClientBrownDeep
                    "In Progress" -> ClientTerracotta
                    else -> ClientTextMuted
                }
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(phase.title, fontWeight = FontWeight.Bold)
                    Text(phase.status, color = color, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusButton("Completed", ClientBrownDeep, Modifier.weight(1f)) { onStatusChange("Completed") }
                StatusButton("In Progress", ClientTerracotta, Modifier.weight(1f)) { onStatusChange("In Progress") }
                StatusButton("Pending", ClientTextMuted, Modifier.weight(1f)) { onStatusChange("Pending") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddProductClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ClientBrownMid),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Product", fontSize = 14.sp)
            }

            if (phase.products.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = ClientSand, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Associated Products:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ClientCharcoal)
                phase.products.forEach { product ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• ${product.name}", fontSize = 12.sp, color = ClientCharcoal)
                        Text("$${product.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ClientBrownMid)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusButton(text: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(32.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(text, fontSize = 10.sp, color = color)
    }
}

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ClientViewModel) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(ClientWarmWhite).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Brush.linearGradient(listOf(ClientBrownDeep, ClientBrownMid))), contentAlignment = Alignment.Center) {
            Text(user?.firstName?.take(2)?.uppercase() ?: "JD", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(user?.firstName ?: "John Doe", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(user?.email ?: "johndoe@email.com", color = ClientTextMuted)
        Spacer(modifier = Modifier.height(32.dp))
        ProfileOption("Edit Profile", Icons.Default.Edit) { navController.navigate("edit_profile") }
        ProfileOption("Notifications", Icons.Default.Notifications) { navController.navigate("notifications") }
        ProfileOption("Payment History", Icons.Default.History) { navController.navigate("payments") }
        ProfileOption("Help Center", Icons.AutoMirrored.Filled.HelpOutline) { }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { showLogoutDialog = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = ClientTerracotta), shape = RoundedCornerShape(12.dp)) { Text("Logout") }
    }

    if (showLogoutDialog) {
        AlertDialog(onDismissRequest = { showLogoutDialog = false }, title = { Text("Confirm Logout") }, text = { Text("Are you sure you want to log out?") }, confirmButton = { Button(onClick = { showLogoutDialog = false; context.startActivity(Intent(context, LoginActivity::class.java)) }) { Text("Logout") } }, dismissButton = { Button(onClick = { showLogoutDialog = false }) { Text("Cancel") } })
    }
}

@Composable
fun ProfileOption(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = ClientCardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ClientBrownMid)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = ClientTextMuted)
        }
    }
}
