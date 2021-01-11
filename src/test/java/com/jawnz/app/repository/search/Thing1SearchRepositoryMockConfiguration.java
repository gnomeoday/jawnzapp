package com.jawnz.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Thing1SearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Thing1SearchRepositoryMockConfiguration {

    @MockBean
    private Thing1SearchRepository mockThing1SearchRepository;

}
