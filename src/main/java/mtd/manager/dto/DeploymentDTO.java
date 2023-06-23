package mtd.manager.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class DeploymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    private Long strategy;

    private String namespace;
    private Long id;

}
