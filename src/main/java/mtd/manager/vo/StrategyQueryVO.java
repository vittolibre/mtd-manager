package mtd.manager.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class StrategyQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Boolean enabled;

    private String scheduling;

    private Long id;

}
