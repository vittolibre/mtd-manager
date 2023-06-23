package mtd.manager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class DeploymentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "name can not null")
    private String name;

    @NotNull(message = "strategy can not null")
    private Long strategy;

    private String namespace;

}
