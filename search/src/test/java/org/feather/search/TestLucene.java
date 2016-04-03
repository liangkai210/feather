package org.feather.search;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.feather.search.index.LuceneIndex;
import org.junit.Before;
import org.junit.Test;

public class TestLucene {

	LuceneIndex index = null;

	@Before
	public void createIndex() {
		index = new LuceneIndex("/Users/dongdong/Workspaces/index");
	}

	@Test
	public void testMultiSearch() throws ParseException, IOException {
		for (int i = 0; i < 1; i++) {
			search("title", "dodge");
		}
	}

	private void search(String field, String queryString) throws ParseException, IOException {
		IndexSearcher searcher = index.getSearcher();
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, 15);
		displayDocs(topDocs);
		index.releaseSearcher(searcher);
	}

	private void displayDocs(TopDocs topDocs) throws IOException {
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = index.getSearcher().doc(scoreDoc.doc);
			System.out.println(document.get("title"));
		}
	}

}
