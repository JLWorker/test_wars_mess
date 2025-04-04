## 📜 Инструкция для участников соревнования по тестированию Java

### 🎯 Цель задания

Ваша задача — разработать мессенджер для шпионов, который соблюдает строгие правила безопасности.

Один студент пишет тесты, второй — код, который проходит эти тесты.

### 🕵️‍♂️ Описание мессенджера

Этот мессенджер позволяет шпионам обмениваться секретными сообщениями.
Сообщения самоуничтожаются в определенных ситуациях, а также требуют код доступа для прочтения.

### 🔧 Функциональные требования

- **Отправка сообщений**
    - Отправитель указывает получателя, текст сообщения и код доступа.

- **Чтение сообщений**
    - Получатель вводит код доступа, чтобы прочитать сообщение.
    - После первого чтения сообщение удаляется навсегда.

- **Ограничение на количество сообщений**
    - У каждого пользователя может быть не более 5 непрочитанных сообщений.
    - При попытке отправить шестое сообщение старейшее сообщение удаляется.

- **Время жизни сообщений**
    - Если сообщение не прочитано в течение 1,5 секунд, оно удаляется автоматически.
    - Если получатель не заходит в чат 3 секунды, все его сообщения удаляются.
    - **Как реализовать?** Можно запускать потоки (`ScheduledExecutorService`), которые удаляют сообщения через нужное время.

### 📌 Интерфейс класса

Ваш класс должен реализовывать следующий интерфейс:

```java
class SpyMessenger {
    void sendMessage(String sender, String receiver, String message, String passcode);
    String readMessage(String user, String passcode); // Нужно ввести код доступа
}
```

### 🔍 Что проверять в тестах?

- **Корректную отправку и чтение сообщений**
    - Можно ли отправить сообщение и прочитать его, введя правильный код?

- **Удаление после прочтения**
    - Исчезает ли сообщение после первого чтения?

- **Удаление по таймеру**
    - Исчезает ли сообщение через 1,5 секунды?
    - Исчезают ли все сообщения, если пользователь не заходит в чат 3 секунды?

- **Ограничение на 5 сообщений**
    - Удаляется ли старое сообщение при отправке шестого?

- **Проверка кода доступа**
    - Можно ли прочитать сообщение с неправильным кодом?

- **Попытка взлома**
    - Если чужой пользователь попробует прочитать сообщение, удалится ли оно?

### 🏆 Как определить победителя?

- **Если код проходит все тесты** → студент, написавший код, получает **5 баллов**.
- **Если тесты нашли баги** → студент, написавший тесты, получает **5 баллов**.
- **Если код работает, но тесты не проверяют все случаи** → оценка делится.

### ✅ Пример теста (JUnit 5)

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

class SpyMessengerTest {
    @Test
    void testMessageSelfDestructionAfterReading() {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");

        // Первое чтение — сообщение должно быть доступно
        assertEquals("Top Secret", messenger.readMessage("Bob", "1234"));

        // Второе чтение — сообщение уже должно исчезнуть
        assertNull(messenger.readMessage("Bob", "1234"));
    }

    @Test
    void testWrongPasscode() {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Secret", "1234");

        // Попытка прочитать с неправильным паролем
        assertNull(messenger.readMessage("Bob", "0000"));
    }

    @Test
    void testMessageDeletionAfterTimeout() throws InterruptedException {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Self-Destruct", "1234");

        // Ждем 1.6 секунды (дольше, чем 1.5 секунды)
        TimeUnit.MILLISECONDS.sleep(1600);

        // Сообщение должно быть удалено
        assertNull(messenger.readMessage("Bob", "1234"));
    }
}
```

### 🚀 Готовы? Тогда начинаем!

1. Один студент пишет тесты.
2. Второй пишет код так, чтобы он прошел все тесты.
3. Победит тот, кто лучше протестирует или лучше напишет код!

### 🕒 Пояснение про время жизни сообщений

В шпионском мессенджере важны строгие временные ограничения, чтобы информация не могла утечь.

#### 🔥 1. Самоуничтожение сообщения через 1,5 секунды

- Когда отправитель отправляет сообщение, оно ждет 1,5 секунды.
- Если получатель не успел его прочитать за это время, оно удаляется автоматически.
- Даже если получатель зайдет позже, он уже не сможет его прочитать.

Пример:

- 🕵️‍♂️ Алиса отправляет Бобу сообщение в **12:00:00**.
- Боб не заходит в чат до **12:00:01.5**.
- Сообщение удаляется.
- В **12:00:02** Боб заходит в чат, но сообщения уже нет.

#### ⏳ 2. Очистка всех сообщений через 3 секунды неактивности

- Если получатель не заходит в чат 3 секунды, то все его непрочитанные сообщения удаляются.
- Это сделано для безопасности: если агент долго не выходит на связь, сообщения уничтожаются.

Пример:

- Боб получает 3 сообщения в **12:00:00**.
- Боб не заходит в чат до **12:00:03**.
- В **12:00:03** все 3 сообщения удаляются, даже если у них еще не истекли 1,5 секунды.
- В **12:00:04** Боб заходит в чат, но все сообщения исчезли.

### 🔍 В чем разница между этими правилами?

| Время    | Условие                 | Результат                     |
|----------|-------------------------|--------------------------------|
| **1,5 сек** | Сообщение не прочитано  | Только это сообщение удаляется |
| **3 сек**   | Получатель не зашел в чат | Удаляются все его сообщения   |
