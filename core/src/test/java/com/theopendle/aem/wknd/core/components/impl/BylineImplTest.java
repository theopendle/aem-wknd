package com.theopendle.aem.wknd.core.components.impl;

import com.adobe.cq.wcm.core.components.models.Image;
import com.google.common.collect.ImmutableList;
import com.theopendle.aem.wknd.core.TestUtil;
import com.theopendle.aem.wknd.core.components.Byline;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Constants;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BylineImplTest {

    @Mock
    private Image image;

    @Mock
    private ModelFactory modelFactory;

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(BylineImpl.class);
        ctx.load().json(TestUtil.getJsonTestResourcePath(this.getClass()), "/content");

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
                .thenReturn(image);

        ctx.registerService(ModelFactory.class, modelFactory, Constants.SERVICE_RANKING, Integer.MAX_VALUE);
    }

    @Test
    void test_testGetName() {
        final String expected = "Jane Doe";

        ctx.currentResource("/content/byline");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertEquals(expected, byline.getName());
    }

    @Test
    void test_getOccupations_valid() {
        List<String> expected = new ImmutableList.Builder<String>()
                .add("Blogger")
                .add("Photographer")
                .add("YouTuber")
                .build();

        ctx.currentResource("/content/byline");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertEquals(expected, byline.getOccupations());
    }

    @Test
    void test_getOccupations_noOccupations() {
        List<String> expected = Collections.emptyList();

        ctx.currentResource("/content/empty");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertEquals(expected, byline.getOccupations());
    }

    @Test
    void test_isEmpty_noImage() {
        ctx.currentResource("/content/byline");

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()),
                any(Resource.class),
                eq(Image.class))).thenReturn(null);

        Byline byline = ctx.request().adaptTo(Byline.class);
        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_noName() {
        ctx.currentResource("/content/no-name");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_noOccupation() {
        ctx.currentResource("/content/no-occupation");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_emptyOccupation() {
        ctx.currentResource("/content/empty-occupation");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_noImageSrc() {
        ctx.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("");

        Byline byline = ctx.request().adaptTo(Byline.class);
        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_empty() {
        ctx.currentResource("/content/empty");
        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertTrue(byline.isEmpty());
    }

    @Test
    void test_isEmpty_notEmpty() {
        ctx.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("/image.png");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertNotNull(byline);
        assertFalse(byline.isEmpty());
    }
}