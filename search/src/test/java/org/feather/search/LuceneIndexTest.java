package org.feather.search;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LuceneIndexTest {

	LuceneIndex index;

	@Before
	public void init() {
		try {
			index = new LuceneIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetSearch() {
		Assert.assertNotNull(index.getIndexSearcher());
		index.close();
	}

	@Test
	public void testGetReader() {
		Assert.assertNotNull(index.getIndexWriter());
		index.close();
	}

}
