package mtd.manager.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class NodeLabelQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String key;

    private String value;

    private Long idNode;

}
