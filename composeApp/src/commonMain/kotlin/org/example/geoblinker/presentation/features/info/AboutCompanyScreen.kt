package org.example.geoblinker.presentation.features.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Экран О компании (About Company)
 * Информация о компании, миссия, команда
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutCompanyScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTeamMember: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О компании") },
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
            // Логотип компании
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GEOBLINKER",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Миссия
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Наша миссия",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Мы создаем инновационные решения для мониторинга и контроля местоположения, делая технологии GPS-отслеживания доступными и удобными для каждого.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // О компании
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "О нас",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "GeoBlinker основана в 2020 году группой энтузиастов из сферы IoT и мобильных технологий. За 4 года работы мы выросли в команду профессионалов, обслуживающую более 100,000 пользователей по всему миру.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                    
                    Divider()
                    
                    CompanyStatItem(
                        label = "Год основания",
                        value = "2020"
                    )
                    
                    CompanyStatItem(
                        label = "Активных пользователей",
                        value = "100,000+"
                    )
                    
                    CompanyStatItem(
                        label = "Отслеживаемых устройств",
                        value = "250,000+"
                    )
                    
                    CompanyStatItem(
                        label = "Стран присутствия",
                        value = "45"
                    )
                }
            }

            // Ценности
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Наши ценности",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    ValueItem(
                        title = "Безопасность",
                        description = "Защита данных наших клиентов — наш приоритет"
                    )
                    
                    ValueItem(
                        title = "Инновации",
                        description = "Постоянное развитие и внедрение новых технологий"
                    )
                    
                    ValueItem(
                        title = "Надежность",
                        description = "Стабильная работа сервиса 24/7"
                    )
                    
                    ValueItem(
                        title = "Поддержка",
                        description = "Всегда на связи с нашими пользователями"
                    )
                }
            }

            // Команда
            Text(
                text = "Наша команда",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TeamMemberCard(
                name = "Алексей Иванов",
                position = "CEO & Founder",
                onClick = { onNavigateToTeamMember("ceo") }
            )

            TeamMemberCard(
                name = "Мария Петрова",
                position = "CTO",
                onClick = { onNavigateToTeamMember("cto") }
            )

            TeamMemberCard(
                name = "Дмитрий Сидоров",
                position = "Head of Product",
                onClick = { onNavigateToTeamMember("product") }
            )

            TeamMemberCard(
                name = "Елена Козлова",
                position = "Head of Support",
                onClick = { onNavigateToTeamMember("support") }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CompanyStatItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ValueItem(
    title: String,
    description: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "• $title",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun TeamMemberCard(
    name: String,
    position: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = position,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Подробнее"
            )
        }
    }
}
