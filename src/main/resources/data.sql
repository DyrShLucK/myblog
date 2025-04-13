-- Добавление постов
INSERT INTO post (title, image, text, tags) VALUES
('10 лайфхаков для путешественников', 'images/post_1', 'Советы по экономии и удобству в поездках...', 'путешествия, советы'),
('Рецепт идеального кофе', 'images/post_2', 'Как сварить кофе лучше, чем в Starbucks...', 'кулинария, кофе'),
('Обзор нового iPhone 15', 'images/post_3', 'Новые функции и особенности...', 'технологии, apple'),
('Как научиться медитировать', 'images/post_4', 'Полное руководство для начинающих...', 'здоровье, духовность'),
('Топ-5 книг 2023 года', 'images/post_5', 'Обзор лучших новинок литературы...', 'книги, рекомендации'),
('Секреты японской кухни', 'images/post_6', 'Традиционные рецепты и техники...', 'кулинария, япония'),
('Утренние ритуалы успешных людей', 'images/post_7', 'Привычки, которые изменят ваш день...', 'саморазвитие'),
('История Великой Китайской стены', 'images/post_8', 'Забытые факты о строительстве...', 'история, архитектура'),
('Гид по национальным паркам США', 'images/post_9', 'Самые красивые природные места...', 'путешествия, природа'),
('Как выбрать ноутбук в 2023', 'images/post_10', 'Рекомендации по характеристикам...', 'технологии, гайд'),
('Искусство минимализма', 'images/post_11', 'Как избавиться от лишнего...', 'стиль жизни'),
('Фестиваль цветов в Голландии', 'images/post_12', 'Когда и как попасть на праздник...', 'путешествия, события'),
('Быстрые завтраки для занятых', 'images/post_13', '5 рецептов за 10 минут...', 'кулинария, рецепты'),
('История космических полетов', 'images/post_14', 'От Гагарина до современности...', 'наука, космос'),
('Гайд по фотосъемке смартфоном', 'images/post_15', 'Советы для идеальных снимков...', 'технологии, фото'),
('Секреты долголетия от ученых', 'images/post_16', 'Последние исследования...', 'здоровье, наука'),
('Тренды веб-дизайна 2023', 'images/post_17', 'Что будет актуально...', 'дизайн, технологии'),
('Как вырастить орхидею', 'images/post_18', 'Советы по уходу...', 'цветоводство, хобби'),
('История Византийской империи', 'images/post_19', 'Важные события и деятели...', 'история, культура'),
('Эко-советы для дома', 'images/post_20', 'Как сделать жизнь экологичнее...', 'экология, советы');

-- Добавление комментариев
INSERT INTO comment (text, post_id) VALUES
('Отличные советы!', 1),
('Спасибо, пригодится в следующей поездке', 1),
('А как насчет безопасности в путешествиях?', 1),

('Обязательно попробую!', 2),
('Лучше использовать арабику?', 2),

('Жду обзор камер!', 3),
('Стоит ли обновляться с iPhone 13?', 3),
('Цена слишком высокая...', 3),
('Дизайн почти не изменился', 3),

('Сложно для новичков', 4),
('Стало легче после недели практики', 4),
('Как выбрать место для медитации?', 4),

('Прочитал все книги из списка!', 5),
('Рекомендую добавить "Метро 2033"', 5),

('Спасибо за рецепты!', 6),
('Лучше использовать соевый соус?', 6),

('Попробовал - день стал продуктивнее!', 7),
('Сколько времени нужно уделять ритуалам?', 7),

('Не знала этих фактов!', 8),
('Какие части стены лучше сохранились?', 8),

('Планирую посетить Yellowstone', 9),
('Какие парки самые посещаемые?', 9),
('Совет по аренде авто?', 9),

('Спасибо за сравнение характеристик!', 10),
('Посоветуйте SSD!', 10),

('Сложно привыкнуть к минимализму', 11),
('Как хранить документы?', 11),

('Когда лучше ехать?', 12),
('Сколько стоит вход?', 12),

('Идеально для утра!', 13),
('Добавил в закладки', 13),

('Вдохновляет!', 14),
('Как стать космонавтом?', 14),

('Фотки стали лучше!', 15),
('Как сделать ночной снимок?', 15),

('Хочу дожить до 100!', 16),
('Важно ли генетика?', 16),

('Современные тренды радуют', 17),
('Адаптивный дизайн обязателен?', 17),

('У меня не цвела 2 года', 18),
('Помог пересадка!', 18),

('Интересные исторические детали', 19),
('Как пало Константинополь?', 19),

('Перешел на солнечные батареи', 20),
('Сложно найти биоразлагаемые товары', 20);