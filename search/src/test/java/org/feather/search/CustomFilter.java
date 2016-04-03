package org.feather.search;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class CustomFilter extends TokenFilter {

	protected CustomFilter(TokenStream input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	public void test() {
		
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
