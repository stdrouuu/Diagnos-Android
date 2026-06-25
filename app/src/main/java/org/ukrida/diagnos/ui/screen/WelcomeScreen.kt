package org.ukrida.diagnos.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ukrida.diagnos.R

@Composable
fun WelcomeScreen(
    onNavigateLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)), // bg-gray-100 simulation
        contentAlignment = Alignment.Center
    ) {
        // Simulated Phone Container (w-full max-w-[412px] h-screen max-h-[892px] bg-white)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main content wrapper offset (-mt-20 simulation)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.offset(y = (-40).dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logofull),
                    contentDescription = "Diagnos Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtitle
                Text(
                    text = "Ketahui dan Temukan Diri Anda Sejak dini",
                    color = Color(0xFF3CA89C),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Button Masuk
                Button(
                    onClick = onNavigateLogin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF42B2A6),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                        .height(48.dp)
                        .shadow(0.2.dp, shape = RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "Masuk",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
