package mtd.manager.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class NodeLabelDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String key;

    private String value;

    private Long idNode;

}
