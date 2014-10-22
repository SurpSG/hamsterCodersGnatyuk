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


скриншот результата выполнения команды ab – c 100 – n 10000 http://somedomain/status
![ScreenShot](https://raw.github.com/SurpSG/hamsterCodersGnatyuk/master/Screenshot AB test 2014-10-22 14.07.21.png)

еще один скриншот станицы /status, но уже после выполнение команды ab из предыдущего пункта
![ScreenShot](https://raw.github.com/SurpSG/hamsterCodersGnatyuk/master/Screenshot Status page 2014-10-22 14.09.09.png)

скриншоты как выглядят станицы /status в рабочем приложении
![ScreenShot](https://raw.github.com/SurpSG/hamsterCodersGnatyuk/master/Screenshot status page 2014-10-22 14.17.50.png)


Имплементация
=============
Создано 4 слушателя для pipeline:
  - TrafficHandler(унаследован от ChannelTrafficShapingHandler) - сбор статистики;
      С помощью обьекта TrafficCounter собирается статистика: кол-во считанных байт, кол-во записаных байт, скорость     чтения/записи. Сбор статистики начинается когда вызываеться метод "channelRead" и заканчивается когда вызывается метод "channelReadComplete". Игнорируются запроси на "/favicon.ico" в статистических данных.
      
  - HttpRequestDecoder - декодирование потоков байт в HttpRequest
  - HttpResponseEncoder - кодирование HttpResponse в поток байт
  - HttpServerHandler(унаследован от SimpleChannelInboundHandler) - обработка HttpRequest и создание ответа - HttpResponse.
      Создается обьект ResponseFactory, который возвращает обьект AResponse(абстрактный клас для класов HelloWorldResponse, DefaultResponse, RedirectResponse, StatusResponse). С помощью этого обьекта выполняется ответ(response) на запрос(request).

ServerStatistic - клас для сбора статистических данных сервера. Для дизайна этого класа был применен паттерн проектирования - Singleton. Большинство методов этого класа синхронизированы(synchronized) т.к. они работают с общими ресурсами.
      

  
