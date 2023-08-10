package mtd.manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "node", schema = "mtdmanager")
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hostname", nullable = false)
    private String hostname;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "role")
    private String role;

    @Column(name = "type")
    private String type;

    @Column(name = "available")
    private Boolean available;

//    @OneToMany(mappedBy = "node")
//    private List<NodeLabel> nodeLabels = new ArrayList<>();

}
