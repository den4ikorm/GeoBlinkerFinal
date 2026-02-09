package org.example.geoblinker.presentation.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel

/**
 * PhoneSettingsScreen - Изменение номера телефона
 * 
 * Функционал:
 * - Ввод нового номера телефона
 * - Валидация (минимум 10 цифр)
 * - Навигация к экрану подтверждения кода
 * - Loading state при отправке кода
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: (String) -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    var phoneNumber by remember { 
        mutableStateOf(state.profile?.phone ?: "") 
    }
    var isError by remember { mutableStateOf(false) }
    
    // Валидация номера телефона
    val isValid = remember(phoneNumber) {
        phoneNumber.length >= 10 && phoneNumber.all { it.isDigit() }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Номер телефона") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Изменение номера телефона",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "На новый номер будет отправлен код подтверждения",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it.filter { char -> char.isDigit() }
                    isError = false
                },
                label = { Text("Номер телефона") },
                placeholder = { Text("+7 (XXX) XXX-XX-XX") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("Номер должен содержать минимум 10 цифр")
                    } else {
                        Text("Введите номер без пробелов и символов")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Кнопка отправки кода
            Button(
                onClick = {
                    if (isValid) {
                        // TODO: Отправить код через API
                        onNavigateToConfirmation(phoneNumber)
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && phoneNumber.isNotEmpty()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Отправить код")
                }
            }
        }
    }
}
