package ru.otus.processor;

import ru.otus.model.Message;

public interface Processor {

    Message process(Message message);

    //ok_todo: 2. Сделать процессор, который поменяет местами значения field11 и field12

    //ok_todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
    //         Секунда должна определяьться во время выполнения.
    //         Тест - важная часть задания
    // Обязательно посмотрите пример к паттерну Мементо!
}
