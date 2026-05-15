# strings.md

## Purpose
Это правило описывает, как должны создаваться и именоваться строковые ресурсы в feature-модулях

## Scope
Где применяется:
- All
- Правило распространяется на все строковые ресурсы в `res/values/strings.xml` и локализованных папках

## Principles
- Строковые ресурсы не должны пересекаться между feature-модулями
- Каждый строковый ресурс должен быть переведён на все поддерживаемые языки
- Отсутствие перевода недопустимо

## Rules
- Называй каждый строковый ресурс с обязательным префиксом названия фичи в формате `feature_name_`
- Например, для `feature-listening` префикс — `feature_listening_`, для `feature-email` — `feature_email_`
- При добавлении строки в `values/strings.xml` обязательно добавляй её с переводом в `values-ru/strings.xml` и `values-kk/strings.xml`
- Не оставляй строки без перевода ни в одной из локалей
- Не используй строковые ресурсы одной фичи в другой фиче

## Do
- Используй префикс фичи для всех строк без исключений
- Добавляй строки сразу во все три файла: `values`, `values-ru`, `values-kk`
- Держи строки всех локалей синхронизированными: одинаковые ключи, одинаковый порядок

## Don't
- Не создавай строковый ресурс без префикса фичи
- Не добавляй строку только в одну локаль
- Не используй общие имена без префикса, например `title`, `button_ok`, `error_message`
- Не заимствуй строки из других feature-модулей

## Examples
### ✅ Correct
`feature-listening/src/main/res/values/strings.xml`
```xml
<resources>
    <string name="feature_listening_title">SMS Listening</string>
    <string name="feature_listening_enable_button">Enable</string>
</resources>
```

`feature-listening/src/main/res/values-ru/strings.xml`
```xml
<resources>
    <string name="feature_listening_title">Прослушивание SMS</string>
    <string name="feature_listening_enable_button">Включить</string>
</resources>
```

`feature-listening/src/main/res/values-kk/strings.xml`
```xml
<resources>
    <string name="feature_listening_title">SMS тыңдау</string>
    <string name="feature_listening_enable_button">Қосу</string>
</resources>
```

### ❌ Incorrect
```xml
<string name="title">SMS Listening</string>
<string name="enable_button">Enable</string>
```
