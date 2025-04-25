package com.transformer.es.helper;

import com.transformer.helper.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @author ouliyuan 2023/8/30
 */
@Slf4j
public class QueryHelper {

    private static final NamedXContentRegistry namedXContentRegistry;

    static {
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        namedXContentRegistry = new NamedXContentRegistry(searchModule.getNamedXContents());
    }

    public static TermQueryBuilder term(String name, String value) {
        return new TermQueryBuilder(name, value);
    }

    public static MatchPhraseQueryBuilder matchPhrase(String name, String value) {
        return new MatchPhraseQueryBuilder(name, value);
    }

    public static RangeQueryBuilder dateEquals(String name, Date date) {
        return dateRange(name, date, date);
    }

    public static RangeQueryBuilder dateRange(String name, Date startTime, Date endTime) {
        return range(name, DateHelper.formatDate(startTime), DateHelper.formatDate(endTime));
    }

    public static RangeQueryBuilder dateTimeRange(String name, Date startTime, Date endTime) {
        return range(name, DateHelper.formatTime(startTime), DateHelper.formatTime(endTime));
    }

    public static RangeQueryBuilder range(String name, String from, String to) {
        RangeQueryBuilder range = new RangeQueryBuilder(name);
        range.gte(from);
        range.lte(to);
        return range;
    }

    public static TermsQueryBuilder terms(String name, List<String> values) {
        return new TermsQueryBuilder(name, values);
    }

    public static TermsQueryBuilder termsInteger(String name, List<Integer> values) {
        return new TermsQueryBuilder(name, values);
    }

    public static BoolQueryBuilder should(Map<String, List<String>> fieldMap) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        fieldMap.forEach((key, value) -> boolQueryBuilder.should(terms(key, value)));

        return boolQueryBuilder;
    }

    public static ExistsQueryBuilder exist(String name) {
        return new ExistsQueryBuilder(name);
    }

    @Deprecated
    public static SearchSourceBuilder parseBuilder(String json) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        XContentParser parser = XContentFactory.xContent(XContentType.JSON).createParser(namedXContentRegistry, DeprecationHandler.THROW_UNSUPPORTED_OPERATION, json);
        builder.parseXContent(parser);
        return builder;
    }

    public static QueryBuilder buildKeywordLogicQuery(String name, String keyword) {
        String key = StringUtils.trim(keyword);
        if (StringUtils.isBlank(key)) {
            return null;
        }

        if (StringUtils.contains(key, "&&")) {
            BoolQueryBuilder query = QueryBuilders.boolQuery();

            String[] musts = StringUtils.split(key, "&&");
            Arrays.stream(musts).forEach(e -> {
                QueryBuilder must = getMustQueryBuilder(name, e);
                if (must != null) {
                    query.must(must);
                }
            });

            return query;
        }
        return getMustQueryBuilder(name, key);
    }

    private static QueryBuilder getMustQueryBuilder(String name, String keyword) {
        QueryBuilder build = getShouldQueryBuilder(name, keyword);
        if (build == null) {
            build = getConditionQueryBuilder(name, StringUtils.trim(keyword));
        }
        return build;
    }

    private static BoolQueryBuilder getShouldQueryBuilder(String name, String keyword) {
        String key = StringUtils.trim(keyword);
        if (StringUtils.isBlank(key)) {
            return null;
        }

        if (StringUtils.contains(key, "||")) {
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            String[] orConditions = StringUtils.split(key, "||");
            Arrays.stream(orConditions).forEach(e -> {
                QueryBuilder builder = getConditionQueryBuilder(name, e);
                if (builder != null) {
                    query.should(builder);
                }
            });
            return query;
        }
        return null;
    }

    private static QueryBuilder getConditionQueryBuilder(String name, String keyword) {
        if (keyword.startsWith("!")) {
            return getNotMatchQueryBuilder(name, keyword.substring("!".length()));
        } else {
            return getMatchQueryBuilder(name, StringUtils.trim(keyword));
        }
    }

    private static QueryBuilder getMatchQueryBuilder(String name, String keyword) {
        String key = StringUtils.trim(keyword);
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return QueryHelper.matchPhrase(name, key);
    }

    private static QueryBuilder getNotMatchQueryBuilder(String name, String keyword) {
        String key = StringUtils.trim(keyword);
        if (StringUtils.isBlank(key)) {
            return null;
        }
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.mustNot(getMatchQueryBuilder(name, key));
        return query;
    }
}
