package org.example.geoblinker.presentation.features.other

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Экран создания нового обращения в техподдержку
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeRequestScreen(
    onNavigateBack: () -> Unit,
    onRequestSent: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.NORMAL) }
    var isLoading by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    
    val categories = listOf(
        "Технические проблемы",
        "Вопросы по тарифам",
        "Проблемы с оплатой",
        "Управление устройствами",
        "Настройки уведомлений",
        "Работа с картой",
        "Другое"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новое обращение") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Информационная карточка
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Мы здесь, чтобы помочь!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Среднее время ответа: 2 часа",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Категория
            Text(
                text = "Категория обращения *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите категорию") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }

            // Тема
            Text(
                text = "Тема обращения *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Краткое описание проблемы") },
                placeholder = { Text("Например: Трекер не подключается") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text("${subject.length}/100")
                }
            )

            // Сообщение
            Text(
                text = "Описание проблемы *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Подробное описание") },
                placeholder = { Text("Опишите проблему максимально подробно...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                supportingText = {
                    Text("${message.length}/1000")
                }
            )

            // Приоритет
            Text(
                text = "Приоритет",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityOption(
                    priority = Priority.LOW,
                    label = "Низкий",
                    description = "Общий вопрос, не срочно",
                    isSelected = priority == Priority.LOW,
                    onClick = { priority = Priority.LOW }
                )

                PriorityOption(
                    priority = Priority.NORMAL,
                    label = "Обычный",
                    description = "Стандартный вопрос",
                    isSelected = priority == Priority.NORMAL,
                    onClick = { priority = Priority.NORMAL }
                )

                PriorityOption(
                    priority = Priority.HIGH,
                    label = "Высокий",
                    description = "Важная проблема, требует внимания",
                    isSelected = priority == Priority.HIGH,
                    onClick = { priority = Priority.HIGH }
                )

                PriorityOption(
                    priority = Priority.URGENT,
                    label = "Срочный",
                    description = "Критическая проблема, нужна немедленная помощь",
                    isSelected = priority == Priority.URGENT,
                    onClick = { priority = Priority.URGENT }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Прикрепить файлы (placeholder)
            OutlinedButton(
                onClick = { /* Attach files */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Прикрепить файлы (скриншоты, логи)")
            }

            // Информация
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "💡 Советы:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Опишите проблему максимально подробно",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Приложите скриншоты, если возможно",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Укажите модель устройства и версию приложения",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка отправки
            Button(
                onClick = {
                    isLoading = true
                    // Имитация отправки
                    onRequestSent("new_request_id")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCategory.isNotBlank() && 
                         subject.isNotBlank() && 
                         message.isNotBlank() &&
                         !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Отправка...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Отправить обращение")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PriorityOption(
    priority: Priority,
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                when (priority) {
                    Priority.LOW -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    Priority.NORMAL -> MaterialTheme.colorScheme.primaryContainer
                    Priority.HIGH -> Color(0xFFFF9800).copy(alpha = 0.1f)
                    Priority.URGENT -> Color(0xFFF44336).copy(alpha = 0.1f)
                }
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(
                    when (priority) {
                        Priority.LOW -> Color(0xFF4CAF50)
                        Priority.NORMAL -> MaterialTheme.colorScheme.primary
                        Priority.HIGH -> Color(0xFFFF9800)
                        Priority.URGENT -> Color(0xFFF44336)
                    }
                )
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private enum class Priority {
    LOW, NORMAL, HIGH, URGENT
}
