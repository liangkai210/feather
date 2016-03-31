package org.feather.search;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.feather.crawler.CraiglistCrawler;
import org.feather.crawler.DetailedInfo;
import org.junit.Before;
import org.junit.Test;

public class TestIndex {

	String indexPath = "/Users/liangkai/workspace/index";

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
	public void createIndex() throws IOException {
		CraiglistCrawler craiglistCrawler = new CraiglistCrawler();
		Thread thread = new Thread(craiglistCrawler);
		thread.start();

		while (craiglistCrawler.isFinish == false) {
			if (craiglistCrawler.infos.isEmpty()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			writer.addDocument(createDocument(craiglistCrawler.infos.poll()));
			bulkWrite();
		}
	}

	private void bulkWrite() throws IOException {
		if (flushIndex >= 50) {
			writer.flush();
			writer.commit();
			flushIndex = 0;
		}

	}

	public Document createDocument(DetailedInfo info) {
		flushIndex++;
		Document document = new Document();
		document.add(new TextField("title", info.getTitle(), Store.YES));
		document.add(new TextField("description", info.getDescription(), Store.YES));
		document.add(new TextField("carName", info.getCarName(), Store.YES));
		document.add(new LongField("postedTime", info.getPostedTime(), Store.YES));
		document.add(new LongField("updatedTime", info.getUpdatedTime(), Store.YES));
		document.add(new DoubleField("price", info.getPrice(), Store.YES));
		for (Map.Entry<String, String> entry : info.getCarInfo().entrySet())
			document.add(new TextField(entry.getKey(), entry.getValue(), Store.YES));
		return document;
	}

}
