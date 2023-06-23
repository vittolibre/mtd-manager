package mtd.manager.controller;


import mtd.manager.dto.NodeDTO;
import mtd.manager.service.NodeService;
import mtd.manager.vo.NodeQueryVO;
import mtd.manager.vo.NodeUpdateVO;
import mtd.manager.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @PostMapping
    public String save(@Valid @RequestBody NodeVO vO) {
        return nodeService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        nodeService.delete(id);
    }

    @PutMapping
    public void update(@Valid @RequestBody NodeUpdateVO vO) {
        nodeService.update(vO);
    }

    @GetMapping("/{id}")
    public NodeDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return nodeService.getById(id);
    }

    @GetMapping
    public Page<NodeDTO> query(@Valid NodeQueryVO vO) {
        return nodeService.query(vO);
    }

    @GetMapping("/all")
    public List<NodeDTO> findAll() {
        return nodeService.findAll();
    }
}
