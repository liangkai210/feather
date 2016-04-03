package org.feather.search;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

public class ScoreTest {

	class DateScoreProvider extends CustomScoreProvider {

		public DateScoreProvider(LeafReaderContext context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			// TODO Auto-generated method stub
			return super.customScore(doc, subQueryScore, valSrcScore);
		}

		@Override
		public float customScore(int arg0, float arg1, float[] arg2) throws IOException {
			// TODO Auto-generated method stub
			return super.customScore(arg0, arg1, arg2);
		}

	}

	@Test
	public void test() throws IOException {
		Query query = new TermQuery(new Term("title", "toyota"));
		TopDocs topDocs = searcher.search(query, 15);
		displayDocs(topDocs);

	}

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

	private void displayDocs(TopDocs topDocs) throws IOException {
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);
			System.out.println(document.get("title"));
		}
	}
}
