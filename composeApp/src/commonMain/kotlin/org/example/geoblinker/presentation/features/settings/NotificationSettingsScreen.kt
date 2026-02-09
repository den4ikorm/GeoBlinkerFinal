package org.example.geoblinker.presentation.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.example.geoblinker.presentation.viewmodels.NotificationViewModel

/**
 * NotificationSettingsScreen - Настройки уведомлений
 * 
 * Функционал:
 * - Настройка типов уведомлений (Push/Email)
 * - Включение/выключение событий:
 *   - Device offline
 *   - Low battery
 *   - Geofence
 *   - Speed alert
 * - Switch компоненты с описаниями
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = koinInject()
) {
    // Настройки уведомлений
    var pushEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(false) }
    var deviceOffline by remember { mutableStateOf(true) }
    var lowBattery by remember { mutableStateOf(true) }
    var geofence by remember { mutableStateOf(true) }
    var speedAlert by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Уведомления") },
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
            // Тип уведомлений
            Text(
                text = "Способ доставки",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            NotificationSwitchItem(
                title = "Push-уведомления",
                description = "Получать уведомления на устройство",
                checked = pushEnabled,
                onCheckedChange = { pushEnabled = it }
            )
            
            NotificationSwitchItem(
                title = "Email-уведомления",
                description = "Получать уведомления на почту",
                checked = emailEnabled,
                onCheckedChange = { emailEnabled = it }
            )
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // События
            Text(
                text = "Типы событий",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            NotificationSwitchItem(
                title = "Устройство офлайн",
                description = "Уведомлять при потере связи с трекером",
                checked = deviceOffline,
                onCheckedChange = { deviceOffline = it }
            )
            
            NotificationSwitchItem(
                title = "Низкий заряд",
                description = "Уведомлять при разряде батареи < 20%",
                checked = lowBattery,
                onCheckedChange = { lowBattery = it }
            )
            
            NotificationSwitchItem(
                title = "Геозоны",
                description = "Уведомлять о входе/выходе из геозон",
                checked = geofence,
                onCheckedChange = { geofence = it }
            )
            
            NotificationSwitchItem(
                title = "Превышение скорости",
                description = "Уведомлять при превышении лимита",
                checked = speedAlert,
                onCheckedChange = { speedAlert = it }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Кнопка сохранения
            Button(
                onClick = {
                    // TODO: Сохранить настройки через NotificationViewModel
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
private fun NotificationSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
