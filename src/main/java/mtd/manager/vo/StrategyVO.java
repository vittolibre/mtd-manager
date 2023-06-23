package mtd.manager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class StrategyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "name can not null")
    private String name;

    @NotNull(message = "enabled can not null")
    private Boolean enabled;

    @NotNull(message = "scheduling can not null")
    private String scheduling;

    @NotNull(message = "id can not null")
    private Long id;

}
