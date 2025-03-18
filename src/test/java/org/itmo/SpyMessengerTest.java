package org.itmo;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;


/**
 *
 * _______________________________________
 Отправка сообщений

 Отправитель указывает получателя, текст сообщения и код доступа.
 Чтение сообщений

 Получатель вводит код доступа, чтобы прочитать сообщение.
 После первого чтения сообщение удаляется навсегда.
 Ограничение на количество сообщений

 У каждого пользователя может быть не более 5 непрочитанных сообщений.
 При попытке отправить шестое сообщение старейшее сообщение удаляется.
 Время жизни сообщений

 Если сообщение не прочитано в течение 1,5 секунд, оно удаляется автоматически.
 Если получатель не заходит в чат 3 секунды, все его сообщения удаляются.
 Как реализовать? Можно запускать потоки (ScheduledExecutorService), которые удаляют сообщения через нужное время.




 Корректную отправку и чтение сообщений

 Можно ли отправить сообщение и прочитать его, введя правильный код?
 Удаление после прочтения

 Исчезает ли сообщение после первого чтения?
 Удаление по таймеру

 Исчезает ли сообщение через 1,5 секунды?
 Ограничение на 5 сообщений

 Удаляется ли старое сообщение при отправке шестого?
 Проверка кода доступа

 Можно ли прочитать сообщение с неправильным кодом?
 Попытка взлома

 Если чужой пользователь попробует прочитать сообщение, удалится ли оно?

 */

class SpyMessengerTest {
    @Test
    void testMessageSelfDestructionAfterReading() {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");

        // Первое чтение — сообщение должно быть доступно
        assertNull(messenger.readMessage("Alice", "1234"));
        assertEquals("Top Secret", messenger.readMessage("Bob", "1234"));
        assertNull(messenger.readMessage("Bob", "1234"));
    }

    @Test
    void codeTest() {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        assertNull(messenger.readMessage("Bob", "12345"));
    }

    @Test
    void testMessageSelfDestructionAfterReadingWithDuration() throws InterruptedException {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");

        Thread.sleep(2000);

        assertNull(messenger.readMessage("Alice", "1234"));
    }

    @Test
    void testLimit5Messages() {
        SpyMessenger messenger = new SpyMessenger();
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        messenger.sendMessage("Alice", "Bob", "Top Secret", "1234");
        messenger.sendMessage("Fill", "Jim", "Top Secret2", "1234");
        messenger.sendMessage("Anna", "Rey", "Top Secret4", "1234");

        String result = messenger.readMessage("Rey", "1234");
        assertNotNull(result);
    }


}