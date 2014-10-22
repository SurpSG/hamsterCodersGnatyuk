hamsterCodersGnatyuk
====================

Необходимо реализовать http-сервер на фреймворке netty (http://netty.io/), со следующим функционалом:

1. По запросу на http://somedomain/hello отдает «Hello World» через 10 секунд

2. По запросу на http://somedomain/redirect?url= происходит переадресация на указанный url

3. По запросу на http://somedomain/status выдается статистика:

  - общее количество запросов

  - количество уникальных запросов (по одному на IP)

  - счетчик запросов на каждый IP в виде таблицы с колонкам и IP, кол-во запросов, время последнего запроса

  - количество переадресаций по url’ам в виде таблицы, с колонками url, кол-во переадресация

  - количество соединений, открытых в данный момент

  - в виде таблицы лог из 16 последних обработанных соединений, колонки src_ip, URI, timestamp, sent_bytes, received_bytes, speed (bytes/sec)


Все это (вместе с особенностями имплементации в текстовом виде) выложить на github, приложить к этому:

скриншоты как выглядят станицы /status в рабочем приложении

скриншот результата выполнения команды ab – c 100 – n 10000 http://somedomain/status

еще один скриншот станицы /status, но уже после выполнение команды ab из предыдущего пункта
