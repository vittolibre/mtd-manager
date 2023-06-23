package mtd.manager.entity;

import java.util.List;

/**
 * A Projection for the {@link Node} entity
 */
public interface NodeInfo {
    Long getId();

    String getHostname();

    String getIpAddress();

    String getRole();

    List<NodeLabelInfo> getNodeLabels();

    /**
     * A Projection for the {@link NodeLabel} entity
     */
    interface NodeLabelInfo {
        Integer getId();

        String getKey();

        String getValue();
    }
}