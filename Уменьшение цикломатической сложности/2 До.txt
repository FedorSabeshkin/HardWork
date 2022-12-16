// До
// Сложность: 9
public Feed readXML(String urlPath, String connectionName, TopOption top, SkipOption skip) {
		Parser parser = new Parser();
		Feed feed = new Feed();
		try{
			Document document;
			Element feedNode;
			Destination destination = ReadWriteDestInfo.readFromFile().getDestinationByName(connectionName);
			String updatedUrlPath = setTopSkipUrl(urlPath, top, skip);
			while(updatedUrlPath.contains(",,")){
				updatedUrlPath = updatedUrlPath.replaceAll(",,", ",");
			}
			if(updatedUrlPath.endsWith(",")){
				updatedUrlPath = updatedUrlPath.substring(0, updatedUrlPath.length() - 1);
			}
			urlPath = updatedUrlPath;
			logger.trace("top  = " + top + " skip  = " + skip + " url = " + urlPath);
			String url = destination.getUrl();
			String[] splittedUrl = urlPath.split("");
			String responseURL = "";
			for (String s : splittedUrl){
				if(Character.UnicodeBlock.of(s.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)){
					responseURL += URLEncoder.encode(s, "UTF-8");
				} else{
					responseURL += s;
				}
			}
			responseURL = responseURL.replaceAll(" ", "%20");
			URL fullurl = new URL(url + responseURL);
			String username = destination.getUser();
			String password = destination.getPassword();
			Authenticator.setDefault(new MyAuthenticator(username, password));
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			document = parser.parse(fullurl);
			feedNode = document.getRootElement();
			for (Element i : feedNode.elements()){
				if(i.getName() == "entry"){
					feed.AddEntry(ParseEntry(i));
				}
			}
		} catch (Exception e){
			logger.error(e.getMessage() == null ? "" : e.getMessage(), e.getCause());
		}
		return feed;
	}