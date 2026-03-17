package com.dubaiza.clean.demo.feature.employee.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dubaiza.clean.demo.domain.model.Employee
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeViewModel2
import com.dubaiza.clean.demo.feature.employee2.EmployeeStatus


@Preview(showBackground = true)
@Composable
fun EditEmployeeScreenPreview() {
    val sampleEmployee = Employee(
        id = 1,
        name = "John Doe",
        email = "john.doe@company.ae",
        phone = "+971 50 123 4567",
        designation = "Developer",
        department = "IT",
//        joinDate = "01/15/2023",
        status = EmployeeStatus.ACTIVE
    )

    /*EditEmployeeScreen(
        onSave = { *//* Do nothing for preview *//* },
        onBack = { *//* Do nothing for preview *//* }
    )*/
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmployeeScreen(
    viewModel: EmployeeViewModel2,
    onSave: (Employee) -> Unit,
    onBack: () -> Unit
) {


    val employee by viewModel.selectedEmployee.collectAsState()

    if (employee == null) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }


    var name by remember { mutableStateOf(employee!!.name) }
    var email by remember { mutableStateOf(employee!!.email) }
    var phone by remember { mutableStateOf(employee!!.phone) }
    var role by remember { mutableStateOf(employee!!.designation ?: "") }
    var department by remember { mutableStateOf(employee!!.department ?: "") }
//    var joinDate by remember { mutableStateOf(employee.joinDate ?: "") }
    var status by remember { mutableStateOf(employee!!.status ?: EmployeeStatus.ACTIVE) }
    var employeePhoto by remember { mutableStateOf<Int?>(null) } // replace with your image type

    var showRoleDropdown by remember { mutableStateOf(false) }
    var showDeptDropdown by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val statuses = listOf(EmployeeStatus.ALL, EmployeeStatus.ACTIVE, EmployeeStatus.ON_LEAVE, EmployeeStatus.REMOTE)
    val roles = listOf("Manager", "Developer", "Designer", "HR")
    val departments = listOf("IT", "Design", "Finance", "Marketing")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Employee", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                        onClick = {
                            onSave(
                                employee!!.copy(
                                    name = name,
                                    email = email,
                                    phone = phone,
                                    designation = role,
                                    department = department,
//                                    joinDate = joinDate,
                                    status = status
                                )
                            )
                        }
                    ) {
                        Text("Save", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
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

            // Employee Photo
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = employeePhoto?.let { painterResource(id = it) }
                        ?: painterResource(id = android.R.drawable.ic_menu_camera),
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
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

           /* // Role & Department Dropdowns
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = showRoleDropdown,
                    onExpandedChange = { showRoleDropdown = !showRoleDropdown },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Role") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        roles.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r) },
                                onClick = {
                                    role = r
                                    showRoleDropdown = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = showDeptDropdown,
                    onExpandedChange = { showDeptDropdown = !showDeptDropdown },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Department") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = showDeptDropdown,
                        onDismissRequest = { showDeptDropdown = false }
                    ) {
                        departments.forEach { d ->
                            DropdownMenuItem(
                                text = { Text(d) },
                                onClick = {
                                    department = d
                                    showDeptDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Join Date
            OutlinedTextField(
                value = "joinDate",
                onValueChange = {},
                readOnly = true,
                label = { Text("Join Date") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { *//* show date picker *//* }
            )*/

            // Status Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                statuses.forEach { s ->
                    FilterChip(
                        selected = status == s,
                        onClick = { status = s },
                        label = { Text(s.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}