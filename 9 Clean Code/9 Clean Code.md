## Улучшения ясности кода на уровне реализации

Попробуем найти в коде плохие примеры и то, как их исправить

### Примеры
####  1.1. Методы, которые используются только в тестах

Если говорить о методов классов, которые тестирую, то такого нет.
Возможны случаи, когда добавляются гетеры, используемые только в тестах, но тогда можно:
а) проводить сравненение только через equals всего объекта
б) в тесте создавать потомка, для которого менять видимость полей и тетсировать уже через него

Если о классах, которые предоставляют структуру для тестов - то там наличие специфических методов действительно требуется.

####  1.2. Цепочки методов. Метод вызывает другой метод, который вызывает другой метод, который вызывает другой метод, который вызывает другой метод... и далее и далее.

Иногда так делал, когда сигнатуру метода из класса утилит, упрощал для вызова в конкретном клиентском классе.
Например
```java
/**
* Метод с более простой сигнатурой, вызываемый из клиентов не имеющих данных об arg3.
**/
public void doIt(String arg1, String arg2){
	String arg3 = getAgr3ByArg2(arg2); 
	doIt(arg1, arg2, null);
}

/**
* В некоторых клиентских классах у нас уже доступен arg3,
* и что бы не задерживаться лишним вызовом для его поиска внутри тела перегруженного метода 
* doIt(String arg1, String arg2) сразу вызываем метод с тремя аргументами
**/
public void doIt(String arg1, String arg2, String arg3){
	// выполнение непосредственного действия
}
```
В данном случае можно подумать о выборочном сокрытии ненужных сигнатур методов для некоторых клиентов, но скорее всего можно и так оставить - обоснование длля чего описал в комментарии к методу.

####  1.3. У метода слишком большой список параметров.

Ну это классическая проблема, которую в случае конструктора скрывает билдер, как любят говорить на собеседованиях :).

В случае с конструктором здесь решение в правильном ограничении на максимальное число полей в классе.
```java
// до
class Warrior{

	Health health
	
	Power power
	
	Outfit outfit
	
	Name name
	
	public Warrior(Health health, 
					Power power, 
					Outfit outfit, 
					Name name){
		// ...
	}
}
```
```java
// после (сократили с 4 до 2 аргументы в конструкторе)
class Warrior{
	
	Сharacteristic characteristic
	
	Name name
	
	public Warrior(Сharacteristic characteristic, 
					Name name){
		// ...
	}
}

//  Собственные(внутренние) характеристики
class OwnСharacteristic{

	Health health
	
	Power power

	public OwnСharacteristic(Health health,
							Power power){
		// ...
	}
}
// Характеристики
class Сharacteristic{

	OwnСharacteristic ownСharacteristic
	
	Outfit outfit
	
	public Сharacteristic(OwnСharacteristic ownСharacteristic,
						Outfit outfit){
		// ...
	}
	
}
```
####  1.4. Странные решения. Когда несколько методов используются для решения одной и той же проблемы, создавая несогласованность.

Частично перекликается с пунктом 1.2, но там обоснованность такого решения описал.  

В примере ниже используется 4 перегрузки метода для форматирования, три из которых вызывают другой метод с аналогичным названием.
Удобство тут было, как и в 1.2, в том, что в разных классах клиентах имел набор аргументов в разных типах.
Минусом являлось то, что от перегрузок этого метода в глаха рябило :). 
Стоило добавить комментарии для каждого, что бы чисто визуально разделить их в тексте.
```java
  /**
   * Получение даты и времени в String в нужном часовом поясе и паттерне.
   */
  public static String getFormattedDateTime(LocalDateTime dateTime, ZoneOffset zoneOffset) {
    if (zoneOffset != null) {
      OffsetDateTime serverOffsetDateTime = dateTime.atZone(
          ZoneId.systemDefault()).toOffsetDateTime();

      OffsetDateTime clientOffsetDateTime = serverOffsetDateTime
          .withOffsetSameInstant(zoneOffset);

      return DATE_TIME_FORMATTER.format(clientOffsetDateTime);
    }
    return dateTime.format(DATE_TIME_FORMATTER);
  }
  
  public static String getFormattedDateTime(LocalDateTime dateTime, String offsetId) {
    return getFormattedDateTime(dateTime, ZoneOffset.of(offsetId));
  }

  public static String getFormattedDateTime(Timestamp timestamp, ZoneOffset zoneOffset) {
    return getFormattedDateTime(timestamp.toLocalDateTime(), zoneOffset);
  }

  public static String getFormattedDateTime(Timestamp timestamp, String offsetId) {
    return getFormattedDateTime(timestamp.toLocalDateTime(), ZoneOffset.of(offsetId));
  }
 ``` 
####  1.5. Чрезмерный результат. Метод возвращает больше данных, чем нужно вызывающему его компоненту.

Таких примеров не нашел, но если бы встретился с ними и потребовалось бы решение, то создал бы DTO,
конкретно под данный клинтский класс, с нужным набором полей.
