## Идея

Надо откатываться от лишних классов, которые фактически не используются напрямую, а только через наследников, но своим наличием помогают по смыслу организовать другие классы. В таком случае лучше отдать предпочтение интерфейсам.

В ООАП по Бертрану Мейеру это выражается в том, что:

1. После выделения сущностей в системе, следующим этапом проходимся по ним и исключаем все лишние.

2. На начальном этапе проектировании стараемся обойтись вообще без наследования, в исключительных случаях можем минимизировать его 1-2 уровнями, но и это лучше делать позже.

В работе иерархию наследования использую по минимому, только для вынесения каркаса взаимодействия методов.

Фактически везде интерфейсы и в методах опираюсь на механизм interface dispatch.

## Примеры

1. Была надежда что получится для общения с клиентом и записи в БД использовать одни и те же DTO, но фактически и это обошел, что оказалось лучше, т.к. не все данные надо клиенту отдавать.

   Понимаю саму идею отказа от лишних классов в иерархии так:

2. Синтетический пример
Если я пишу симулятор жизни горожанина. В системе есть домашняя кошка, которую можно гладить и кормить, а есть кошка в цирке Куклачева, на которую мы только смотрим и никак иначе не взаимодействуем.

   Можно создать родителя Cat для HomeCat и CircusCat, но лучше этого не делать, т.к. поведение отличается колосально, и вторая кошка вообще скорее DTO для View, которую мы только видим.

## Вывод

Надо предпочитать интерфейс классу и не создавать искусственных иерархий.