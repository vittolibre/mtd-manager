package mtd.manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "node_label", schema = "mtdmanager")
public class NodeLabel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
//    @ManyToOne
//    @JoinColumn(name = "id_node")
//    private Node node;
    @Column(name = "id_node")
    private Long idNode;
}