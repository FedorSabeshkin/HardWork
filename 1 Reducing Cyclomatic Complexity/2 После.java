	// После 
	// Сложность 2
	// Применил: избавление от else, вынес циклы в метод, вынес из цикла if в метод, если он не используется для прерывания цикла.
	// Замена цикла forEach.
	public Feed readXML(String urlPath, String connectionName, TopOption top, SkipOption skip) {
		Parser parser = new Parser();
		Feed feed = new Feed();
		try{
			Document document;
			Element feedNode;
			Destination destination = ReadWriteDestInfo.readFromFile().getDestinationByName(connectionName);
			URL fullurl = performRequestUrl(urlPath, top, skip, destination);
			String username = destination.getUser();
			String password = destination.getPassword();
			Authenticator.setDefault(new MyAuthenticator(username, password));
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			document = parser.parse(fullurl);
			feedNode = document.getRootElement();
			parseEnrties(feed, feedNode);
		} catch (Exception e){
			logger.error(e.getMessage() == null ? "" : e.getMessage(), e.getCause());
		}
		return feed;
	}
	
	/**
	 * Подготовка URL для запроса данных.
	 */
	private URL performRequestUrl(String urlPath, TopOption top, SkipOption skip, Destination destination)
			throws MalformedURLException {
		String updatedUrlPath = setTopSkipUrl(urlPath, top, skip);
		updatedUrlPath = removeExtraCommasFromUrl(updatedUrlPath);
		urlPath = updatedUrlPath;
		logger.trace("top  = " + top + " skip  = " + skip + " url = " + urlPath);
		String url = destination.getUrl();
		String[] splittedUrl = urlPath.split("");
		String responseURL = "";
		responseURL = cyrillicToUnicode(responseURL, splittedUrl);
		responseURL = responseURL.replaceAll(" ", "%20");
		URL fullurl = new URL(url + responseURL);
		return fullurl;
	}
	
	/**
	 * Удаление лишних запятых из url.
	 */
	private String removeExtraCommasFromUrl(String updatedUrlPath) {
		updatedUrlPath = updatedUrlPath.replaceAll(",,", ",");
		if(updatedUrlPath.endsWith(",")){
			updatedUrlPath = updatedUrlPath.substring(0, updatedUrlPath.length() - 1);
		}
		return updatedUrlPath;
	}

	private void parseEnrties(Feed feed, Element feedNode) {
		feedNode.elements().stream().forEach(element ->{
			attemptParseEntry(feed, element);
		});
	}
	
	/**
	 * Распарсить елемент, если он представляет собою Entry.
	 */
	private void attemptParseEntry(Feed feed, Element element) {
		boolean isEntry = element.getName() == "entry";
		if(isEntry){
			feed.AddEntry(ParseEntry(element));
		}
	}