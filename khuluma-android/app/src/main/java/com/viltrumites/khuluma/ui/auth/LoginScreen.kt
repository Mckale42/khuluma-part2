package com.viltrumites.khuluma.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.viltrumites.khuluma.ServiceLocator
import com.viltrumites.khuluma.ui.components.BrandButton
import com.viltrumites.khuluma.ui.components.Mascot
import com.viltrumites.khuluma.ui.theme.Brand
import com.viltrumites.khuluma.util.UiState

@Composable
fun LoginScreen(onSignedIn: () -> Unit, vm: LoginViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { if (vm.alreadySignedIn) onSignedIn() }
    LaunchedEffect(state) { if (state is UiState.Success) onSignedIn() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            val token = account.idToken
            if (token != null) vm.signIn(token) else vm.fail("No ID token returned.")
        } catch (e: ApiException) {
            vm.fail("Google sign-in cancelled or failed (${e.statusCode}).")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Brand.headerGradient).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Mascot(size = 96.dp)
        Spacer(Modifier.height(16.dp))
        Text("Khuluma", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            "Learn to speak South African languages",
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center, fontSize = 15.sp
        )
        Spacer(Modifier.height(48.dp))
        if (state is UiState.Loading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            BrandButton(
                text = "Continue with Google",
                onClick = { launcher.launch(ServiceLocator.auth.signInIntent()) },
                modifier = Modifier.fillMaxWidth()
            )
            // Debug-only shortcut so the app can be tested against a demo API
            // without a live Firebase project. Not shown in release builds.
            if (com.viltrumites.khuluma.BuildConfig.DEBUG) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Skip for now (demo)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onSignedIn() }
                        .padding(8.dp)
                )
            }
        }
        if (state is UiState.Error) {
            Spacer(Modifier.height(16.dp))
            Text((state as UiState.Error).message, color = Color.White, textAlign = TextAlign.Center)
        }
    }
}
