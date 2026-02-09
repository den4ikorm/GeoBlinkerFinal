package org.example.geoblinker.presentation.features.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Экран детального ответа на вопрос из FAQ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequentQuestScreen(
    questionId: String,
    onNavigateBack: () -> Unit
) {
    val questionData = remember { getQuestionData(questionId) }
    var isHelpful by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ") },
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
            // Категория
            Text(
                text = questionData.category.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // Вопрос
            Text(
                text = questionData.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Divider()

            // Ответ
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    questionData.answerParagraphs.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            // Дополнительные шаги (если есть)
            if (questionData.steps.isNotEmpty()) {
                Text(
                    text = "Пошаговая инструкция:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        questionData.steps.forEachIndexed { index, step ->
                            StepItem(
                                number = index + 1,
                                text = step
                            )
                        }
                    }
                }
            }

            // Связанные вопросы
            if (questionData.relatedQuestions.isNotEmpty()) {
                Text(
                    text = "Связанные вопросы:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                questionData.relatedQuestions.forEach { related ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = related,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Блок "Был ли ответ полезен?"
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Был ли ответ полезен?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (isHelpful == null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isHelpful = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("👍 Да")
                            }

                            OutlinedButton(
                                onClick = { isHelpful = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("👎 Нет")
                            }
                        }
                    } else {
                        Text(
                            text = if (isHelpful == true) {
                                "Спасибо за ваш отзыв! 😊"
                            } else {
                                "Спасибо! Мы постараемся улучшить этот ответ."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Контакт поддержки
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Не нашли ответ?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Свяжитесь с нашей службой поддержки:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "support@geoblinker.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StepItem(
    number: Int,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

// Модель данных вопроса
private data class QuestionData(
    val category: String,
    val question: String,
    val answerParagraphs: List<String>,
    val steps: List<String> = emptyList(),
    val relatedQuestions: List<String> = emptyList()
)

// Получение данных вопроса по ID
private fun getQuestionData(questionId: String): QuestionData {
    return when (questionId) {
        "getting_started_1" -> QuestionData(
            category = "Начало работы",
            question = "Как начать использовать GeoBlinker?",
            answerParagraphs = listOf(
                "GeoBlinker — это простое и удобное приложение для отслеживания GPS-трекеров. Чтобы начать использовать приложение, выполните несколько простых шагов.",
                "Прежде всего, вам понадобится GPS-трекер, совместимый с нашей системой. Список поддерживаемых устройств можно найти в разделе FAQ."
            ),
            steps = listOf(
                "Зарегистрируйтесь в приложении, используя email или номер телефона",
                "Подтвердите свой аккаунт через код, отправленный на email/SMS",
                "Перейдите в раздел 'Устройства' и нажмите 'Добавить устройство'",
                "Введите IMEI вашего GPS-трекера (находится на корпусе устройства)",
                "Включите GPS-трекер и дождитесь его появления на карте"
            ),
            relatedQuestions = listOf(
                "Как привязать первое устройство?",
                "Какие устройства поддерживаются?"
            )
        )
        
        "devices_1" -> QuestionData(
            category = "Устройства",
            question = "Сколько устройств можно подключить?",
            answerParagraphs = listOf(
                "Количество устройств, которые вы можете подключить, зависит от вашего тарифного плана:",
                "• Бесплатный план: до 1 устройства\n• Базовый план: до 3 устройств\n• Премиум план: до 10 устройств\n• Корпоративный план: неограниченно",
                "Вы можете в любой момент обновить свой тарифный план для подключения большего количества устройств."
            ),
            relatedQuestions = listOf(
                "Какие есть тарифные планы?",
                "Как привязать первое устройство?"
            )
        )
        
        "subscription_1" -> QuestionData(
            category = "Подписка и оплата",
            question = "Какие есть тарифные планы?",
            answerParagraphs = listOf(
                "GeoBlinker предлагает гибкую систему тарифов для разных потребностей:",
                "БЕСПЛАТНЫЙ ПЛАН:\n• 1 устройство\n• Обновление позиции каждые 30 секунд\n• 7 дней истории\n• Базовые уведомления",
                "БАЗОВЫЙ ПЛАН (299₽/мес):\n• До 3 устройств\n• Обновление каждые 10 секунд\n• 30 дней истории\n• Все типы уведомлений\n• Геозоны",
                "ПРЕМИУМ ПЛАН (599₽/мес):\n• До 10 устройств\n• Обновление каждые 5 секунд\n• Неограниченная история\n• Приоритетная поддержка\n• Экспорт данных\n• Расширенная аналитика"
            ),
            relatedQuestions = listOf(
                "Как отменить подписку?",
                "Какие способы оплаты доступны?"
            )
        )
        
        else -> QuestionData(
            category = "Общие",
            question = "Информация не найдена",
            answerParagraphs = listOf(
                "К сожалению, мы не смогли найти информацию по этому вопросу.",
                "Пожалуйста, свяжитесь с нашей службой поддержки для получения помощи."
            )
        )
    }
}
