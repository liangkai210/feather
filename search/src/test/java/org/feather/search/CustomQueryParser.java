package org.feather.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;

public class CustomQueryParser extends QueryParser {

	public CustomQueryParser(String f, Analyzer a) {
		super(f, a);
	}

	@Override
	protected org.apache.lucene.search.Query getWildcardQuery(String field, String termStr)
			throws ParseException {
		// return super.getWildcardQuery(field, termStr);
		throw new ParseException("由于性能原因，禁止通配符查询");
	}

	@Override
	protected org.apache.lucene.search.Query getFuzzyQuery(String field, String termStr,
			float minSimilarity) throws ParseException {
		// return super.getFuzzyQuery(field, termStr, minSimilarity);
		throw new ParseException("由于性能原因，禁止模糊查询");
	}

	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field, String part1, String part2,
			boolean startInclusive, boolean endInclusive) throws ParseException {
		// TODO Auto-generated method stub
		if (field.contains("time"))
			return NumericRangeQuery.newLongRange(field, Long.parseLong(part1),
					Long.parseLong(part2), startInclusive, endInclusive);
		else if (field.contains("price"))
			return NumericRangeQuery.newDoubleRange(field, Double.parseDouble(part1),
					Double.parseDouble(part2), startInclusive, endInclusive);
		return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
	}

}
