#!/data/data/com.termux/files/usr/bin/bash

# GeoBlinker Auto-Setup Script
# Автоматическая настройка проекта для демо-сборки
# Версия: 1.0 (БЕЗ Google Maps API - только демо)

set -e  # Остановиться при ошибке

# Цвета
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Путь к проекту
PROJECT_PATH="/storage/emulated/0/Documents/МУСОР/0_CLAUDE/ГЕО0/geoBlinker/GeoBlinkerFinal"

echo -e "${GREEN}╔══════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   GeoBlinker Auto-Setup для ДЕМО v1.0       ║${NC}"
echo -e "${GREEN}║   (БЕЗ Google Maps - только для теста)      ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════╝${NC}"
echo ""

# Проверка что путь существует
if [ ! -d "$PROJECT_PATH" ]; then
    echo -e "${RED}❌ ОШИБКА: Проект не найден!${NC}"
    echo -e "${YELLOW}Путь: $PROJECT_PATH${NC}"
    echo -e "${YELLOW}Проверьте что проект распакован в эту папку${NC}"
    exit 1
fi

echo -e "${GREEN}✓${NC} Проект найден: $PROJECT_PATH"
echo ""

# Переход в проект
cd "$PROJECT_PATH"

# Проверка settings.gradle.kts
if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}❌ ОШИБКА: Это не корень проекта GeoBlinker${NC}"
    exit 1
fi

echo -e "${GREEN}✓${NC} Проект GeoBlinker подтвержден"
echo ""

# ========================================
# ШАГ 1: Создать local.properties
# ========================================
echo -e "${BLUE}[1/4]${NC} Создание local.properties..."

if [ -f "local.properties" ]; then
    echo -e "${YELLOW}⚠️  local.properties уже существует${NC}"
    echo -e "${YELLOW}   Создаю backup...${NC}"
    cp local.properties local.properties.backup
    echo -e "${GREEN}✓${NC} Backup создан: local.properties.backup"
fi

# Создаём файл с DEMO ключом
cat > local.properties << 'EOF'
# GeoBlinker - Local Properties
# ДЕМО версия - карта не будет работать, но app запустится

# Fake API Key для демо (карта будет серая, но app запустится)
MAPS_API_KEY=DEMO_KEY_NOT_REAL_ONLY_FOR_TESTING

# SDK Location (для Termux)
sdk.dir=/data/data/com.termux/files/usr
EOF

echo -e "${GREEN}✓${NC} local.properties создан с DEMO ключом"
echo ""

# ========================================
# ШАГ 2: Backup оригинальных файлов
# ========================================
echo -e "${BLUE}[2/4]${NC} Создание backup файлов..."

# Backup build.gradle.kts
if [ ! -f "composeApp/build.gradle.kts.backup" ]; then
    cp composeApp/build.gradle.kts composeApp/build.gradle.kts.backup
    echo -e "${GREEN}✓${NC} Backup: build.gradle.kts.backup"
else
    echo -e "${YELLOW}⚠️  build.gradle.kts.backup уже существует${NC}"
fi

# Backup AndroidManifest.xml
MANIFEST="composeApp/src/androidMain/AndroidManifest.xml"
if [ ! -f "$MANIFEST.backup" ]; then
    cp "$MANIFEST" "$MANIFEST.backup"
    echo -e "${GREEN}✓${NC} Backup: AndroidManifest.xml.backup"
else
    echo -e "${YELLOW}⚠️  AndroidManifest.xml.backup уже существует${NC}"
fi

echo ""

# ========================================
# ШАГ 3: Обновить AndroidManifest.xml
# ========================================
echo -e "${BLUE}[3/4]${NC} Обновление AndroidManifest.xml..."

# Заменяем YOUR_GOOGLE_MAPS_API_KEY_HERE на ${MAPS_API_KEY}
if grep -q '\${MAPS_API_KEY}' "$MANIFEST"; then
    echo -e "${GREEN}✓${NC} AndroidManifest.xml уже настроен"
else
    sed -i.tmp 's/YOUR_GOOGLE_MAPS_API_KEY_HERE/${MAPS_API_KEY}/g' "$MANIFEST"
    rm -f "$MANIFEST.tmp"
    echo -e "${GREEN}✓${NC} AndroidManifest.xml обновлен"
fi

echo ""

# ========================================
# ШАГ 4: Обновить build.gradle.kts
# ========================================
echo -e "${BLUE}[4/4]${NC} Обновление build.gradle.kts..."

# Проверяем есть ли уже код для API key
if grep -q "manifestPlaceholders\[\"MAPS_API_KEY\"\]" "composeApp/build.gradle.kts"; then
    echo -e "${GREEN}✓${NC} build.gradle.kts уже настроен"
else
    echo -e "${YELLOW}⚠️  Добавляю код для чтения API key...${NC}"
    
    # Создаём временный файл с обновлённым кодом
    # Ищем строку versionName = "1.0.0" и вставляем после неё код
    awk '
    /versionName = "1.0.0"/ {
        print $0
        print ""
        print "        // Читаем Google Maps API key из local.properties"
        print "        val properties = java.util.Properties()"
        print "        val localPropertiesFile = rootProject.file(\"local.properties\")"
        print "        if (localPropertiesFile.exists()) {"
        print "            localPropertiesFile.inputStream().use { properties.load(it) }"
        print "        }"
        print "        val mapsApiKey = properties.getProperty(\"MAPS_API_KEY\", \"\")"
        print ""
        print "        // Передаем в AndroidManifest через placeholder"
        print "        manifestPlaceholders[\"MAPS_API_KEY\"] = mapsApiKey"
        next
    }
    { print }
    ' composeApp/build.gradle.kts > composeApp/build.gradle.kts.tmp
    
    mv composeApp/build.gradle.kts.tmp composeApp/build.gradle.kts
    echo -e "${GREEN}✓${NC} build.gradle.kts обновлен"
fi

echo ""

# ========================================
# Финальная проверка
# ========================================
echo -e "${BLUE}[CHECK]${NC} Финальная проверка настроек..."

# Проверка 1: local.properties существует
if [ -f "local.properties" ]; then
    echo -e "${GREEN}✓${NC} local.properties существует"
else
    echo -e "${RED}❌${NC} local.properties отсутствует!"
fi

# Проверка 2: MAPS_API_KEY в local.properties
if grep -q "MAPS_API_KEY" "local.properties"; then
    echo -e "${GREEN}✓${NC} MAPS_API_KEY найден в local.properties"
else
    echo -e "${RED}❌${NC} MAPS_API_KEY отсутствует в local.properties!"
fi

# Проверка 3: AndroidManifest использует placeholder
if grep -q '\${MAPS_API_KEY}' "$MANIFEST"; then
    echo -e "${GREEN}✓${NC} AndroidManifest.xml использует placeholder"
else
    echo -e "${RED}❌${NC} AndroidManifest.xml НЕ использует placeholder!"
fi

# Проверка 4: build.gradle читает properties
if grep -q "manifestPlaceholders" "composeApp/build.gradle.kts"; then
    echo -e "${GREEN}✓${NC} build.gradle.kts настроен"
else
    echo -e "${RED}❌${NC} build.gradle.kts НЕ настроен!"
fi

echo ""
echo -e "${GREEN}╔══════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║          НАСТРОЙКА ЗАВЕРШЕНА! ✓              ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════╝${NC}"
echo ""

# ========================================
# Инструкции по сборке
# ========================================
echo -e "${YELLOW}╔══════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║           СЛЕДУЮЩИЕ ШАГИ                     ║${NC}"
echo -e "${YELLOW}╚══════════════════════════════════════════════╝${NC}"
echo ""

echo -e "${BLUE}1️⃣  Собрать Debug APK:${NC}"
echo "   cd $PROJECT_PATH"
echo "   ./gradlew clean"
echo "   ./gradlew :composeApp:assembleDebug"
echo ""

echo -e "${BLUE}2️⃣  Найти APK:${NC}"
echo "   Путь: composeApp/build/outputs/apk/debug/composeApp-debug.apk"
echo ""

echo -e "${BLUE}3️⃣  Скопировать в Download:${NC}"
echo "   cp composeApp/build/outputs/apk/debug/composeApp-debug.apk \\"
echo "      ~/storage/downloads/GeoBlinker-demo.apk"
echo ""

echo -e "${BLUE}4️⃣  Установить:${NC}"
echo "   File Manager → Downloads → GeoBlinker-demo.apk → Install"
echo ""

# ========================================
# Важные заметки
# ========================================
echo -e "${YELLOW}╔══════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║           ВАЖНЫЕ ЗАМЕТКИ                     ║${NC}"
echo -e "${YELLOW}╚══════════════════════════════════════════════╝${NC}"
echo ""

echo -e "${RED}⚠️  ДЕМО версия - карта НЕ будет работать!${NC}"
echo -e "   (API key = DEMO_KEY_NOT_REAL)"
echo ""
echo -e "${GREEN}✅ Что БУДЕТ работать:${NC}"
echo "   • App запускается"
echo "   • Все экраны (кроме карты)"
echo "   • Devices (mock данные)"
echo "   • Notifications"
echo "   • Profile"
echo "   • Смена языка"
echo "   • Смена темы"
echo ""
echo -e "${RED}❌ Что НЕ будет работать:${NC}"
echo "   • Map screen (серый экран)"
echo "   • Геолокация"
echo ""

echo -e "${BLUE}💡 Для ПОЛНОЙ версии:${NC}"
echo "   1. Получить настоящий Google Maps API key"
echo "   2. Заменить в local.properties:"
echo "      MAPS_API_KEY=AIzaSyВАШ_НАСТОЯЩИЙ_КЛЮЧ"
echo "   3. Пересобрать APK"
echo ""

echo -e "${GREEN}✨ Готово! Теперь можно собирать проект!${NC}"
echo ""

# Опция: сразу запустить сборку?
echo -e "${YELLOW}Хотите сразу начать сборку APK? (y/n)${NC}"
read -r response

if [[ "$response" =~ ^[Yy]$ ]]; then
    echo ""
    echo -e "${GREEN}🔨 Начинаю сборку...${NC}"
    echo ""
    
    # Очистка
    echo -e "${BLUE}Очистка проекта...${NC}"
    ./gradlew clean
    echo ""
    
    # Сборка
    echo -e "${BLUE}Сборка Debug APK (может занять 5-10 минут)...${NC}"
    ./gradlew :composeApp:assembleDebug
    
    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}╔══════════════════════════════════════════════╗${NC}"
        echo -e "${GREEN}║         СБОРКА УСПЕШНА! 🎉                   ║${NC}"
        echo -e "${GREEN}╚══════════════════════════════════════════════╝${NC}"
        echo ""
        
        # Копируем APK
        echo -e "${BLUE}Копирую APK в Downloads...${NC}"
        cp composeApp/build/outputs/apk/debug/composeApp-debug.apk \
           ~/storage/downloads/GeoBlinker-demo.apk
        
        echo -e "${GREEN}✓${NC} APK скопирован: ~/storage/downloads/GeoBlinker-demo.apk"
        echo ""
        echo -e "${GREEN}📱 Теперь установите APK через File Manager!${NC}"
        echo ""
    else
        echo ""
        echo -e "${RED}❌ Сборка не удалась!${NC}"
        echo -e "${YELLOW}Посмотрите ошибки выше и сообщите мне.${NC}"
        echo ""
    fi
else
    echo ""
    echo -e "${GREEN}OK! Запустите сборку вручную когда будете готовы:${NC}"
    echo "   cd $PROJECT_PATH"
    echo "   ./gradlew clean assembleDebug"
    echo ""
fi
