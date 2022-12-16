	// После 
	// Сложность 2
	// Применил: избавление от else, вынес циклы в метод, вынес из цикла if в метод, если он не используется для прерывания цикла.
	// Замена цикла forEach.
	private static Property ParseComplex(List<Element> elements, String name, Element i) {
		boolean isCollection = isCollection(i);
		if(isCollection){
			return parseCollectionOfComplex(elements, name, i);
		}
		return parseSimpleComplex(elements, name);
	}

	/**
	 * Проверяет, что рассматриваемый элемент является коллецией.
	 */
	private static boolean isCollection(Element i) {
		return (i.attributeValue("type") != null) && i.attributeValue("type").contains("Collection(");
	}

	/**
	 * Парсинг коллекции комплексных типов.
	 */
	private static Property parseCollectionOfComplex(List<Element> elements, String name, Element i) {
		List<Object> listOfGenerally = new ArrayList<Object>();
		for (Element element : elements){
			listOfGenerally = nextFillOfSubnodes(listOfGenerally, element);
		}
		Property property = new Property();
		property.setName(name);
		boolean isComplex = isComplex(i);
		property.setComplex(isComplex);
		property.setCollection(true);
		property.setValue(listOfGenerally);
		return property;
	}

	/**
	 * Возвращает новый список с добавление потомков переданного элемента.
	 */
	private static List<Object> nextFillOfSubnodes(List<Object> listOfGenerally, Element element) {
		List<Object> nextListOfGenerally = new ArrayList<>(listOfGenerally);
		if(!isEmptySubnodes(element)){
			List<Property> listOfProperties = transformElementsToProperties(element.elements());
			Complex ct = new Complex();
			ct.setListOfProperties(listOfProperties);
			nextListOfGenerally.add(ct);
			return nextListOfGenerally;
		}
		nextListOfGenerally.add(parseProperty(element));
		return nextListOfGenerally;
	}

	/**
	 * Проверяет отсутствие потомков у узла.
	 */
	private static boolean isEmptySubnodes(Element element) {
		return element.elements().isEmpty();
	}

	/**
	 * Проверяет является ли элемент комплексным.
	 */
	private static boolean isComplex(Element i) {
		return i.attributeValue("type").contains("Edm");
	}

	/**
	 * Парсинг простого Complex.
	 */
	private static Property parseSimpleComplex(List<Element> elements, String name) {
		Complex complex = new Complex();
		List<Property> listOfProperties = transformElementsToProperties(elements);
		complex.setListOfProperties(listOfProperties);
		Property property = new Property();
		property.setName(name);
		property.setComplex(true);
		property.setCollection(false);
		property.setValue(complex);
		return property;
	}

	/**
	 * Преобразование списка Element в список Property.
	 */
	private static List<Property> transformElementsToProperties(List<Element> elements) {
		List<Property> listOfProperties = new ArrayList<Property>();
		elements.stream().forEach(element ->{
			Property et = new Property();
			et.setName(element.getName());
			et.setValue(element.getStringValue());
			listOfProperties.add(et);
		});
		return listOfProperties;
	}
