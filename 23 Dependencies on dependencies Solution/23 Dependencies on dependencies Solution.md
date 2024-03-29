## Идея

На вопрос о зависимости можно отвечать по разному, но характерезовать это в конечном итоге можно будет учитывая три критерия.

**1. Семантика или конкретная вычислительная модель**

**2. Ключевае свойства**

**3. Пространство допустимых изменений**

Когда мы добавляем зависимость в наш менеджер пакетов, то у него определенно появилась новая зависимость, при этом, пока мы не начали использовать новый пакет в коде, можно говорить о том, что код от нее фактически не зависит.

Бывает что сохраняя один и тот же интерфейс и меняя реализацию (меняя зависимость) мы не меняя код, можем поменять ключевые свойства системы.

Например от более медленного сервиса получения географической карты, перешли на более быстрый. При этом с самой карты нам необходимы только координаты, на которые пользователь тыкнет на экране, то есть наш код взаимодействия не изменился.

Я не могу согласиться с тем, что если у всего от чего зависит наша система есть запасной компонент, то у нее нет зависимостей. Это было бы верно, если бы запасные компоненты не могли отказать.

Зависимостью будем называть ситуации, когда причиной происшествие чего-то с Б, являеется А. Значит Б зависело от А.
### Причинность
Различают _причинность гипотетическую_: _"Случайная искра может вызвать пожар"_.  
И _фактическую причинность_: _"Случайная искра в пекарне Джона Ллойда вызвала Великий лондонский пожар 1555 года."_

Что бы не увезнуть в теории о которой можно долго спорить, мы будем рассматривать только фактическую причинность.

### Определение зависимости 
В некоторой вычислительной модели свойство Р зависит от А тогда и
только тогда, когда А - фактическая причина Р (На некотором пространстве допустимых изменений А).

## Разбор примеров (из прошлого упражнения)
Рассмотрим примеры из прошлого упражнения с учетом новой формулировки зависимости.

### Зависимость зависимости

А независит от Б. Могут ли измененеия в Б повлиять на А?

А если из Б изменить глобальную переменную используемую в А?

По крайнему вопросу можем сказать, что в конкретной вычислительной модели использующей транзитивные зависимости, свойство А отвечающее за число создаваемых потоков, зависит от поведения Б, на множестве изменений им глобальной переменной используемой А(конкретно дефолтного числа создаваемых потоков).

От изменений на другом множестве переменных, вызванных поведением Б, модуль А не зависит.

### Зависимость перебрасывания

Если для работы А пробуем использовать Б, который недоступен и в этом случае используем С, то зависим ли мы от Б?

Когда С всегда доступен нет, независим, но так не бывает.

Наше свойство "быстрое обслуживание клиентов", в конкретной вычислительной модели обмена

платежными данными по сети, зависит от работоспособности Б (выполняющего работу быстро),

на пространстве изменений доступен/недоступен.

Свойство "совершение платежа" (при терпеливом клиенте) не зависит от Б, т.к. в случае его недоступности, платеж будет выполнен через С. 
Тут мы уже фактически зависим от С, т.к. на его пространстве изменений доступен/недоступен наше возможность совершения платежа может измениться. 
Зависит ли это косвенно от Б. Думаю что фактически нет. Это вроде как младший брат. Ему могут дать какое-то задание, 
но с учетом что присматривать будет старший и при необходимости его выполнит. 
Если же младший уйдет играть, а старший задание не выполнит, то вся ответственность ляжет на старшего. "Ты же ответсвенный".

## Примеры (новые)

### а) Проект зависит от модуля содержащего коды и описания ошибок.

0. Функциональные требования.

1. Свойство предоставления клиентам и разработчикам актуального описания ошибок и диапазона их кодов

2. На пространстве дополнений и изменений модуля описания ошибок (о невозможности скачать Даже старую версию не говорим здесь, а считаем проблемы только наличие актуальной информации в модуле с описанием ошибок.)

### 6) Проект зависит от хранилища артефактов.

0. Зависимости сборки.
   
2. Свойство собираемости(build).

3. На пространстве изменений: доступность/недоступность хранилища артефактов, отдающего для скачивания требуемой для проекта версии библиотеки. (без возможности ее замены другой

версией).

### в) Проект зависит от веб фрэймворка.

0. Функциональные требования.

1. Свойство разрешения на эксплуацию программы.

2. На пространстве версий веб фрэймворка, в разрезе отсутсвия/наличия в них известных уязвимостей.

### г) Проект зависит от его дизайна

0.  Функциональные требования.

1. Свойство скорости внесения положительных изменений в функционал.

2. На пространстве выбора возможных дизайнов для реализации решения.


## Итог

Дополнительные критерии определения зависимости позволили о ней рассуждать намного лучше, особенно само уточнение о фактической причинности.

Пространство в котором наше утверждение должно быть верным сильно сузилось, тем не менее, полностью для меня это ситуацию не прояснило, в том числе мой пример (б) для меня все еще дискусионен в пространстве изменений. Но тут так же можно применять критерий причинности

для отброса всего лишнего. Не учитывая проблемы сети, менеджера пакетов и т.п., как гипотетические, рассуждая лишь о фактических.
