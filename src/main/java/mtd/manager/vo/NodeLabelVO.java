package mtd.manager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class NodeLabelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private Integer id;

    private String key;

    private String value;

    @NotNull(message = "idNode can not null")
    private Long idNode;

}
