package org.feather.implement.craigslist;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.feather.crawler.Auto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler {

	public boolean isFinish = false;
	public int count = 0;

	CarIndex carIndex = new CarIndex();

	static Logger logger = LoggerFactory.getLogger(Crawler.class);

	public static String url = "https://montgomery.craigslist.org/";

	public List<String> getNearCitys(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		Element nearBlock = doc.select(".acitem").first();
		Elements cityNearLinks = nearBlock.select("a[href]");
		return getAbsHref(cityNearLinks);
	}

	private List<String> getAbsHref(Elements links) {
		List<String> result = new ArrayList<String>();
		for (Element link : links)
			result.add(link.attr("abs:href"));
		return result;
	}

	public List<String> getAutoUrls(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return Collections.emptyList();
		}
		Elements autoUrls = doc.select(".content > p > a[href]");
		return getAbsHref(autoUrls);
	}

	private String getFirstContent(Elements elements) {
		if (elements != null && elements.size() > 0)
			return elements.first().text();
		else
			return null;
	}

	private Auto getAutoDetailedInfo(String url) {
		Auto info = new Auto();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}

		info.setTitle(getFirstContent(doc.select("#titletextonly")));
		info.setDescription(getFirstContent(doc.select("#postingbody")));

		String price = getFirstContent(doc.select(".postingtitletext > .price"));
		info.setPrice(getPrice(price));

		Elements carInfo = doc.select(".attrgroup");
		info.setAutoName(getFirstContent(carInfo.first().select("span > b")));
		info.setAutoInfo(getInfoMap(carInfo.get(1).select("span")));

		Elements time = doc.select(".postinginfo > time");
		String postTime = time.get(1).attr("datetime");
		info.setPostedTime(getTime(postTime));
		if (time.size() > 2) {
			String updateTime = time.get(2).attr("datetime");
			info.setUpdatedTime(getTime(updateTime));
		}

		return info;
	}

	private double getPrice(String price) {
		if (price == null || price.length() == 0)
			return 0d;
		return price.startsWith("$") ? Double.parseDouble(price.substring(1))
				: Double.parseDouble(price);
	}

	private Map<String, String> getInfoMap(Elements carDetails) {
		Map<String, String> carInfos = new HashMap<String, String>();
		for (Element detail : carDetails) {
			String[] pair = detail.text().split(":");
			String key = pair[0];
			String value = pair[1];
			carInfos.put(key, value);
		}
		return carInfos;
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private long getTime(String source) {
		source = source.replace("T", " ");
		source = source.replace("-0500", "");
		try {
			return sdf.parse(source).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
	}

	private String goToNextPageUrl(String url, int i, int count) {
		return url + "?s=" + i * count;
	}

	private String goToNextPageUrl(String url, int i) {
		return goToNextPageUrl(url, i, 100);
	}

	private String getAutoList(String url) {
		return url + "search/cta";
	}

	public void run() {
		run(url);
	}

	public void run(String url) {
		Crawler cc = new Crawler();
		List<String> cityUrls = cc.getNearCitys(url);
		for (String cityUrl : cityUrls) {
			String cars = getAutoList(cityUrl);
			for (int i = 0; i < 100; i++) {
				String nextList = goToNextPageUrl(cars, i);
				List<String> pages = getAutoUrls(nextList);
				if (pages.isEmpty()) {
					break;
				}
				for (String page : pages) {
					Auto info = getAutoDetailedInfo(page);
					if (info == null)
						continue;
					count++;
					carIndex.addDocument(info);
					System.out.println(count + ": " + info);
				}
			}

		}
		isFinish = true;
	}

}
