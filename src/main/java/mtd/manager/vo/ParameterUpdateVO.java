package mtd.manager.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ParameterUpdateVO extends ParameterVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
