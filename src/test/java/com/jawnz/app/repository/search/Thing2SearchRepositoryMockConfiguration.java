package com.jawnz.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Thing2SearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Thing2SearchRepositoryMockConfiguration {

    @MockBean
    private Thing2SearchRepository mockThing2SearchRepository;

}
