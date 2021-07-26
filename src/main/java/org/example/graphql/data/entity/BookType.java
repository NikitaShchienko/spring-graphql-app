package org.example.graphql.data.entity;

import io.leangen.graphql.annotations.GraphQLEnumValue;
import io.leangen.graphql.annotations.types.GraphQLType;

@GraphQLType(name = "BookType", description = "Book type")
public enum BookType {

    @GraphQLEnumValue(name = "Novel", description = "Novel") NOVEL
}
