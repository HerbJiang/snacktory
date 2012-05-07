package de.jetwick.snacktory;

import java.net.URLEncoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.json.*;

public class TestByHitHotData {
	String locale = "zh_TW";
	String category = "s";

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testIt() {
		String hrUrl = "http://hithot.cc/cloud_util/CloudJson_v2_HitRank.jsp?locale=" + locale + "&rt=a&cl=" + category;
		HtmlFetcher fetcher = new HtmlFetcher();
		fetcher.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.0.8) Gecko/2009032609 Firefox/3.0.8 (.NET CLR 3.5.30729)");
		long begin = System.currentTimeMillis();
		
		try {
			String data = fetcher.fetchAsString(hrUrl, 10000);
			//System.out.println("*" + data + "*");
			JSONArray rankArr = new JSONArray(data);
			
			System.out.println("Time cost:" + (System.currentTimeMillis() - begin) + "ms / Total count:" + rankArr.length());
			int counter = 0;
			for (int i=0; i < rankArr.length(); i++) {
				String keyword = rankArr.getJSONObject(i).getString("name");
				String newsUrl = "http://hithot.cc/cloud_util/MetroNews.jsp?locale=" + locale + "&fsi=true&q=" + URLEncoder.encode(keyword, "UTF-8");
				
				String result = fetcher.fetchAsString(newsUrl, 10000);
				JSONObject resultJson = new JSONObject(result);
				
				if (resultJson.has("results")) {
					JSONArray newsArr = resultJson.getJSONArray("results");
					for (int j=0; j < newsArr.length(); j++) {
						JSONObject no = newsArr.getJSONObject(j);
						String url = no.getString("nurl");
						String title = null;
						String imgUrl = null;
						//System.out.println(url);
						try{
							JResult rs = fetcher.fetchAndExtract(url, 10000, true);
							title = rs.getTitle();
							if (!rs.getImageUrl().isEmpty())
								imgUrl = rs.getImageUrl();
						}
						catch (Exception e2) {
							e2.printStackTrace();
						}
						
						if (imgUrl != null) {
							System.out.println("[" + url + "]" + "(" + title + ") => " + imgUrl);
						}
						else {
							System.out.println("[Failed]" + url);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
