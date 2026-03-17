package com.dubaiza.clean.demo.feature.employee2


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dubaiza.clean.demo.domain.model.Employee
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeUiState
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeViewModel2
import com.dubaiza.clean.demo.feature.employee.presentation.screens.EmptyScreen
import com.dubaiza.clean.demo.feature.employee.presentation.screens.ErrorScreen
import com.dubaiza.clean.demo.feature.employee.presentation.screens.LoadingScreen

// ============================
// Main Employee List Screen
// ============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    viewModel: EmployeeViewModel2,
    onAddEmployee: () -> Unit,
    onEditEmployee: (Employee) -> Unit,
    onDeleteEmployee: (Employee) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(EmployeeStatus.ALL) }
    var showDeleteDialog by remember { mutableStateOf<Employee?>(null) }

    val coroutineScope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { EmployeeTopBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEmployee) {
                Icon(Icons.Default.Add, contentDescription = "Add Employee")
            }
        },
        bottomBar = { EmployeeBottomNavigation(selectedTab = 0) } ,// Example default tab
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search employees...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            // Filters
            val filters = listOf(EmployeeStatus.ALL, EmployeeStatus.ACTIVE, EmployeeStatus.REMOTE, EmployeeStatus.ON_LEAVE)
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter.displayName) }
                    )
                }
                Icon(Icons.Default.Settings, contentDescription = "Filter Options", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is EmployeeUiState.SuccessSingle -> LoadingScreen()
                is EmployeeUiState.Loading -> LoadingScreen()
                is EmployeeUiState.Empty -> EmptyScreen()
                is EmployeeUiState.Error -> ErrorScreen((uiState as EmployeeUiState.Error).message)
                is EmployeeUiState.SuccessList -> {
//                        val employees = (uiState as EmployeeUiState.Success).employees

//                        EmployeeList((uiState as EmployeeUiState.Success).employees)
                    val employees = (uiState as EmployeeUiState.SuccessList).employees

                    // Employee List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filteredEmployees = employees
                            .filter { it.name.contains(searchQuery, ignoreCase = true) }
                            .filter { selectedFilter == EmployeeStatus.ALL || it.status == selectedFilter }

                        items(filteredEmployees) { employee ->
                            EmployeeCard(
                                employee = employee,
                                onEdit = { onEditEmployee(employee) },
                                onDelete = { showDeleteDialog = employee }
                            )
                        }
                    }

                   /* val filteredEmployees = if (searchQuery.isEmpty()) {
                        employees
                    } else {
                        employees.filter {
                            it.name.contains(searchQuery, true) ||
                                    it.designation.contains(searchQuery, true) ||
                                    it.department.contains(searchQuery, true)
                        }
                    }


                    EmployeeList(
                        employees = filteredEmployees,
                        onDelete = { employee ->
                            viewModel.deleteEmployee(employee)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("${employee.name} deleted")
                            }
                        },
                        onEditEmployee={employee ->
                            onEditEmployee.invoke(employee)
                        },
                        onShareClick = {employee ->
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out ${employee.name}, ${employee.designation}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        },
                        onCallClick = { employee ->
                            val intent = Intent(Intent.ACTION_DIAL).apply {
//                                    data = android.net.Uri.parse("tel:${employee.phoneNumber}")
                                data = android.net.Uri.parse("tel:${123456789}")
                            }
                            context.startActivity(intent)
                        }
                    )*/



                }
            }


        }

        // Delete Confirmation Dialog
        showDeleteDialog?.let { employee ->
            ConfirmDeleteDialog(
                employee = employee,
                onConfirm = {
                    onDeleteEmployee(employee)
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}

// ============================
// Top App Bar
// ============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeTopBar() {
    TopAppBar(
        title = { Text("Employees", style = MaterialTheme.typography.titleLarge) },
        navigationIcon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
        actions = {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", modifier = Modifier.padding(end = 12.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }
    )
}

// ============================
// Bottom Navigation
// ============================
@Composable
fun EmployeeBottomNavigation(selectedTab: Int) {
    val tabs = listOf("Dashboard", "Directory", "Schedule", "Settings")
    NavigationBar {
        tabs.forEachIndexed { index, label ->
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = label) },
                label = { Text(label) },
                selected = selectedTab == index,
                onClick = {}
            )
        }
    }
}

// ============================
// Employee Card
// ============================
@Composable
fun EmployeeCard(
    employee: Employee,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(employee.name, fontWeight = FontWeight.Bold)
                Text(employee.designation, color = Color.Gray)
                StatusBadge(employee.status)
            }

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF3B82F6)) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red) }
            }
        }
    }
}

// ============================
// Status Badge
// ============================
@Composable
fun StatusBadge(status: EmployeeStatus) {
    val color = when (status) {
        EmployeeStatus.ACTIVE -> Color(0xFFFFA500)
        EmployeeStatus.ON_LEAVE -> Color(0xFFFFA500)
        EmployeeStatus.REMOTE -> Color(0xFF3B82F6)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .padding(top = 2.dp)
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(status.displayName, color = color, style = MaterialTheme.typography.labelSmall)
    }
}

// ============================
// Delete Confirmation Dialog
// ============================
@Composable
fun ConfirmDeleteDialog(employee: Employee, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Delete Employee", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Are you sure you want to delete ${employee.name}?")
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) { Text("Delete", color = Color.Red) }
                }
            }
        }
    }
}

// ============================
// Models
// ============================
enum class EmployeeStatus(val displayName: String) { ALL("All"), ACTIVE("Active"), ON_LEAVE("On Leave"), REMOTE("Remote") }

