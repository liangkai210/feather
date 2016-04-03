package org.feather.search.index;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.feather.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneIndex {

	protected Logger logger = LoggerFactory.getLogger(LuceneIndex.class);

	public LuceneIndex(String indexPath) {
		this(indexPath, new StandardAnalyzer());
	}

	public LuceneIndex(String indexPath, Analyzer analyzer) {
		init(indexPath, analyzer);
	}

	protected IndexWriter indexWriter;
	private Directory directory;
	private SearcherManager searcherManager;

	private void init(String indexPath, Analyzer analyzer) {
		try {
			directory = FSDirectory.open(Paths.get(indexPath));
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
			searcherManager = new SearcherManager(indexWriter, new SearcherFactory());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public IndexWriter getWriter() {
		return this.indexWriter;
	}

	public IndexSearcher getSearcher() {
		try {
			return searcherManager.acquire();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public void releaseSearcher(IndexSearcher indexSearcher) {
		if (indexSearcher == null)
			return;
		try {
			searcherManager.release(indexSearcher);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void commit() {
		try {
			indexWriter.flush();
			indexWriter.commit();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private int flushIndex = 0;

	public void addDocument(Document document) {
		try {
			indexWriter.addDocument(document);
			flushIndex++;
			bulkWrite();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void bulkWrite() throws IOException {
		if (flushIndex >= 50) {
			commit();
			flushIndex = 0;
		}
	}

	public void close() {
		FileUtil.close(indexWriter);
		FileUtil.close(directory);
		FileUtil.close(searcherManager);
	}

}
