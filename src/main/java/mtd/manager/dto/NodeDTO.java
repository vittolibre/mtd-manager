package mtd.manager.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class NodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String hostname;

    private String ipAddress;

    private Long id;

    private String role;
    private Boolean available;

}
