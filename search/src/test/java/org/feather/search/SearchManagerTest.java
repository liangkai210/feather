package org.feather.search;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

public class SearchManagerTest {

	SearcherManager searcherManager = null;
	Directory directory = null;
	String indexPath = "/Users/dongdong/Workspaces/index";

	@Before
	public void init() throws IOException {
		directory = FSDirectory.open(Paths.get(indexPath));
		searcherManager = new SearcherManager(directory, new SearcherFactory());
	}

	@Test
	public void testSearch() throws IOException {
		IndexSearcher searcher = searcherManager.acquire();
		searcherManager.maybeRefresh();
		try {
		} finally {
			searcherManager.release(searcher);
		}
	}

}
