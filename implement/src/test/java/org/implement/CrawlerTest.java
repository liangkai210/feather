package org.implement;

import org.feather.implement.craigslist.Crawler;
import org.junit.Before;
import org.junit.Test;

public class CrawlerTest {

	Crawler crawler = null;

	@Before
	public void init() {
		crawler = new Crawler();
	}

	@Test
	public void test() {
		crawler.run();
	}

}
