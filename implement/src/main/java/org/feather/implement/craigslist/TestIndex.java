package org.feather.implement.craigslist;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.feather.crawler.Auto;
import org.feather.crawler.CraiglistCrawler;
import org.junit.Before;
import org.junit.Test;

public class TestIndex {

	String indexPath = "/Users/dongdong/Workspaces/index";

	Directory directory = null;
	IndexWriter writer = null;
	IndexSearcher searcher = null;

	@Before
	public void init() {
		try {
			createDirectory();
			createIndexWriter();
			createIndexSearcher();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirectory() throws IOException {
		directory = FSDirectory.open(Paths.get(indexPath));
	}

	private void createIndexWriter() throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(directory, config);
	}

	private void createIndexSearcher() throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
	}

	private int flushIndex = 0;

	@Test
	public void createIndex() throws IOException, InterruptedException {
		CraiglistCrawler craiglistCrawler = new CraiglistCrawler();
		Thread thread = new Thread(craiglistCrawler);
		thread.start();
		while (craiglistCrawler.isFinish == false) {
			if (craiglistCrawler.autos.isEmpty()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			writer.addDocument(createDocument(craiglistCrawler.autos.poll()));
			bulkWrite();
			System.out.print("--- " + craiglistCrawler.count + " ---");
			if (craiglistCrawler.count >= 10) {
				thread.join();
				craiglistCrawler.isFinish = true;
			}
		}
	}

	private void bulkWrite() throws IOException {
		if (flushIndex >= 50) {
			writer.flush();
			writer.commit();
			flushIndex = 0;
		}
	}

	private Document createDocument(Auto info) {
		flushIndex++;
		Document document = new Document();
		document.add(new TextField("title", info.getTitle(), Store.YES));
		document.add(new TextField("description", info.getDescription(), Store.YES));
		document.add(new TextField("carName", info.getAutoName(), Store.YES));
		document.add(new LongField("postedTime", info.getPostedTime(), Store.YES));
		document.add(new LongField("updatedTime", info.getUpdatedTime(), Store.YES));
		document.add(new DoubleField("price", info.getPrice(), Store.YES));
		for (Map.Entry<String, String> entry : info.getAutoInfo().entrySet())
			document.add(new TextField(entry.getKey(), entry.getValue(), Store.YES));
		return document;
	}

	@Test
	public void testSearcher() throws ParseException, IOException {
		String queryString = "Sedan";
		QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, 15);
		displayDocs(topDocs);
	}

	@Test
	public void testSearcherSort() throws ParseException, IOException {
		String queryString = "Sedan";
		QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, 15);
		displayDocs(topDocs);
	}

	private void displayDocs(TopDocs topDocs) throws IOException {
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);
			System.out.println(document.get("title"));
		}
	}

	public void testFilter() {
	}
}
