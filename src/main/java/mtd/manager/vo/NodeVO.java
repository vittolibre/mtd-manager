package mtd.manager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class NodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "hostname can not null")
    private String hostname;

    @NotNull(message = "ipAddress can not null")
    private String ipAddress;

    @NotNull(message = "id can not null")
    private Long id;

    private String role;
    private Boolean available;

}
