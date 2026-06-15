# delegate.md

## Папка `delegate/` в feature-модуле

### Назначение

Описывает, что лежит в папке `delegate/` фичи и для чего она нужна.
`delegate/` — это слой, через который фича выставляет наружу или реализует у себя «контракт
исполнителя» (delegate): объект с lifecycle-методами (`onCreate`, `onDestroy`, `send`, …), который
другая фича или `app` подключает к своему рантайму (foreground service, broadcast receiver, фоновая
отправка и т.п.). Это **не** часть `domain` и не часть `data` — у делегата другие обязанности и
другая граница ответственности.

### Делает

- Хранит интерфейс делегата (`XDelegate`) в `api` и его реализацию (`XDelegateImpl`) в `impl`.
- Группирует вокруг делегата вспомогательные классы, которые имеют смысл только в его контексте:
  `Service`, `BroadcastReceiver`, `NotificationDelegate`, внутренние `Mapper`-ы делегата, `Facade`-ы
  и подпапки `management/`, `sending/` и т.п.
- Делает `XDelegate` публичным в `api` (его внедряют соседние фичи и `app`).
- Делает `XDelegateImpl` и все вспомогательные классы `internal` в `impl`.
- Регистрирует делегата в `XModule` через `factoryOf(::XDelegateImpl) bind XDelegate::class`.

### Не делает

- Не содержит бизнес-логику — она в `UseCase`. Делегат оркестрирует вызовы `UseCase` и работу с
  Android-рантаймом.
- Не содержит UI (Composable, ViewModel, State, Action). Это слой `presentation/`.
- Не содержит `Repository`, `Dao`, `Entity` и data-мапперы (entity ↔ domain). Это слои `data/` и
  `db/`.
- Не лежит внутри `domain/`. Интерфейс делегата в `api` лежит в `api/.../<feature>/delegate/`, а не
  в `api/.../<feature>/domain/delegate/`.
- Не называется `infrastructure/`. Папка с делегатами **всегда** называется `delegate/`.

### Расположение

```text
feature-<name>/
  api/
    src/main/java/<root.package>/feature/<name>/
      delegate/
        XDelegate.kt                — public interface
  impl/
    src/main/java/<root.package>/feature/<name>/
      delegate/
        XDelegateImpl.kt            — internal class XDelegateImpl : XDelegate
        <support>/                  — опциональные подпапки только для делегата:
          mapper/                     внутренние Android-мапперы делегата
          receiver/                   BroadcastReceiver
          management/                 NotificationDelegate и др. lifecycle-классы
          sending/facade/             facade-классы исполнителей
```

### Когда заводить `delegate/`

- Фича выставляет наружу runtime-контракт с lifecycle (`onCreate/onDestroy`, фоновая отправка,
  подписка на системные события).
- Соседняя фича или `app` должна уметь подключить рантайм этой фичи к собственному `Service` /
  `Activity` / другому рантайму, не зная деталей реализации.

Если такого контракта нет — папку `delegate/` создавать не нужно.

### Пример

Фича `sms` выставляет `SmsBroadcastDelegate`, который подписывается на системный SMS-broadcast и
отдаёт сохранённый `smsId` через `Provider`. Реализация лежит в `impl/delegate/` вместе со
вспомогательным `BroadcastReceiver` и `Mapper`-ом.

```text
feature-sms/
  api/src/main/java/<root.package>/feature/sms/
    delegate/
      SmsBroadcastDelegate.kt          // public interface + nested Provider
  impl/src/main/java/<root.package>/feature/sms/
    delegate/
      SmsBroadcastDelegateImpl.kt      // internal class : SmsBroadcastDelegate
      mapper/
        SmsBroadcastMapper.kt          // internal: SmsMessage -> SmsModel
      receiver/
        SmsBroadcastReceiver.kt        // internal: BroadcastReceiver
```

```kotlin
// api: контракт
interface SmsBroadcastDelegate {
    fun onCreate(provider: Provider)
    fun onDestroy()

    interface Provider {
        fun onReceiveSmsId(id: Long)
    }
}
```

```kotlin
// impl/XModule.kt
factoryOf(::SmsBroadcastDelegateImpl) bind SmsBroadcastDelegate::class
```
