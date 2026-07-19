package com.example.hydroflow.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydroflow.ui.theme.HydrationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(
    viewModel: HydrationViewModel,
    modifier: Modifier = Modifier
) {
    // Collecter l'état du ViewModel de manière réactive à Compose
    val uiState by viewModel.uiState.collectAsState()

    // Animation fluide pour le cercle de progression
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progressPercentage,
        animationSpec = tween(durationMillis = 800),
        label = "ProgressAnimation"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HydroFlow",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Section Streak / Motivation
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Objectif Quotidien",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "${uiState.goalMl / 1000} Litres (2000 ml)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E5FF) // Accent Turquoise
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF00E5FF).copy(alpha = 0.15f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "🔥 ${uiState.streakDays} Jours",
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Cercle de Progression Central
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(240.dp)
                    .padding(12.dp)
            ) {
                // Canvas pour dessiner le cercle de progression Material 3 personnalisé
                val turquoiseColor = Color(0xFF00E5FF)
                val trackColor = MaterialTheme.colorScheme.surfaceVariant

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Cercle de fond (rail de progression)
                    drawCircle(
                        color = trackColor,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Arc de progression
                    drawArc(
                        color = turquoiseColor,
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Textes intérieurs
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "💧",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "${uiState.currentIntakeMl}",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "/ ${uiState.goalMl} ml",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(uiState.progressPercentage * 100).toInt()}%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = turquoiseColor
                    )
                }
            }

            // Message de félicitations
            if (uiState.isGoalReached) {
                Text(
                    "🎉 Objectif d'hydratation atteint !",
                    color = Color(0xFF00E5FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Boutons d'Action (Ajouter eau & Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Bouton Reset
                OutlinedButton(
                    onClick = { viewModel.resetIntake() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(
                            listOf(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.error)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Réinitialiser"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset", fontWeight = FontWeight.SemiBold)
                }

                // Bouton Ajouter 250ml
                Button(
                    onClick = { viewModel.addWater(250) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E5FF), // Accent Turquoise
                        contentColor = Color(0xFF121212)    // Thème sombre - texte contrasté
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Ajouter de l'eau"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("+250 ml", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Historique Récent
            Text(
                "Historique aujourd'hui",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            if (uiState.logs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aucune boisson enregistrée",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.logs.size) { index ->
                        val log = uiState.logs[index]
                        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val timeStr = formatter.format(log.timestamp)

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💧", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "Eau ajoutée",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            timeStr,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "+${log.amountMl} ml",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E5FF)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = { viewModel.deleteLog(log.id) }) {
                                        Text("❌", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}