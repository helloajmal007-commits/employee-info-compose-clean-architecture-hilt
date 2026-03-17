package com.dubaiza.clean.demo.feature.employee.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dubaiza.clean.demo.domain.model.Employee
import com.dubaiza.clean.demo.feature.employee2.EmployeeStatus
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen2(
    onSave: (Employee) -> Unit,
    onBack: () -> Unit
) {



    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var joinDate by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(EmployeeStatus.ACTIVE) }
    var showStatusSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var employeePhoto by remember { mutableStateOf<Int?>(null) } // replace with your image type

    val scrollState = rememberScrollState()
    val statuses = listOf(EmployeeStatus.ALL, EmployeeStatus.ACTIVE, EmployeeStatus.ON_LEAVE, EmployeeStatus.REMOTE)

    val roles = listOf("Manager", "Developer", "Designer", "HR")
    val departments = listOf("IT", "Design", "Finance", "Marketing")

    val context = LocalContext.current

    // Date Picker
    /*val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(

        { _, year, month, dayOfMonth ->
            joinDate = "${month + 1}/$dayOfMonth/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )*/

    var showRoleDropdown by remember { mutableStateOf(false) }
    var showDeptDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Employee", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                    onClick = {
                        onSave(
                            Employee(
                                name = name,
                                email = email,
                                phone = phone,
                                designation = role,
                                department = department,
                                status = status
                            )
                        )
                    }
                    ) {
                        Text("Add", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Employee Photo Upload
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = employeePhoto?.let { painterResource(id = it) } ?: painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Employee Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F3F3)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { /* handle image selection */ },
                    modifier = Modifier
                        .size(28.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Photo", tint = Color.White)
                }
            }
            Text("Upload employee photo", fontSize = 12.sp, color = Color.Gray)

            // Full Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Johnathan Doe") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("john.doe@company.ae") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("+971 50 000 0000") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Current Status Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                statuses.forEach { s ->
                    FilterChip(
                        selected = status == s,
                        onClick = { status = s },
                        label = { Text(s.name, fontSize = 10.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Add Notes / Documents
            OutlinedButton(
                onClick = { /* open notes/documents */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Notes or Documents")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Adding an employee will automatically send an invitation email to the provided address for account setup.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}