# strings.md

## Purpose
Это правило описывает, как должны создаваться и именоваться строковые ресурсы в feature-модулях, и как они должны переводиться на поддерживаемые языки.

## Scope
Где применяется:
- All
- Правило распространяется на все строковые ресурсы в `res/values/strings.xml` и локализованных папках `res/values-<locale>/strings.xml` каждого `feature-<name>/impl/`.

## Principles
- Строковые ресурсы не должны пересекаться между feature-модулями.
- Каждый строковый ресурс обязан быть переведён на все поддерживаемые проектом языки.
- Отсутствие перевода для какой-либо локали недопустимо.
- Имя ресурса должно сразу подсказывать, какой фиче он принадлежит.

## Rules
- Имена ресурсов начинаются с обязательного префикса `feature_<name>_`, где `<name>` — имя фичи (директории `feature-<name>/`), приведённое к snake_case.
- При добавлении строки в `values/strings.xml` обязательно добавляй её перевод во все локализованные папки проекта.
- Все локализованные файлы держи в синхроне: одинаковые ключи, одинаковый порядок.
- Не используй строки одной фичи в коде другой фичи. Каждая фича объявляет свои строки самостоятельно.
- Базовая локаль (например, English) лежит в `values/`; локализации — в `values-<locale>/`.
- Поддерживаемые языки проекта согласовываются один раз и фиксируются в стандартном списке локалей; добавление новой локали — отдельное решение.

## Do
- Используй префикс `feature_<name>_` для каждой строки фичи без исключений.
- Добавляй строки сразу во все локализованные файлы фичи: `values/strings.xml`, `values-<locale1>/strings.xml`, `values-<locale2>/strings.xml`, … .
- Имя ключа описывай в формате `feature_<name>_<subject>_<role>`: `feature_x_list_title`, `feature_x_list_empty_message`, `feature_x_edit_save_button`.
- Группируй ресурсы внутри `strings.xml` по экранам/блокам комментариями `<!-- list screen -->`, `<!-- edit screen -->`, если файл становится большим.
- Используй `ResProvider` (из `framework/tools`) для подстановки строк в `Mapper`-классах фичи; не вызывай `Context.getString` напрямую в presentation.

## Don't
- Не создавай строковый ресурс без префикса `feature_<name>_`.
- Не добавляй строку только в одну локаль — это сломает сборку в других локалях и оставит дыры в UI.
- Не используй общие имена без префикса: `title`, `button_ok`, `error_message`.
- Не заимствуй строки из других feature-модулей по их `R.string.<key>` — каждая фича обязана объявить свои.
- Не размещай feature-строки в `framework/`. Там — только общие строки кросс-проектного UI-kit.

## Examples

### ✅ Correct — фича `x`, две локали

`feature-x/impl/src/main/res/values/strings.xml`:
```xml
<resources>
    <string name="feature_x_list_title">Items</string>
    <string name="feature_x_list_empty_message">No items yet</string>
    <string name="feature_x_edit_save_button">Save</string>
</resources>
```

`feature-x/impl/src/main/res/values-<locale1>/strings.xml`:
```xml
<resources>
    <string name="feature_x_list_title">Элементы</string>
    <string name="feature_x_list_empty_message">Пока ничего нет</string>
    <string name="feature_x_edit_save_button">Сохранить</string>
</resources>
```

`feature-x/impl/src/main/res/values-<locale2>/strings.xml`:
```xml
<resources>
    <string name="feature_x_list_title">Тізім</string>
    <string name="feature_x_list_empty_message">Әзірге бос</string>
    <string name="feature_x_edit_save_button">Сақтау</string>
</resources>
```

### ✅ Correct — использование через `ResProvider`

```kotlin
internal class XListMapper(
    private val resProvider: ResProvider,
) {
    fun toToolbarTitle(): String =
        resProvider.getString(R.string.feature_x_list_title)

    fun toEmptyMessage(): String =
        resProvider.getString(R.string.feature_x_list_empty_message)
}
```

### ❌ Incorrect

```xml
<!-- 1. Без префикса фичи -->
<resources>
    <string name="title">Items</string>                       <!-- ❌ -->
    <string name="empty_message">No items yet</string>        <!-- ❌ -->
    <string name="save_button">Save</string>                  <!-- ❌ -->
</resources>

<!-- 2. Перевод добавлен только в одну локаль -->
<!-- values/strings.xml -->
<resources>
    <string name="feature_x_list_title">Items</string>
    <string name="feature_x_list_empty_message">No items yet</string>
</resources>
<!-- values-<locale1>/strings.xml — отсутствует feature_x_list_empty_message -->  <!-- ❌ -->

<!-- 3. Перепутаны ключи между локалями (порядок и состав) -->
<!-- values/strings.xml -->
<resources>
    <string name="feature_x_a">A</string>
    <string name="feature_x_b">B</string>
</resources>
<!-- values-<locale1>/strings.xml -->
<resources>
    <string name="feature_x_b">Б</string>                     <!-- ❌ потеряли feature_x_a -->
</resources>
```

```kotlin
// 4. Заимствование строки чужой фичи
internal class XListMapper(...) {
    fun title(): String =
        resProvider.getString(com.<root.package>.feature.y.R.string.feature_y_some_text)   // ❌
}
```
