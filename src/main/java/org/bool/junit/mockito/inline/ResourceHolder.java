package org.bool.junit.mockito.inline;

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

/**
 * {@link CloseableResource} adapter for {@link AutoCloseable}
 * resource.
 * 
 * <pre><code lang="spock">
 * def "closes AutoCloseable"() {
 *   given:
 *     def resource = Mock(AutoCloseable)
 *     def holder = new ResourceHolder(resource)
 *   when:
 *     holder.close()
 *   then:
 *     1 * resource.close()
 * }
 * 
 * def "no interaction with non-closeable"() {
 *   given:
 *     def resource = Mock(Object)
 *     def holder = new ResourceHolder(resource)
 *   when:
 *     holder.close()
 *   then:
 *     0 * resource._
 * }
 * </code></pre>
 * 
 */
public class ResourceHolder implements CloseableResource {

    private final Object resource;

    public ResourceHolder(Object resource) {
        this.resource = resource;
    }

    public Object getResource() {
        return resource;
    }

    @Override
    public void close() throws Exception {
        if (resource instanceof AutoCloseable) {
            ((AutoCloseable) resource).close();
        }
    }
}
