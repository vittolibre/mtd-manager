package mtd.manager.repository;

import mtd.manager.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface NodeRepository extends JpaRepository<Node, String>, JpaSpecificationExecutor<Node> {

    @Query(nativeQuery = true, value = "SELECT *\n" +
            "FROM mtdmanager.node\n" +
            "WHERE available=true\n" +
            "ORDER BY random()\n" +
            "LIMIT 1;")
    Node findRandomNode();

}