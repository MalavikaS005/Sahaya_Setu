package com.sid.civilq_1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ✅ Move initial messages outside composable to prevent recreation
private val INITIAL_MESSAGES = listOf(
    ChatMessage(
        text = "Hi there! I'm your Civic Assistant. I can help you with:",
        isUser = false,
        timestamp = System.currentTimeMillis() - 1000
    ),
    ChatMessage(
        text = "• Report civic issues (potholes, streetlights, garbage)\n• Check status of your reports\n• Get information about city services\n• Find emergency contacts\n\nHow can I help you today?",
        isUser = false,
        timestamp = System.currentTimeMillis()
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {
    var messages by remember { mutableStateOf(INITIAL_MESSAGES) }
    var currentMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 👇 Auto-scroll to bottom when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatAppBar(onBackClick = remember { { navController.popBackStack() } })
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Bottom, // 🔥 Changed to Bottom alignment
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = messages,
                    key = { it.timestamp }
                ) { message ->
                    MessageBubble(message = message)
                    Spacer(modifier = Modifier.height(12.dp)) // Add spacing between messages
                }
            }

            ChatInputSection(
                currentMessage = currentMessage,
                onMessageChange = { currentMessage = it },
                onSendClick = remember {
                    {
                        if (currentMessage.isNotBlank()) {
                            val newMessage = ChatMessage(
                                text = currentMessage,
                                isUser = true,
                                timestamp = System.currentTimeMillis()
                            )
                            messages = messages + newMessage

                            val userQuery = currentMessage
                            currentMessage = ""

                            scope.launch {
                                delay(800)
                                val botResponse = ChatMessage(
                                    text = generateCivicBotResponse(userQuery),
                                    isUser = false,
                                    timestamp = System.currentTimeMillis()
                                )
                                messages = messages + botResponse
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF6200EE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // ✅ Cache the icon to prevent reload
                    Icon(
                        painter = painterResource(id = com.sid.civilq_1.R.drawable.bot),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Civic Assistant",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Online",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
private fun ChatInputSection(
    currentMessage: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = currentMessage,
            onValueChange = onMessageChange,
            placeholder = {
                Text(
                    "Ask about civic issues, report ...",
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = onSendClick,
            modifier = Modifier
                .size(48.dp)
                .offset(y = (-4).dp),
            containerColor = Color(0xFF6200EE),
            shape = RoundedCornerShape(24.dp),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    // ✅ Bot messages on left, user messages on right
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start // 🔥 Back to conditional arrangement
    ) {
        // ✅ Bot avatar (only for bot messages)
        if (!message.isUser) {
            BotAvatar()
            Spacer(modifier = Modifier.width(8.dp))
        }

        // ✅ Message card with optimized styling
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) Color(0xFF6200EE) else Color.White
            ),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (message.isUser) 20.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 20.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = if (message.isUser) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ✅ User avatar (only for user messages)
        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            UserAvatar()
        }
    }
}

// ✅ Extract avatars to separate composables to reduce nesting
@Composable
private fun BotAvatar() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(Color(0xFF6200EE), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = com.sid.civilq_1.R.drawable.bot),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun UserAvatar() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(Color(0xFFE3F2FD), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(16.dp)
        )
    }
}

// ✅ Data class remains the same
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

// ✅ Optimized response generation - consider memoizing this too
fun generateCivicBotResponse(userQuery: String): String {
    return when {
        userQuery.contains("report", ignoreCase = true) && userQuery.contains("pothole", ignoreCase = true) -> {
            "I'll help you report a pothole! Please provide:\n\n1️⃣ Exact location (street name & nearest landmark)\n2️⃣ Size (small/medium/large)\n3️⃣ Any safety concerns\n\nYou can also take a photo when filing the report. Would you like me to guide you to the report section?"
        }
        userQuery.contains("garbage", ignoreCase = true) -> {
            "For garbage collection issues, I need:\n\n🗑️ Your area/ward number\n📅 How many days uncollected\n♻️ Type of waste (household/commercial)\n\nThis helps prioritize the complaint. Shall I direct you to file a report?"
        }
        userQuery.contains("streetlight", ignoreCase = true) || userQuery.contains("light", ignoreCase = true) -> {
            "Reporting a streetlight issue! Please note:\n\n💡 Exact pole number (if visible)\n📍 Location details\n⚡ Issue type: not working/flickering/damaged\n\nStreetlight issues are usually resolved within 48-72 hours. Ready to file a report?"
        }
        userQuery.contains("status", ignoreCase = true) -> {
            "To check your report status, I need your report ID or you can describe the issue you reported. You can also check the 'Active' and 'Solved' sections on the home screen to see all reports in your area."
        }
        userQuery.contains("emergency", ignoreCase = true) -> {
            "For emergencies, use the red 'Emergency Contacts' button on the home screen. This will immediately connect you to emergency services (112). For non-emergency civic issues, I can help you file a proper report."
        }
        userQuery.contains("hello", ignoreCase = true) || userQuery.contains("hi", ignoreCase = true) -> {
            "Hello! I'm here to help you with civic issues. Whether you want to report a problem, check on existing reports, or get information about city services - just let me know what you need!"
        }
        else -> {
            "I specialize in helping with civic issues like:\n\n• Potholes and road problems\n• Street lighting issues\n• Garbage collection\n• Water supply problems\n• Public facilities\n\nWhat specific civic issue can I help you with today?"
        }
    }
}
