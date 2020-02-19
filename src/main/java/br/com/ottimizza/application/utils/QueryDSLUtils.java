package br.com.ottimizza.application.utils;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class QueryDSLUtils {

    public static BooleanTemplate unnacent(StringPath path, String value) {
        return Expressions.booleanTemplate(
            "upper(unaccent({0})) like unaccent({1})", path, value.toUpperCase()
        );
    }

}