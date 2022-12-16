	// До 
	// Сложность 11
	private static Property parseComplex(List<Element> elements, String name, Element i) {
		boolean isCollection = (i.attributeValue("type") != null) && i.attributeValue("type").contains("Collection(");
		if(isCollection){
			List<Object> listOfGenerally = new ArrayList<Object>();
			for (Element element : elements){
				// либо добавляем ComplexType содержащий лист свойств
				if(!element.elements().isEmpty()){
					Complex ct = new Complex();
					List<Property> listOfProperties = new ArrayList<Property>();
					for (Element element2 : element.elements()){
						Property et = new Property();
						et.setName(element2.getName());
						et.setValue(element2.getStringValue());
						listOfProperties.add(et);
					}
					ct.setListOfProperties(listOfProperties);
					listOfGenerally.add(ct);
				}
				// либо пропертю со строковым значением
				else{
					listOfGenerally.add(ParseProperty(element));
				}
			}
			Property property = new Property();
			property.setName(name);
			if(i.attributeValue("type").contains("Edm")){
				property.setComplex(false);
			} else{
				property.setComplex(true);
			}
			property.setCollection(true);
			property.setValue(listOfGenerally);
			return property;
		}
		else{
			Complex complex = new Complex();
			List<Property> listOfProperties = new ArrayList<Property>();
			for (Element element : elements){
				Property et = new Property();
				et.setName(element.getName());
				et.setValue(element.getStringValue());
				listOfProperties.add(et);
			}
			complex.setListOfProperties(listOfProperties);
			Property property = new Property();
			property.setName(name);
			property.setComplex(true);
			property.setCollection(false);
			property.setValue(complex);
			return property;
		}
	}