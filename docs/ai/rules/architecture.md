# architecture.md

## Purpose
Это правило описывает архитектурное решение в проекте

## Scope
Где применяется:
- All
- Правило распространяется на presentation, domain, data и связи между ними

## Principles
- Архитектура проекта строится на разделении ответственности между слоями presentation, domain и data
- Каждый слой должен зависеть только от своей зоны ответственности и не смешивать логику других слоев
- Бизнес-логика должна быть изолирована от UI, Android- и framework-зависимостей
- Взаимодействие с данными должно проходить через domain- и data-слой, а не напрямую из UI

## Rules
- Размещай UI-компоненты, `ViewModel`, `UiState` и навигацию только в слое presentation
- Размещай бизнес-логику, `UseCase` и domain-модели только в слое domain
- Размещай `Repository`, `request`, `response`, `entity`, мапперы, API и локальное хранилище только в слое data
- Для data-слоя создавай отдельные модели для каждого источника данных, например `HomeRequest`, `HomeResponse`, `HomeEntity`
- Для domain-слоя создавай отдельную domain-модель с суффиксом `Model`, например `HomeModel`
- Преобразовывай data-модели в domain-модели через мапперы
- Не используй `request`, `response` и `entity` модели за пределами data-слоя
- Не передавай domain-модели напрямую в Composable-функции
- Передавай данные в UI только через `UiState`
- Не обращайся к слою data напрямую из presentation
- Не используй Android-, Compose- и framework-типы в domain-слое
- Не размещай бизнес-логику в UI-компонентах и Composable-функциях
- Выполняй запросы к API и базе данных только через data-слой
- Сохраняй границы между presentation, domain и data при добавлении новой функциональности

## Do
- Создавай отдельные модели для `data`, `domain` и `presentation`, если у них разная ответственность
- Для data-слоя используй явные суффиксы моделей: `Request`, `Response`, `Entity`
- Для domain-слоя всегда используй domain-модель с суффиксом `Model`, например `HomeModel`
- Создавай мапперы для преобразования `request`, `response` и `entity` моделей в domain-модели
- Выполняй преобразование data-моделей в domain-модели внутри data-слоя
- Возвращай из `Repository` domain-модели или типы, пригодные для бизнес-логики, а не модели источника данных
- Передавай данные из `ViewModel` в UI только через `UiState`
- Держи `UiState` immutable и отражай в нем только состояние экрана
- Размещай бизнес-логику в `UseCase` и `ViewModel`, не вынося ее в Composable-функции
- Используй `suspend`-функции и `Flow` в соответствии с ответственностью слоя
- При добавлении новой функциональности сначала определяй, к какому слою относится каждая новая сущность
- Описывай детальные правила presentation-слоя отдельно в `viewmodel.md`, `screen.md` и `navigation.md`
- Описывай детальные правила domain-слоя отдельно в `usecase.md`
- Описывай детальные правила data-слоя отдельно в `repository.md`, `api.md` и `db.md`
- Описывай детальные правила внедрения зависимостей отдельно в `di.md`

## Don't
- Не используй одну и ту же модель одновременно в `data`, `domain` и `presentation`
- Не называй domain-модели без суффикса `Model`
- Не передавай `request`, `response` и `entity` модели в `ViewModel`, `UiState` и Composable-функции
- Не передавай domain-модели напрямую в Composable-функции
- Не выполняй маппинг data-моделей в UI или Composable-функциях
- Не размещай мапперы domain-моделей вне data-слоя без явной причины
- Не возвращай из `Repository` `request`, `response` или `entity` модели
- Не обращайся к API, базе данных или `Repository` напрямую из UI
- Не размещай бизнес-логику в Composable-функциях
- Не используй Android-, Compose- и framework-типы в domain-слое
- Не смешивай ответственность `presentation`, `domain` и `data` в одном классе
- Не обходи границы слоев ради сокращения количества классов или упрощения реализации
- Не игнорируй дополнительные `Don't`, описанные в `viewmodel.md`, `screen.md`, `navigation.md`, `usecase.md`, `repository.md`, `api.md`, `db.md` и `di.md`

## Examples
- Основные примеры размещай в `viewmodel.md`, `screen.md`, `navigation.md`, `usecase.md`, `repository.md`, `api.md`, `db.md` и `di.md`
