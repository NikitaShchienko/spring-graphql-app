package org.example.graphql.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Component;

@GraphQLApi
@Component("graphql_GreetingGraphQlApi")
public class GreetingGraphQlApi {

    @GraphQLQuery(name = "greeting")
    public String getGreeting(@GraphQLArgument(name = "name", description = "Your name") final String name) {
        return "Hello " + name + "!";
    }
}
