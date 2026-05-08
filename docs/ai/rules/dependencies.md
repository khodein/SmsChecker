# dependencies.md

## Purpose
Это правило описывает, как должны добавляться, группироваться и подключаться зависимости проекта через Gradle Version Catalog и build-logic

## Scope
Где применяется:
- All
- Правило распространяется на `gradle/libs.versions.toml`, `build-logic`, Gradle-плагины модулей и подключение библиотек

## Principles
- Версии библиотек и Gradle-плагинов должны храниться в одном месте
- Подключение зависимостей должно быть сгруппировано по назначению и вынесено в convention plugins
- Gradle-модули должны подключать готовые группы зависимостей через `id(...)`, а не дублировать списки библиотек
- build-logic должен описывать правила подключения, но не должен становиться второй точкой хранения версий
- Базовые convention plugins должны задавать тип модуля, а dependency convention plugins должны подключать переиспользуемые группы возможностей
- Dependency convention plugins должны подходить для application- и library-модулей, если группа зависимостей может использоваться в обоих типах модулей
- Новые библиотеки должны добавляться только осознанно и с разрешения

## Rules
- Храни версии библиотек и Gradle-плагинов только в `gradle/libs.versions.toml`
- Добавляй новые алиасы библиотек в секцию `[libraries]` файла `gradle/libs.versions.toml`
- Добавляй новые алиасы Gradle-плагинов в секцию `[plugins]` файла `gradle/libs.versions.toml`
- Добавляй версии только в секцию `[versions]` и переиспользуй их через `version.ref`
- При добавлении зависимости сначала найди существующую смысловую категорию в `[versions]`, `[libraries]` и `[plugins]`
- Если подходящая категория уже есть, добавляй новую версию, библиотеку или plugin alias внутрь этой категории
- Если подходящей категории нет, создай новую категорию в конце соответствующей секции
- Новую категорию отделяй пустой строкой и коротким комментарием, например `# Analytics`, `# Database`, `# Code quality`
- Сохраняй одинаковый порядок категорий между `[versions]`, `[libraries]` и `[plugins]`, если категория используется в нескольких секциях
- Не указывай версии библиотек напрямую в `build.gradle.kts` модулей
- Не указывай версии библиотек напрямую в файлах `build-logic/src/main/kotlin/*.gradle.kts`
- Для подключения группы связанных зависимостей создавай отдельный convention plugin в `build-logic/src/main/kotlin/`
- Называй convention plugin по назначению группы, например `smschecker.android.compose`, `smschecker.ktor`, `smschecker.koin`
- Для Android application настроек используй `smschecker.android.application`
- Для Android library модулей используй `smschecker.android.library`
- Не смешивай настройку типа модуля и подключение всех библиотек в одном application или library plugin
- Подключай переиспользуемые dependency-группы отдельными plugin id, например `smschecker.android.core`, `smschecker.android.compose`, `smschecker.android.room`
- Для feature-модулей используй `smschecker.android.feature`, если модулю нужен стандартный набор feature-зависимостей
- Feature-модули называй по шаблону `feature-{name}`, например `feature-listening`
- При создании нового feature-модуля достаточно подключить `smschecker.android.feature` и указать `namespace`
- Не добавляй feature-модуль вручную в `settings.gradle.kts` через `include(...)`
- Новый Gradle-модуль подключается автоматически, если его директория находится в корне проекта и содержит `build.gradle.kts`
- Не добавляй feature-модуль вручную в dependencies блока `app/build.gradle.kts`
- Feature-модули с именем `feature-*` автоматически подключаются к `app` через `implementationFeatureModules()`
- Feature convention plugin должен быть Android library plugin и подключать только feature-level зависимости
- Для feature-модулей подключай только `androidx.navigation3:navigation3-runtime`, если нужен только `NavKey`
- Если feature-модулю нужна одна библиотека из большой группы, добавляй ее прямо в feature convention plugin, а не создавай отдельный plugin под одну dependency
- Feature-модуль может использовать `smschecker.android.room` для `Dao` и `Entity`, но не должен объявлять `RoomDatabase`
- Application-level `RoomDatabase` должен оставаться в `app`
- Подключай группы зависимостей в модуле через `plugins { id("...") }`
- Переиспользуемый dependency convention plugin должен добавлять зависимости только после применения `com.android.application` или `com.android.library`, если группа может использоваться в обоих типах модулей
- Feature convention plugin является library-only и может добавлять feature-level зависимости только после применения `com.android.library`
- Для ожидания Android-плагина в dependency convention plugin используй `pluginManager.withPlugin("com.android.application")` и `pluginManager.withPlugin("com.android.library")`
- Если dependency convention plugin настраивает Android extension, он должен отдельно обрабатывать `ApplicationExtension` и `LibraryExtension`
- Если dependency convention plugin добавляет зависимости через helper-функцию, делай ее extension-функцией на `DependencyHandler`
- Не добавляй большие списки `implementation(...)` напрямую в `app/build.gradle.kts`, если для них можно создать или переиспользовать convention plugin
- Допускается оставлять зависимости напрямую в конкретном module `build.gradle.kts`, если это точечная специфика одного модуля и она не повторяется в других модулях
- Не смешивай несвязанные зависимости в одном convention plugin без необходимости
- Если библиотеке нужен Gradle-плагин, подключай этот Gradle-плагин внутри соответствующего convention plugin
- Если Gradle-плагин нужен для компиляции build-logic, добавляй его marker dependency в `build-logic/build.gradle.kts`
- Для доступа к Version Catalog внутри precompiled script plugin используй `VersionCatalogsExtension`
- Не используй type-safe accessor `libs.some.library` внутри precompiled script plugin, если он недоступен в build-logic
- При добавлении новой внешней библиотеки сначала получи разрешение

## Do
- Оставляй `gradle/libs.versions.toml` единой точкой правды для версий
- Группируй зависимости по смыслу: Compose, Navigation, Koin, Ktor, Coil, test и другие отдельные направления
- Добавляй новые элементы в уже существующую категорию, если она подходит по назначению зависимости
- Добавляй новую категорию в конец секции, если подходящей категории еще нет
- Делай convention plugins маленькими и понятными по ответственности
- Подключай в модуле только те convention plugins, которые действительно нужны модулю
- Для feature-модулей используй только `smschecker.android.feature` и `namespace`
- Создавай директорию feature-модуля в корне проекта по шаблону `feature-{name}`
- Доверяй автоматическому подключению feature-модулей в `settings.gradle.kts` и `app/build.gradle.kts`
- Для shared-модулей явно подключай базовый plugin типа модуля и нужные dependency plugins
- Для нового feature-модуля делай `build.gradle.kts` минимальным: feature plugin и `android { namespace = "..." }`
- Для общих модулей вроде `framework` подключай только те группы, которые реально нужны этому модулю
- Используй BOM через `platform(...)`, если библиотечная группа поддерживает BOM
- Для Android application настроек используй отдельный application convention plugin
- Для Android library настроек используй отдельный library convention plugin
- Для базовых AndroidX зависимостей используй отдельный core convention plugin
- Для UI-зависимостей Compose используй отдельный Compose convention plugin
- Для локальной базы данных Room используй отдельный Room convention plugin
- Для DAO и Entity в feature-модулях переиспользуй `smschecker.android.room`, но `RoomDatabase` объявляй только в `app`
- Для тестовых зависимостей используй отдельный test convention plugin
- Удаляй прямые зависимости из модуля после переноса в convention plugin
- Сохраняй читаемость `app/build.gradle.kts`, чтобы по списку plugin id было понятно, какие возможности использует модуль
- Проверяй, что новая зависимость не дублирует уже подключенную библиотеку или существующий стек проекта

## Don't
- Не создавай второй каталог версий внутри build-logic
- Не храни версии библиотек в Kotlin-коде convention plugins
- Не добавляй зависимости напрямую в `app/build.gradle.kts`, если они относятся к уже существующей группе
- Не выноси зависимость в отдельный convention plugin, если она нужна только одному конкретному модулю и не образует переиспользуемую группу
- Не создавай один общий convention plugin со всеми библиотеками без необходимости
- Не заставляй application plugin автоматически подключать все dependency-группы, если модуль может явно объявить нужные возможности
- Не подключай библиотеку в группу, к которой она не относится по назначению
- Не вставляй новую зависимость в случайное место файла без учета категории
- Не создавай новую категорию, если уже есть подходящая существующая категория
- Не размещай новую категорию в середине секции без причины
- Не добавляй новую библиотеку без разрешения
- Не добавляй Gradle repositories в module-level `build.gradle.kts`
- Не добавляй Gradle repositories в `build-logic/build.gradle.kts`, если включен режим `RepositoriesMode.FAIL_ON_PROJECT_REPOS`
- Не применяй устаревшие или конфликтующие Gradle-плагины
- Не дублируй одну и ту же зависимость в нескольких convention plugins без явной причины
- Не меняй версии существующих библиотек без отдельной причины и понимания влияния на проект

## Examples
### ✅ Correct
`gradle/libs.versions.toml`
```toml
[versions]
# Images
coil = "3.4.0"

[libraries]
# Images
coil = { module = "io.coil-kt.coil3:coil", version.ref = "coil" }
coil-network-okhttp = { module = "io.coil-kt.coil3:coil-network-okhttp", version.ref = "coil" }
```

`build-logic/src/main/kotlin/smschecker.coil.gradle.kts`
```kotlin
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addCoilDependencies() {
    add("implementation", libs.findLibrary("coil").get())
    add("implementation", libs.findLibrary("coil-network-okhttp").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addCoilDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addCoilDependencies()
}
```

`app/build.gradle.kts`
```kotlin
plugins {
    id("smschecker.android.application")
    id("smschecker.coil")
}
```

`app/build.gradle.kts` для app-level `RoomDatabase`
```kotlin
plugins {
    id("smschecker.android.application")
    id("smschecker.android.core")
    id("smschecker.android.room")
}
```

`feature/build.gradle.kts`
```kotlin
plugins {
    id("smschecker.android.feature")
}

android {
    namespace = "com.sms.checker.forwarder.feature.example"
}
```

Feature-модуль должен лежать в директории вида:
```text
feature-example/
  build.gradle.kts
  src/main/AndroidManifest.xml
```

Добавлять `include(":feature-example")` в `settings.gradle.kts` не нужно.

Добавлять `implementation(project(":feature-example"))` в `app/build.gradle.kts` не нужно.

### ❌ Incorrect
```kotlin
dependencies {
    implementation("io.coil-kt.coil3:coil:3.4.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.4.0")
}
```

```kotlin
dependencies {
    add("implementation", "io.coil-kt.coil3:coil:3.4.0")
}
```
