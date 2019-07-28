/*
 *  Copyright 2018 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.theopendle.aem.wknd.core.filters;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(AemContextExtension.class)
class LoggingFilterTest {

    private final LoggingFilter fixture = new LoggingFilter();

    private final TestLogger logger = TestLoggerFactory.getTestLogger(fixture.getClass());

    @BeforeEach
    void setup() {
        TestLoggerFactory.clear();
    }


    @Test
    void doFilter(final AemContext context) throws IOException, ServletException {
        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();

        final MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
        requestPathInfo.setResourcePath("/content/test");
        requestPathInfo.setSelectorString("selectors");

        fixture.init(mock(FilterConfig.class));
        fixture.doFilter(request, response, mock(FilterChain.class));
        fixture.destroy();

        final List<LoggingEvent> events = logger.getLoggingEvents();
        assertEquals(1, events.size());
        final LoggingEvent event = events.get(0);
        assertEquals(Level.DEBUG, event.getLevel());
        assertEquals(2, event.getArguments().size());
        assertEquals("/content/test", event.getArguments().get(0));
        assertEquals("selectors", event.getArguments().get(1));
    }
}
