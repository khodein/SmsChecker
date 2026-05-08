# usecase.md

## Purpose
Это правило описывает, как должны быть устроены `UseCase` в слое domain

## Scope
Где применяется:
- Domain
- Правило распространяется на `UseCase`, domain-модели и бизнес-логику в слое domain

## Principles
- `UseCase` отвечает только за бизнес-логику конкретного сценария
- `UseCase` не должен зависеть от UI, Android, Compose и framework-типов
- `UseCase` должен быть написан на чистом Kotlin
- `UseCase` работает только с domain-моделями и domain-контрактами
- `UseCase` должен обращаться к одному или нескольким `Repository` для получения и формирования данных сценария
- `UseCase` не должен обращаться напрямую к API и базе данных
- Один `UseCase` должен иметь одну понятную ответственность

## Rules
- Размещай `UseCase` только в слое domain
- Реализуй каждый `UseCase` как отдельный класс
- Используй один `UseCase` для одного бизнес-сценария
- Передавай зависимости в `UseCase` только через конструктор
- Используй в `UseCase` только `Repository` и другие domain-контракты
- Не обращайся к API и базе данных напрямую из `UseCase`
- Не используй `request`, `response` и `entity` модели в `UseCase`
- Используй в `UseCase` только domain-модели и domain-результаты
- Пиши бизнес-логику `UseCase` на чистом Kotlin
- Не используй Android-, Compose- и framework-типы в `UseCase`
- Возвращай из `UseCase` данные, пригодные для бизнес-логики, а не для прямого отображения в UI
- Используй `suspend` или `Flow` в зависимости от сценария
- Вызывай `UseCase` из `ViewModel`, а не из UI

## Do
- Называй каждый класс `UseCase` с обязательным суффиксом `UseCase`
- Используй в имени `UseCase` префикс действия по смыслу сценария, например `Get`, `Set`, `Delete`
- Называй `UseCase` по шаблону `GetHomeUseCase`, `SetHomeUseCase`, `DeleteHomeUseCase`
- Создавай отдельный `UseCase` для каждого бизнес-сценария
- Передавай `Repository` и другие domain-зависимости через конструктор
- Используй `UseCase` как точку входа в бизнес-логику для `ViewModel`
- Обращайся к одному или нескольким `Repository`, если это нужно для сценария
- Объединяй, проверяй и подготавливай данные внутри `UseCase` по правилам бизнес-логики
- Возвращай из `UseCase` только domain-модели или domain-результаты
- Реализуй вызов `UseCase` через `suspend operator fun invoke()`
- Держи `UseCase` простым, читаемым и ограниченным одной ответственностью
- Следуй правилам `repository.md` для получения данных и `viewmodel.md` для интеграции с presentation

## Don't
- Не создавай класс бизнес-логики без суффикса `UseCase`
- Не используй имя `UseCase` без префикса действия по смыслу сценария
- Не объединяй несколько разных бизнес-сценариев в одном `UseCase`
- Не размещай `UseCase` вне слоя domain
- Не обращайся к API и базе данных напрямую из `UseCase`
- Не передавай `request`, `response` и `entity` модели в `UseCase`
- Не используй Android-, Compose- и framework-типы в `UseCase`
- Не возвращай из `UseCase` data-модели или данные, подготовленные специально для UI
- Не передавай `ViewModel`, `UiState` и UI-модели в `UseCase`
- Не размещай логику отображения и навигации в `UseCase`
- Не создавай `UseCase` без `suspend operator fun invoke()`
- Не игнорируй `Repository`, если сценарий требует доступа к данным
- Не нарушай одну ответственность `UseCase` ради сокращения количества классов
- Не вызывай `UseCase` напрямую из UI

## Examples
### ✅ Correct
```text
home/
  domain/
    GetHomeUseCase.kt
    HomeModel.kt
    HomeRepository.kt
```

```kotlin
interface HomeRepository {
    suspend fun getHome(): HomeModel
}

data class HomeModel(
    val id: Int,
    val title: String
)

class GetHomeUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): HomeModel {
        val home = homeRepository.getHome()

        return home.copy(
            title = home.title.trim()
        )
    }
}
```

### ❌ Incorrect
```kotlin
class HomeUseCase(
    private val homeApi: HomeApi
) {
    fun loadHome(): HomeResponse {
        return homeApi.getHome()
    }
}
```
