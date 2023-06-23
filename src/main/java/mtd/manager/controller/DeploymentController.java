package mtd.manager.controller;

import mtd.manager.dto.DeploymentDTO;
import mtd.manager.service.DeploymentService;
import mtd.manager.vo.DeploymentQueryVO;
import mtd.manager.vo.DeploymentUpdateVO;
import mtd.manager.vo.DeploymentVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/deployment")
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @PostMapping
    public String save(@Valid @RequestBody DeploymentVO vO) {
        return deploymentService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        deploymentService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @RequestBody DeploymentUpdateVO vO) {
        deploymentService.update(vO);
    }

    @GetMapping("/{id}")
    public DeploymentDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return deploymentService.getById(id);
    }

    @GetMapping
    public Page<DeploymentDTO> query(@Valid DeploymentQueryVO vO) {
        return deploymentService.query(vO);
    }

    @GetMapping("/all")
    public List<DeploymentDTO> findAll() {
        return deploymentService.findAll();
    }
}
