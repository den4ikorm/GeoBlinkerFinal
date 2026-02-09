package org.example.geoblinker.presentation.features.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Экран детальной информации о члене команды
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutCompanyItemScreen(
    memberId: String,
    onNavigateBack: () -> Unit
) {
    val memberData = getMemberData(memberId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(memberData.name) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Фото/Аватар
            Surface(
                modifier = Modifier.size(150.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = memberData.initials,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = memberData.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = memberData.position,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Био
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
                        text = "О себе",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = memberData.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // Достижения
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Достижения",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    memberData.achievements.forEach { achievement ->
                        AchievementItem(achievement)
                    }
                }
            }

            // Контакты
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
                        text = "Контакты",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    ContactItem(
                        label = "Email:",
                        value = memberData.email
                    )
                    
                    if (memberData.linkedin.isNotBlank()) {
                        ContactItem(
                            label = "LinkedIn:",
                            value = memberData.linkedin
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AchievementItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ContactItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

// Данные членов команды
private data class TeamMemberData(
    val name: String,
    val position: String,
    val initials: String,
    val bio: String,
    val achievements: List<String>,
    val email: String,
    val linkedin: String = ""
)

private fun getMemberData(memberId: String): TeamMemberData {
    return when (memberId) {
        "ceo" -> TeamMemberData(
            name = "Алексей Иванов",
            position = "CEO & Founder",
            initials = "АИ",
            bio = "Основатель и генеральный директор GeoBlinker. Более 15 лет опыта в сфере IoT и телематики. Выпускник МГУ, MBA в INSEAD. До основания GeoBlinker работал ведущим архитектором в крупной телекоммуникационной компании.",
            achievements = listOf(
                "Запустил GeoBlinker с нуля до 100,000+ пользователей за 4 года",
                "Привлек $5M инвестиций от венчурных фондов",
                "Спикер на международных конференциях IoT и Smart City",
                "Держатель 3 патентов в области GPS-технологий"
            ),
            email = "a.ivanov@geoblinker.com",
            linkedin = "linkedin.com/in/aivanov"
        )
        "cto" -> TeamMemberData(
            name = "Мария Петрова",
            position = "CTO",
            initials = "МП",
            bio = "Технический директор GeoBlinker. Эксперт в области распределенных систем и мобильной разработки. 12 лет опыта в технологических компаниях. Выпускница МФТИ, PhD в Computer Science.",
            achievements = listOf(
                "Построила масштабируемую архитектуру для 250,000+ устройств",
                "Внедрила AI/ML алгоритмы для предсказания маршрутов",
                "Сократила время отклика системы на 70%",
                "Автор 15+ технических статей"
            ),
            email = "m.petrova@geoblinker.com",
            linkedin = "linkedin.com/in/mpetrova"
        )
        "product" -> TeamMemberData(
            name = "Дмитрий Сидоров",
            position = "Head of Product",
            initials = "ДС",
            bio = "Руководитель продуктового направления. Специалист по UX/UI и product management. 10 лет опыта в разработке B2C и B2B продуктов. Выпускник ВШЭ.",
            achievements = listOf(
                "Увеличил retention пользователей на 45%",
                "Запустил 20+ новых фич на основе user feedback",
                "Внедрил Agile-процессы в команду продукта",
                "Получил награду 'Product Manager of the Year 2023'"
            ),
            email = "d.sidorov@geoblinker.com",
            linkedin = "linkedin.com/in/dsidorov"
        )
        "support" -> TeamMemberData(
            name = "Елена Козлова",
            position = "Head of Support",
            initials = "ЕК",
            bio = "Руководитель службы поддержки пользователей. Эксперт в customer success и построении support-команд. 8 лет опыта в сервисных компаниях.",
            achievements = listOf(
                "Построила команду поддержки из 25 специалистов",
                "Достигла CSAT 4.8/5.0",
                "Сократила среднее время ответа до 2 минут",
                "Внедрила многоязычную поддержку (10 языков)"
            ),
            email = "e.kozlova@geoblinker.com",
            linkedin = "linkedin.com/in/ekozlova"
        )
        else -> TeamMemberData(
            name = "Неизвестный сотрудник",
            position = "",
            initials = "??",
            bio = "Информация не найдена",
            achievements = emptyList(),
            email = "info@geoblinker.com"
        )
    }
}
