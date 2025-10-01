#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

/**
 * Скрипт для очистки артефактов Pact между запусками тестов
 * Удаляет сгенерированные контракты и логи для обеспечения чистого состояния
 */

const directoriesToClean = [
  path.resolve(process.cwd(), 'pacts'),
  path.resolve(process.cwd(), 'logs')
];

const filesToClean = [
  'pacts/ReportUI-AdminService.json',
  'logs/pact.log'
];

console.log('🧹 Cleaning Pact artifacts...');

// Create directories if they don't exist
directoriesToClean.forEach(dir => {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
    console.log(`✅ Created directory: ${path.basename(dir)}`);
  }
});

// Remove contract files and logs
filesToClean.forEach(filePath => {
  const fullPath = path.resolve(process.cwd(), filePath);
  
  if (fs.existsSync(fullPath)) {
    try {
      fs.unlinkSync(fullPath);
      console.log(`🗑️  Удален файл: ${filePath}`);
    } catch (error) {
      console.warn(`⚠️  Не удалось удалить файл ${filePath}: ${error.message}`);
    }
  } else {
    console.log(`ℹ️  Файл не найден: ${filePath}`);
  }
});

console.log('✅ Очистка артефактов Pact завершена');
