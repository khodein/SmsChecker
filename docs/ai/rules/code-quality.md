# code-quality.md

## Purpose
Это правило описывает проверку качества Kotlin-кода через Detekt и auto-correct для форматирования

## Scope
Где применяется:
- All
- Правило распространяется на Kotlin-код, Gradle Kotlin DSL и общие проверки качества кода

## Principles
- Код должен проходить статическую проверку Detekt
- Автоматические исправления можно применять только для безопасных formatting-правил
- Auto-correct не заменяет ручную проверку архитектуры, поведения и читаемости кода
- После auto-correct нужно повторно запускать обычную проверку без auto-correct

## Rules
- Для проверки качества кода используй `./gradlew detekt`
- Для автоматического исправления форматирования используй `./gradlew detekt --auto-correct`
- После `./gradlew detekt --auto-correct` обязательно запусти `./gradlew detekt`
- Не применяй auto-correct, если в рабочей области есть чужие или несвязанные изменения, которые могут быть затронуты форматированием
- Не исправляй Detekt findings отключением правил без явной причины
- Не добавляй baseline без отдельного решения пользователя
- Если Detekt finding указывает на реальную проблему, исправляй код, а не подавляй правило
- Если правило дает ложное срабатывание, используй локальный `@Suppress` только рядом с кодом и с понятной причиной
- Не форматируй и не переписывай файлы, не относящиеся к текущей задаче
- Detekt конфигурация хранится в `config/detekt/detekt.yml`
- Подключение Detekt выполняется через convention plugin `smschecker.detekt`
- Версия Detekt и дополнительные rule sets должны храниться в `gradle/libs.versions.toml`

## Do
- Запускай `./gradlew detekt` перед завершением задачи, если изменения затрагивают Kotlin-код или Gradle Kotlin DSL
- Используй `./gradlew detekt --auto-correct`, если нужно привести форматирование к правилам проекта
- Проверяй diff после auto-correct, потому что форматирование может изменить несколько файлов
- Оставляй `config/detekt/detekt.yml` минимальным и добавляй туда только осознанные проектные отличия от default Detekt config
- Используй стандартные Detekt rules и ktlint wrapper rule set как базовый набор правил проекта

## Don't
- Не запускай auto-correct вслепую на несвязанных изменениях
- Не добавляй массовые suppress-аннотации
- Не отключай стандартные правила только ради прохождения проверки
- Не коммить baseline или generated Detekt config без отдельного решения
- Не добавляй Detekt dependency напрямую в module-level `build.gradle.kts`

## Commands

### Check
```bash
./gradlew detekt
```

### Auto-correct
```bash
./gradlew detekt --auto-correct
```

### Generate Full Config
```bash
./gradlew detektGenerateConfig
```

Генерировать полный config стоит только если нужно осознанно настраивать конкретные правила. По умолчанию проект использует `buildUponDefaultConfig = true`.
