## Задание
Необходимо познакомиться с паттерном Visitor и проблемами, которые возникают с использованием наследования в проектах.    
После чего показать пример использования Visitor.

## Решение

### Краткое описание паттерна Visitor
При разработке мы можем вынести все схожее поведение под некоторый общий тип/класс родитель, но под новое поведение надо дополнять/переопределять метод, который ранее вынесли как общий.  
Но, мы можем так же воспользоваться приемом, который вынесет схожее поведение в отдельный класс - это паттерн Visitor.  
Visitor позволяет нам сохранять принцип единой ответственности, когда имеется несколько классов с очень схожим поведением  
Visitor скорее поведение отделенное от данных, то есть в названии есть глагол  
### Пример
Если рассматривать приземленные пример - это размещение на экране подробного вида(drill down)  
данных по различным типам объектов.   

Экран для размещения один и тот же, некий DescriptionView  
А объекты которые мы на него подает, имеют разный набор полей.   
Это может быть заявка на настройку контекстной рекламы, 
с полями: бюджет, сроки, платформа  
А может быть заявка на ремонт: бюджет, сроки, тип ремонта(капитальный, косметический), тип дома  
Если первые два поля совпадают, то остальные уже нет, пытаться натянуть некий общий выбор - тип платформы для рекламы/тип ремонта - усложняет.

Тут нам поможет Visitor, с методом visit(), который содержит реализацию для размещения конкретного
набора полей на view - без размещения лишних полей.  
Ниже псевдокод.  
Без использования Visitor
```
/**
* Экран подробной информации о товаре.
**/ 
class DescriptionView
	
	void showItem(){
		item.showOn(this);
	}
	
/**
* Заказ.
*/
class Order()
	showOn(View view)	
		// показ сроков и бюджета заказа
				
/**
* Заказ маркетинговой кампании.
*/ 				
class MargetingOrder

	@Override
	showOn(View view)
		// показывать только набор полей для маркетинга, с требуемой обработкой
/**
* Заказ ремонта в квартире.
*/	
class RenovationOrder

	@Override
	showOn(View view)
		// показывать только набор полей для заказа о ремонте, с требуемой обработкой

```  
Применили Visitor
```
/**
* Экран подробной информации о товаре.
**/ 
class DescriptionView
	
	void showItem(){
		item.showOn(this);
	}
	
/**
* Visitor для размещения заказа на форме.
**/	
class Visitor 
	
	visit(View view, MargetingOrder item)
		// показывать только набор полей для маркетинга, с требуемой обработкой
	
	
	visit(View view, RenovationOrder item)
		// показывать только набор полей для заказа о ремонте, с требуемой обработкой
				
				
/**
* Заказ маркетинговой кампании.
*/ 				
class MargetingOrder
	private Visitor visitor;

	showOn(View view)
		visitor.visit(view, item)
/**
* Заказ ремонта в квартире.
*/	
class RenovationOrder
	private Visitor visitor;

	showOn(View view)
		visitor.visit(view, item)
 ```