package mtd.manager.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeploymentUpdateVO extends DeploymentVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
