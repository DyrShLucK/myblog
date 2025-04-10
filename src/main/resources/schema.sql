CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    text TEXT NOT NULL,
    tags VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    likes_count INT DEFAULT 0
);

CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    post_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES post(id)
);
INSERT INTO post (title, image, text, tags)
VALUES (
    'Путешествие в Париж',
    'https://avatars.mds.yandex.net/i?id=1efb59edadec80b91aed9efcac68b04b_l-5877972-images-thumbs&n=27&h=480&w=480',
    'Незабываемое путешествие в столицу Франции! Посетили Эйфелеву башню, Лувр и насладились французской кухней.',
    '#путешествие #Париж'
);

-- Второй пост
INSERT INTO post (title, image, text, tags)
VALUES (
    'Рецепт домашней пиццы',
    'https://cs5.pikabu.ru/post_img/big/2015/06/04/11/1433446202_1725992411.jpg',
    'Приготовьте идеальную пиццу дома: замесите тесто, добавьте томатный соус, сыр и ваши любимые ингредиенты. Выпекайте 15 минут при 220°C.',
    '#кулинария #рецепты'
);