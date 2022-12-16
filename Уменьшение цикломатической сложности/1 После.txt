	// После 
	// Сложность 2
	// Применил: а) избавление от else 
	// 			б) вынес из цикла if в метод, если он не используется для прерывания цикла.
	public String setTopSkipUrl(String urlPath, int topOnPage, int skipParameter) {
		String[] splittedUrl = urlPath.split("");
		logger.trace(urlPath);
		urlPath = cyrillicToUnicode(urlPath, splittedUrl);
		boolean isExistSkipParam = urlPath.split("(?<=\\$skip=)\\d+").length > 1;
		if(isExistSkipParam){
			urlPath = iterateSkipValue(urlPath, skipParameter);
		}
		urlPath = addTopSkipToQuery(urlPath, topOnPage, skipParameter);
		return urlPath.replaceAll(" ", "%20");
	}

	/**
	 * Увеличение значения skip.
	 */
	private String iterateSkipValue(String urlPath, int skipParameter) {
		urlPath = urlPath.split("(?<=\\$skip=)\\d+")[0] + String.valueOf(skipParameter)
				+ urlPath.split("(?<=\\$skip=)\\d+")[1];
		return urlPath;
	}

	/**
	 * Добавление top и skip к пути.
	 */
	private String addTopSkipToQuery(String urlPath, int topOnPage, int skipParameter) {
		boolean isExistQueryParams = urlPath.split("\\?").length > 1;
		if(isExistQueryParams){
			return addTopSkipToExistedQuery(urlPath, topOnPage, skipParameter);
		}
		return addTopSkipToEmptyQuery(urlPath, topOnPage, skipParameter);
	}

	/**
	 * Добавление top и skip к пути без дополнительных аргументов.
	 */
	private String addTopSkipToEmptyQuery(String urlPath, int topOnPage, int skipParameter) {
		urlPath = urlPath + "?$skip=" + String.valueOf(skipParameter) + "&" + "$top="
				+ String.valueOf(topOnPage);
		return urlPath;
	}

	/**
	 * Добавление top и skip к существующему списку аргументов
	 */
	private String addTopSkipToExistedQuery(String urlPath, int topOnPage, int skipParameter) {
		if(!urlPath.contains("$top")){
			urlPath = urlPath.split("\\?")[0] + "?$top=" + String.valueOf(topOnPage) + "&"
					+ urlPath.split("\\?")[1];
		}
		if(!urlPath.contains("$skip")){
			urlPath = urlPath.split("\\?")[0] + "?$skip=" + String.valueOf(skipParameter) + "&"
					+ urlPath.split("\\?")[1];
		}
		return urlPath;
	}

	/**
	 * Переводит символы кириллицы в unicode.
	 */
	private String cyrillicToUnicode(String urlPath, String[] splittedUrl) {
		for (String s : splittedUrl){
			urlPath = replaceCyrillicSymbol(urlPath, s);
		}
		return urlPath;
	}

	/**
	 * Заменяет кириллический символ на символ юникода и добавляет к строке.
	 */
	private String replaceCyrillicSymbol(String urlPath, String s) {
		boolean isUnicodeSymbol = Character.UnicodeBlock.of(s.charAt(0))
				.equals(Character.UnicodeBlock.CYRILLIC);
		if(isUnicodeSymbol){
			return addUnicodeSymbol(urlPath, s);
		}
		return urlPath + s;
	}

	/**
	 * Добавление символа в юникоде.
	 */
	private String addUnicodeSymbol(String urlPath, String s) {
		try{
			return urlPath += URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e){
			logger.error(e.getMessage() == null ? "" : e.getMessage(), e.getCause());
		}
		return urlPath;
	}