package org.feather.search;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.feather.common.util.FileUtil;

public class LuceneIndex {

	private Directory directory;
	private DirectoryReader directoryReader;
	private IndexWriter indexWriter;
	private IndexSearcher indexSearcher;

	private static String defaultIndexPath = "/Users/dongdong/Workspaces/index";

	public LuceneIndex() throws IOException {
		this(defaultIndexPath);
	}

	public LuceneIndex(String indexPath) throws IOException {
		directory = FSDirectory.open(Paths.get(indexPath));
		createIndexWriter();
		createIndexSearcher();
	}

	private void createIndexWriter() throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		this.indexWriter = new IndexWriter(directory, config);
	}

	private void createIndexSearcher() throws IOException {
		directoryReader = DirectoryReader.open(directory);
		this.indexSearcher = new IndexSearcher(directoryReader);
	}

	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	public void close() {
		FileUtil.close(indexWriter);
		FileUtil.close(directoryReader);
		FileUtil.close(directory);
	}

}
