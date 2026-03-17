package com.dubaiza.clean.demo.feature.employee.presentation

  import android.content.Intent
  import com.dubaiza.clean.demo.domain.model.Employee
  import androidx.compose.foundation.layout.*
  import androidx.compose.material.icons.Icons
  import androidx.compose.material.icons.filled.Add
  import androidx.compose.material.icons.filled.Delete
  import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
  import androidx.compose.ui.platform.LocalContext
  import androidx.compose.ui.unit.dp
  import com.dubaiza.clean.demo.feature.employee.dialogs.AddEmployeeDialog
  import com.dubaiza.clean.demo.core.ui.components.EmployeeTopBar
  import com.dubaiza.clean.demo.feature.employee.presentation.screens.EmptyScreen
  import com.dubaiza.clean.demo.feature.employee.presentation.screens.ErrorScreen
  import com.dubaiza.clean.demo.feature.employee.presentation.screens.LoadingScreen
  import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(
    viewModel: EmployeeViewModel2,
//    viewModel: EmployeeViewModel = viewModel(),
    onAddEmployee: () -> Unit,
    onEditEmployee: (Employee) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
//    val scaffoldState = rememberScaffoldState()
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            EmployeeTopBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                isSearching = isSearching,
                onToggleSearch = { isSearching = !isSearching },
                onRefresh = { /* reload logic */ }
            )

            /*TopAppBar(
                title = { Text(text = "Employees Information") }
            )*/
        },
        floatingActionButton = {

            /*FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Employee")
            }*/

            FloatingActionButton(onClick = onAddEmployee) {
                Icon(Icons.Default.Add, contentDescription = "Add Employee")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                when (uiState) {
                    is EmployeeUiState.Loading -> LoadingScreen()
                    is EmployeeUiState.Empty -> EmptyScreen()
                    is EmployeeUiState.Error -> ErrorScreen((uiState as EmployeeUiState.Error).message)
                    is EmployeeUiState.SuccessList -> {
//                        val employees = (uiState as EmployeeUiState.Success).employees

//                        EmployeeList((uiState as EmployeeUiState.Success).employees)
                        val employees = (uiState as EmployeeUiState.SuccessList).employees

                        val filteredEmployees = if (searchQuery.isEmpty()) {
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
                        )

                        /*EmployeeList(employees = employees, onDelete = { employee ->
                            viewModel.deleteEmployee(employee)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("${employee.name} deleted")
                            }
                        })*/

                    }
                    is EmployeeUiState.SuccessSingle -> LoadingScreen()
                }


                if (showAddDialog) {
                    AddEmployeeDialog(
                        onDismiss = { showAddDialog = false },
                        onAdd = { name, designation, department, status, phone, email ->
                            val employee = Employee(
                                name = name,
                                designation = designation,
                                department = department,
                                status = status,
                               phone = phone,
                               email = email
                            )
                            viewModel.addEmployee(employee)
                            showAddDialog = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("${employee.name} added")
                            }
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun EmployeeItemX(employee: Employee, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Name: ${employee.name}", style = MaterialTheme.typography.bodyLarge)
            Text("Designation: ${employee.designation}", style = MaterialTheme.typography.bodyMedium)
            Text("Department: ${employee.department}", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Employee")
        }
    }
}

@Composable
fun EmployeeItem(employee: Employee) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = employee.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${employee.designation} | ${employee.department}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
