package com.dubaiza.clean.demo.feature.employee.component


import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dubaiza.clean.demo.domain.model.Employee
import com.dubaiza.clean.demo.feature.employee2.EmployeeStatus


@Composable
fun EmployeeItemModern(
    employee: Employee,
    onDelete: () -> Unit,
    onEditClick: () -> Unit,
    onShareClick: () -> Unit,
    onCallClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Optional: open details */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employee.name.firstOrNull()?.uppercase() ?: "E",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Employee info
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = employee.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = employee.designation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = employee.department,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


                // Action buttons on the right
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    IconButtonSmall(Icons.Default.Call, MaterialTheme.colorScheme.primary, onCallClick)
                    IconButtonSmall(Icons.Default.Edit, MaterialTheme.colorScheme.secondary, onEditClick)
                    IconButtonSmall(Icons.Default.Share, MaterialTheme.colorScheme.tertiary, onShareClick)
                    IconButtonSmall(Icons.Default.Delete, MaterialTheme.colorScheme.error, onDelete)
                }
            }


        }
    }
}

@Composable
fun IconButtonSmall(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring()
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
            isPressed = false
        },
        modifier = Modifier
            .size(36.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
    }
}


// Preview function
/*
@Preview(showBackground = true)
@Composable
fun EmployeeItemModernPreview() {
    MaterialTheme {
        EmployeeItemModern(
            employee = Employee(
                id = 1,
                name = "John Doe",
                designation = "Software Engineer",
                department = "Development",
//                phoneNumber = "+1234567890",
//                email = "johndoe@example.com"
            ),
            onDelete = {},
            onEditClick = {},
            onShareClick = {},
            onCallClick = {}
        )
    }
}*/


@Preview(showBackground = true)
@Composable
fun EmployeeItemModernListPreview() {
    MaterialTheme {
        Column {
            EmployeeItemModern(
                employee = Employee(1, "John Doe", "Software Engineer", "Development", EmployeeStatus.REMOTE, "+1234567890", "johndoe@example.com"),
                onDelete = {}, onEditClick = {}, onShareClick = {}, onCallClick = {}
            )
            EmployeeItemModern(
                employee = Employee(2, "Jane Smith", "UI/UX Designer", "Design", EmployeeStatus.ACTIVE,"+0987654321", "janesmith@example.com"),
                onDelete = {}, onEditClick = {}, onShareClick = {}, onCallClick = {}
            )
        }
    }
}