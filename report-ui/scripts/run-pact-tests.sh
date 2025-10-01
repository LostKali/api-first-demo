#!/bin/bash

# Скрипт для запуска полного цикла Pact тестирования
# 1. Запускает Pact Consumer тесты
# 2. Генерирует контракты
# 3. Публикует контракты в Pact Broker

set -e

echo "🚀 Запуск Pact Consumer тестов..."
echo "=================================="

# Проверяем, что Pact Broker запущен
echo "📡 Проверка доступности Pact Broker..."
if ! curl -s http://localhost:9292 > /dev/null; then
    echo "❌ Pact Broker недоступен на localhost:9292"
    echo "💡 Запустите Pact Broker: docker-compose up pact-broker pact-db -d"
    exit 1
fi

echo "✅ Pact Broker доступен"

# Создаем директории для логов и контрактов
mkdir -p logs pacts

# Очищаем артефакты перед тестированием
echo "🧹 Очистка артефактов Pact..."
npm run pact:clean

# Генерируем Pact контракты
echo "🧪 Генерация Pact контрактов..."
npm run test:pact

# Проверяем, что контракты были созданы
if [ -f "pacts/ReportUI-AdminService.json" ]; then
    echo "✅ Контракты сгенерированы"
    
    # Публикуем контракты в Pact Broker
    echo "📤 Публикация контрактов в Pact Broker..."
    node scripts/publish-pact.js
    
    if [ $? -eq 0 ]; then
        echo "✅ Контракты успешно опубликованы"
        echo "🌐 Проверьте результаты в Pact Broker: http://localhost:9292"
        echo "💡 Username: pact, Password: password"
    else
        echo "❌ Ошибка при публикации контрактов"
        exit 1
    fi
else
    echo "❌ Контракты не были созданы"
    exit 1
fi

echo ""
echo "🎉 Pact Consumer тестирование завершено успешно!"
echo "================================================"
