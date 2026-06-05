# dependencies.md

## Purpose
Это правило описывает, как должны добавляться, группироваться и подключаться зависимости проекта через Gradle Version Catalog (`gradle/libs.versions.toml`) и convention plugins (`build-logic/`).

## Scope
Где применяется:
- All
- Правило распространяется на `gradle/libs.versions.toml`, `build-logic`, Gradle-плагины модулей и подключение библиотек в `app`, `framework/*` и feature-модулях.

## Principles
- Версии библиотек и Gradle-плагинов хранятся в одном месте — `gradle/libs.versions.toml`.
- Подключение зависимостей сгруппировано по назначению и вынесено в convention plugins из `build-logic/`.
- Модули подключают готовые группы зависимостей через `id("<prefix>.*")`, а не дублируют списки библиотек.
- `build-logic` описывает правила подключения, но не дублирует версии — версии всегда берутся из `libs.versions.toml`.
- Базовые convention plugins задают тип модуля (`<prefix>.android.application`, `<prefix>.android.library`); dependency convention plugins подключают переиспользуемые группы (`<prefix>.android.core`, `<prefix>.android.compose`, `<prefix>.android.room`, `<prefix>.koin`, …).
- Dependency convention plugins подходят и application-, и library-модулям, если группа применима к обоим типам.
- Новые библиотеки добавляются только осознанно и с разрешения пользователя.

## Convention plugins проекта

| Plugin | Назначение |
|---|---|
| `<prefix>.android.application` | application-модуль (`app/`) — базовые `compileSdk`, `targetSdk`, `versionCode/Name`, buildTypes |
| `<prefix>.android.library` | базовая настройка library-модуля |
| `<prefix>.android.core` | базовые AndroidX core-зависимости |
| `<prefix>.android.compose` | Jetpack Compose: bom, foundation, material3, runtime, tooling |
| `<prefix>.android.navigation3` | Navigation 3 runtime/ui для модулей, которым нужен `NavKey` |
| `<prefix>.android.room` | Room runtime + KSP compiler |
| `<prefix>.android.datastore` | DataStore |
| `<prefix>.android.test` | тестовые зависимости |
| `<prefix>.koin` | Koin core + compose + android |
| `<prefix>.ktor` | Ktor client + content negotiation |
| `<prefix>.coil` | Coil core + network |
| `<prefix>.android.feature` | full feature-конфигурация для `impl`-подмодуля: library + core + compose + koin + ktor + coil + room + datastore + navigation3 + автоподключение `:framework`, `:framework:router`, `:framework:tools` |
| `<prefix>.android.feature.api` | минимальная конфигурация для `api`-подмодуля: library + core + автоподключение только `:framework:tools` |

Префикс `<prefix>` фиксируется в проекте один раз (например, `app`, `myapp`, `<projectname>`).

## Rules

### Каталог версий
- Храни версии библиотек и Gradle-плагинов только в `gradle/libs.versions.toml`.
- Добавляй новые алиасы библиотек в секцию `[libraries]`.
- Добавляй новые алиасы Gradle-плагинов в секцию `[plugins]`.
- Добавляй версии только в секцию `[versions]` и переиспользуй через `version.ref`.
- При добавлении зависимости сначала найди существующую смысловую категорию в `[versions]`, `[libraries]` и `[plugins]`.
- Если подходящая категория уже есть, добавляй новую версию/библиотеку/plugin alias внутри этой категории.
- Если подходящей категории нет, создай новую категорию в конце соответствующей секции с пустой строкой и комментарием (`# Analytics`, `# Database`).
- Сохраняй одинаковый порядок категорий между `[versions]`, `[libraries]` и `[plugins]`, если категория используется в нескольких секциях.
- Не указывай версии библиотек напрямую в `build.gradle.kts` модулей.
- Не указывай версии библиотек напрямую в файлах `build-logic/src/main/kotlin/*.gradle.kts`.

### Convention plugins (`build-logic/`)
- Для подключения группы связанных зависимостей создавай отдельный convention plugin в `build-logic/src/main/kotlin/`.
- Называй convention plugin по назначению группы: `<prefix>.android.compose`, `<prefix>.ktor`, `<prefix>.koin`.
- Для Android application настроек используй `<prefix>.android.application`.
- Для Android library модулей используй `<prefix>.android.library`.
- Не смешивай настройку типа модуля и подключение всех библиотек в одном application/library plugin.
- Подключай переиспользуемые dependency-группы отдельными plugin id: `<prefix>.android.core`, `<prefix>.android.compose`, `<prefix>.android.room`, и т.д.
- Переиспользуемый dependency convention plugin должен добавлять зависимости только после применения `com.android.application` или `com.android.library`, если группа может использоваться в обоих типах модулей.
- Для ожидания Android-плагина в dependency convention plugin используй `pluginManager.withPlugin("com.android.application")` и `pluginManager.withPlugin("com.android.library")`.
- Если dependency convention plugin настраивает Android extension, обрабатывай отдельно `ApplicationExtension` и `LibraryExtension`.
- Если dependency convention plugin добавляет зависимости через helper-функцию, делай её extension-функцией на `DependencyHandler`.
- Если библиотеке нужен Gradle-плагин, подключай этот Gradle-плагин внутри соответствующего convention plugin.
- Если Gradle-плагин нужен для компиляции build-logic, добавляй его marker dependency в `build-logic/build.gradle.kts`.
- Для доступа к Version Catalog внутри precompiled script plugin используй `VersionCatalogsExtension`. Не используй type-safe accessor `libs.some.library`, если он недоступен в build-logic.

### Feature-модули
- Feature-модуль — это папка-контейнер `feature-<name>/` с двумя Gradle-субмодулями `api` и `impl`.
- Для `impl` подключай только `<prefix>.android.feature` и указывай `namespace`.
- Для `api` подключай только `<prefix>.android.feature.api` и указывай `namespace`.
- `<prefix>.android.feature` уже подключает `:framework`, `:framework:router`, `:framework:tools` — не дублируй их вручную.
- `<prefix>.android.feature.api` уже подключает `:framework:tools` — не дублируй вручную.
- `impl` зависит от своего `api` через `implementation(project(":feature-<name>:api"))`.
- Если фиче нужна одна библиотека, которая не покрывается convention plugin, добавляй её точечно в `feature-<name>/impl/build.gradle.kts` (или в `api`, если library нужна именно публичному контракту).
- `settings.gradle.kts` подхватывает все `feature-<name>/api` и `feature-<name>/impl` автоматически — не добавляй `include` руками.
- `app/build.gradle.kts` подключает все feature-субмодули автоматически через `implementationFeatureModules()` — не добавляй `implementation(project(":feature-<name>:..."))` руками.
- `app` подключает базовую инфраструктуру явно: `:framework`, `:framework:router`, `:framework:tools`.
- Feature-модуль может использовать `<prefix>.android.room` для `Dao` и `Entity`, но `RoomDatabase` объявляется только в `app/db/` (см. `db.md`).

### Application-модуль (`app`)
- В `app/build.gradle.kts` подключай только те convention plugins, которые соответствуют возможностям модуля. Не подключай feature-зависимости вручную.
- Зависимости feature-субмодулей подхватываются `implementationFeatureModules()`. Сохраняй эту функцию в `app/build.gradle.kts` без модификаций.
- `<prefix>.android.application` отвечает только за тип модуля и базовые настройки SDK/buildTypes. Дополнительные группы (`<prefix>.koin`, `<prefix>.android.room`, и т.д.) подключаются отдельными `id(...)` в `app/build.gradle.kts`.

## Do
- Оставляй `gradle/libs.versions.toml` единой точкой правды для версий.
- Группируй зависимости по смыслу: Compose, Navigation, Koin, Ktor, Coil, Room, DataStore, Test, Analytics, и т.д.
- Добавляй новые элементы в уже существующую категорию, если она подходит.
- Добавляй новую категорию в конец секции, если подходящей категории нет.
- Делай convention plugins маленькими и понятными по ответственности.
- Подключай в модуле только те convention plugins, которые действительно нужны модулю.
- Для feature-модулей подключай только `<prefix>.android.feature` (`impl`) или `<prefix>.android.feature.api` (`api`).
- Создавай директорию feature-модуля в корне проекта по шаблону `feature-<name>/`.
- Доверяй автоматическому подключению feature-субмодулей в `settings.gradle.kts` и в `app/build.gradle.kts`.
- Для shared-модулей явно подключай базовый plugin типа модуля и нужные dependency plugins.
- Для нового feature-модуля делай `build.gradle.kts` минимальным: feature plugin и `android { namespace = "..." }`.
- Для общих модулей вроде `framework/*` подключай только те группы, которые реально нужны этому модулю.
- Используй BOM через `platform(...)`, если библиотечная группа поддерживает BOM (например, Compose BOM).
- Для базовых AndroidX зависимостей используй отдельный `<prefix>.android.core`.
- Для UI-зависимостей Compose используй отдельный `<prefix>.android.compose`.
- Для Room используй отдельный `<prefix>.android.room`.
- Для DataStore используй отдельный `<prefix>.android.datastore`.
- Для тестовых зависимостей используй отдельный `<prefix>.android.test`.
- Удаляй прямые зависимости из модуля после переноса в convention plugin.
- Сохраняй читаемость `app/build.gradle.kts`, чтобы по списку plugin id было понятно, какие возможности использует модуль.
- Проверяй, что новая зависимость не дублирует уже подключенную библиотеку.

## Don't
- Не создавай второй каталог версий внутри `build-logic/`.
- Не храни версии библиотек в Kotlin-коде convention plugins.
- Не добавляй зависимости напрямую в `app/build.gradle.kts`, если они относятся к уже существующей группе.
- Не выноси зависимость в отдельный convention plugin, если она нужна только одному конкретному модулю и не образует переиспользуемую группу.
- Не создавай один общий convention plugin со всеми библиотеками без необходимости.
- Не заставляй application plugin автоматически подключать все dependency-группы, если модуль может явно объявить нужные возможности.
- Не подключай библиотеку в группу, к которой она не относится по назначению.
- Не вставляй новую зависимость в случайное место файла без учёта категории.
- Не создавай новую категорию, если уже есть подходящая существующая категория.
- Не размещай новую категорию в середине секции без причины.
- Не добавляй новую библиотеку без разрешения.
- Не добавляй Gradle repositories в module-level `build.gradle.kts`.
- Не добавляй Gradle repositories в `build-logic/build.gradle.kts`, если включён режим `RepositoriesMode.FAIL_ON_PROJECT_REPOS`.
- Не применяй устаревшие или конфликтующие Gradle-плагины.
- Не дублируй одну и ту же зависимость в нескольких convention plugins без явной причины.
- Не меняй версии существующих библиотек без отдельной причины и понимания влияния на проект.
- Не добавляй `implementation(project(":framework"))`, `:framework:router`, `:framework:tools` в feature-модуль вручную — они уже подключаются через `<prefix>.android.feature` или `<prefix>.android.feature.api`.
- Не добавляй `include(":feature-<name>:...")` в `settings.gradle.kts` — модули подхватываются автоматически.
- Не добавляй `implementation(project(":feature-<name>:..."))` в `app/build.gradle.kts` — подключается через `implementationFeatureModules()`.

## Examples

### ✅ Correct — каталог версий и dependency convention plugin

`gradle/libs.versions.toml`:
```toml
[versions]
# Images
coil = "3.4.0"

[libraries]
# Images
coil = { module = "io.coil-kt.coil3:coil", version.ref = "coil" }
coil-network-okhttp = { module = "io.coil-kt.coil3:coil-network-okhttp", version.ref = "coil" }
```

`build-logic/src/main/kotlin/<prefix>.coil.gradle.kts`:
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

### ✅ Correct — application-модуль

`app/build.gradle.kts`:
```kotlin
plugins {
    id("<prefix>.android.application")
    id("<prefix>.android.core")
    id("<prefix>.android.compose")
    id("<prefix>.android.navigation3")
    id("<prefix>.koin")
    id("<prefix>.ktor")
    id("<prefix>.coil")
    id("<prefix>.android.room")
    id("<prefix>.android.datastore")
    id("<prefix>.android.test")
}

android {
    namespace = "<root.package>"

    defaultConfig {
        applicationId = "<root.package>"
    }
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":framework:tools"))
    implementation(project(":framework:router"))
    implementationFeatureModules()
}
```

### ✅ Correct — feature `api`-подмодуль

`feature-x/api/build.gradle.kts`:
```kotlin
plugins {
    id("<prefix>.android.feature.api")
}

android {
    namespace = "<root.package>.feature.x.api"
}
```

### ✅ Correct — feature `impl`-подмодуль

`feature-x/impl/build.gradle.kts`:
```kotlin
plugins {
    id("<prefix>.android.feature")
}

android {
    namespace = "<root.package>.feature.x"
}

dependencies {
    implementation(project(":feature-x:api"))

    // зависимости от api соседних фич — допустимо
    implementation(project(":feature-y:api"))
}
```

Подключать feature-`impl` нужно только если фиче нужна одна-две точечные библиотеки сверх стандартного набора convention plugin'а:

```kotlin
dependencies {
    implementation(project(":feature-x:api"))
    implementation(libs.some.specific.library)         // точечная библиотека только для этой фичи
}
```

Добавлять `include(":feature-x:api")` / `include(":feature-x:impl")` в `settings.gradle.kts` не нужно — фича подхватится автоматически.

Добавлять `implementation(project(":feature-x:api"))` или `:impl` в `app/build.gradle.kts` не нужно — `implementationFeatureModules()` подключает их сам.

### ❌ Incorrect

```kotlin
// 1. Версия напрямую в build.gradle.kts
dependencies {
    implementation("io.coil-kt.coil3:coil:3.4.0")        // ❌ только через libs.versions.toml
}

// 2. Версия напрямую в convention plugin
dependencies {
    add("implementation", "io.coil-kt.coil3:coil:3.4.0") // ❌
}

// 3. Подключение framework руками в feature
// feature-x/impl/build.gradle.kts
dependencies {
    implementation(project(":framework"))                // ❌ уже подключено через <prefix>.android.feature
    implementation(project(":framework:router"))         // ❌
    implementation(project(":framework:tools"))          // ❌
}

// 4. Ручной include feature в settings.gradle.kts
include(":feature-x:api")                                // ❌ подхватывается автоматически

// 5. Ручное подключение feature в app
// app/build.gradle.kts
dependencies {
    implementation(project(":feature-x:impl"))           // ❌ подключается через implementationFeatureModules()
}

// 6. Подключение impl соседней фичи
// feature-x/impl/build.gradle.kts
dependencies {
    implementation(project(":feature-y:impl"))           // ❌ только :feature-y:api
}

// 7. Compose в api-подмодуле
// feature-x/api/build.gradle.kts
plugins {
    id("<prefix>.android.feature.api")
    id("<prefix>.android.compose")                       // ❌ Compose в api не нужен
}

// 8. Application plugin собирает всё разом
// build-logic/src/main/kotlin/<prefix>.android.application.gradle.kts
plugins {
    id("com.android.application")
    id("<prefix>.android.compose")                       // ❌ не подключай dependency-группы в application plugin
    id("<prefix>.koin")                                  // ❌ модуль должен явно объявлять нужные возможности
}
```
