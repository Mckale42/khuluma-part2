package com.viltrumites.khuluma.ui.buddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.ui.components.BeadStripe
import com.viltrumites.khuluma.ui.components.Mascot
import com.viltrumites.khuluma.ui.components.TypingDots
import com.viltrumites.khuluma.ui.theme.Brand

@Composable
fun BuddyScreen(vm: BuddyViewModel = viewModel()) {
    val messages by vm.messages.collectAsState()
    val sending by vm.sending.collectAsState()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) { if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().background(Brand.headerGradient).padding(start = 14.dp, end = 14.dp, top = 44.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Mascot(size = 40.dp)
            Spacer(Modifier.width(8.dp))
            Text("Buddy — AI tutor", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        BeadStripe()
        LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
            items(messages) { msg -> Bubble(msg) }
            if (sending) item {
                Row(Modifier.padding(8.dp)) {
                    Surface(color = Brand.CardWhite, shape = RoundedCornerShape(14.dp)) {
                        Box(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) { TypingDots() }
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input, onValueChange = { input = it },
                modifier = Modifier.weight(1f), placeholder = { Text("Ask Buddy…") }, singleLine = true,
                shape = RoundedCornerShape(20.dp)
            )
            IconButton(onClick = { vm.send(input.trim()); input = "" }, enabled = input.isNotBlank() && !sending) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Brand.Purple)
            }
        }
    }
}

@Composable
private fun Bubble(msg: ChatMessage) {
    val align = if (msg.fromUser) Alignment.End else Alignment.Start
    Column(Modifier.fillMaxWidth(), horizontalAlignment = align) {
        Surface(
            color = if (msg.fromUser) Brand.SelectBg else Brand.CardWhite,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(0.85f)
        ) { Text(msg.text, Modifier.padding(12.dp), color = Brand.Ink) }
    }
}
