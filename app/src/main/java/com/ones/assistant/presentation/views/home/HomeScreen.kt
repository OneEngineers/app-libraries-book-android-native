package com.ones.assistant.presentation.views.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ones.assistant.R

class HomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HomeScreen(
                    onCreateAccountClick = {},
                    onLoginClick = {}
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0CFFA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "logo",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Welcome to Weare One",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7B1FA2)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(15.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Google Button
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp), // Set height here
                        shape = RoundedCornerShape(20)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Apple Button
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp), // Set height here
                        shape = RoundedCornerShape(20)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.apple),
                            contentDescription = "Apple",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with Apple",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login / Sign up Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = onLoginClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(
                                topStart = 50.dp,
                                bottomStart = 50.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA301E6)
                            )
                        ) {
                            Text(
                                text = "Login",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = onCreateAccountClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(
                                topEnd = 50.dp,
                                bottomEnd = 50.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA600EB)
                            )
                        ) {
                            Text(
                                text = "Sign up",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Divider(modifier = Modifier.weight(1f))

                        Text(
                            text = "  Start your journey with  ",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Divider(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            onCreateAccountClick = {},
            onLoginClick = {}
        )
    }
}