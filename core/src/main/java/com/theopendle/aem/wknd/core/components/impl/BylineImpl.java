package com.theopendle.aem.wknd.core.components.impl;

import com.adobe.cq.wcm.core.components.models.Image;
import com.theopendle.aem.wknd.core.components.Byline;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {Byline.class}, // Make this implementation accessible from HTL via the Byline interface
        resourceType = {BylineImpl.RESOURCE_TYPE}, // See https://sling.apache.org/documentation/bundles/models.html#associating-a-model-class-with-a-resource-type-since-130 for info
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL // Saves us from having to use @Optional on injected fields
)
public class BylineImpl implements Byline {

    static final String RESOURCE_TYPE = "wknd/components/content/byline";

    // Dependency injection. The ModelFactory is an OSGi service, this class is not, so we use
    // @OSGiService. If this were also an OSGi service, we woulds use @Reference instead
    @OSGiService
    private ModelFactory modelFactory;

    @Self // Adapts the current Sling request to the type of the associated variable
    private SlingHttpServletRequest request;

    // Fetches the name property from the JCR. To fetch a property with a different name, use @Named("differentName")
    @ValueMapValue
    @Getter // Simple Lombok Getter to implement getName() from the interface
    private String name;

    @ValueMapValue
    private List<String> occupations;

    @Getter
    private Image image;

    @PostConstruct // Executes method after all dependency injection is complete
    private void init() {
        image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
    }

    public List<String> getOccupations() {
        if (occupations != null) {
            Collections.sort(occupations);
            return occupations;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isEmpty() {
        // Name is missing, but required
        if (StringUtils.isBlank(name)) {
            return true;
        }

        // At least one occupation is required
        if (occupations == null || occupations.isEmpty()) {

            return true;
        }

        // A valid image is required
        if (getImage() == null || StringUtils.isBlank(getImage().getSrc())) {
            return true;
        }

        // Everything is populated, so this component is not considered empty
        return false;
    }
}
