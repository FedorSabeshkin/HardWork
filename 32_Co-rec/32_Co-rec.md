# Ко-рекурсивные программы

## Идея

Есть подход, в котором мы пишем функции, выводя их из структуры входных данных.   
Т.к. среди классических структура данных есть рекурсивные, например, такие как дерево или список, то и программы мы строим рекурсивные.   

Но важно рассмотреть случай, в котором входные данные очень обобщены, а выходные могут быть значительно конкретнее.     
Например парсинг строки в массив слогов.   
В данном случае, нам будет намного удобнее смотреть на выходные данные, что бы постепенно построить схему решения.   
Тем не менее, и первый подход бывает удобен.   


## Пример

Мне требовалось сформировать отчет в Excel, связав входные данные из системы_А с данными из системы_Б.

а) Я начал не с того, как мне требуется объединять эти **(входные)** данные, а с того что проанализировал формат **выходных** данных – отчет, который должен получиться.  
б) Выбрал, какие поля требуются.  
в) Описал DTO, которое, включало в себя все эти поля.  
г) Создал класс печати, заполняющее файл по этому DTO.  
д) Написал на него тест.   
И только теперь приступил к следующему шагу.  
е) Реализовал класс для получения данных из системы_Б, по данным из системы А и  преобразующий данные из системы_Б в DTO передаваемое на печать. (Тут заметил нарушение SRP – еще один плюс записи шагов).

## Вывод
Все дело в том, что нам недостаточно знать о существовании молотка, ведь в этом случае будет казаться, что везде гвозди.   
Важно так же знать, что есть гаечный ключ, и столкнувшись с проблемой подумать, на что она похожа больше: на гайку или гвоздь? 