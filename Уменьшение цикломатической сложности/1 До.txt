	// До 
	// Сложность 11
	public String setTopSkipUrl(String urlPath, int topOnPage, int skipParameter) {
		String[] splittedUrl = urlPath.split("");
		logger.trace(urlPath);
		urlPath = "";
		for (String s : splittedUrl){
			if(Character.UnicodeBlock.of(s.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)){
				try{
					urlPath += URLEncoder.encode(s, "UTF-8");
				} catch (UnsupportedEncodingException e){
					logger.error(e.getMessage() == null ? "" : e.getMessage(), e.getCause());
				}
			} else{
				urlPath += s;
			}
		}
		if(urlPath.split("(?<=\\$skip=)\\d+").length > 1){
			urlPath = urlPath.split("(?<=\\$skip=)\\d+")[0] + String.valueOf(skipParameter)
					+ urlPath.split("(?<=\\$skip=)\\d+")[1];
		}
		if(urlPath.split("\\?").length > 1){
			if(!urlPath.contains("$top")){
				urlPath = urlPath.split("\\?")[0] + "?$top=" + String.valueOf(topOnPage) + "&"
						+ urlPath.split("\\?")[1];
			}
			if(!urlPath.contains("$skip")){
				urlPath = urlPath.split("\\?")[0] + "?$skip=" + String.valueOf(skipParameter) + "&"
						+ urlPath.split("\\?")[1];
			}
		} else{
			urlPath = urlPath + "?$skip=" + String.valueOf(skipParameter) + "&" + "$top="
					+ String.valueOf(topOnPage);
		}
		return urlPath.replaceAll(" ", "%20");
	}