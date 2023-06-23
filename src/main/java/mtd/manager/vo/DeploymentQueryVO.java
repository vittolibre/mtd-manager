package mtd.manager.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class DeploymentQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String strategy;

    private String namespace;
    private Long id;

}
