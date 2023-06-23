package mtd.manager.controller;

import mtd.manager.dto.StrategyDTO;
import mtd.manager.service.StrategyService;
import mtd.manager.vo.StrategyQueryVO;
import mtd.manager.vo.StrategyUpdateVO;
import mtd.manager.vo.StrategyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @PostMapping
    public String save(@Valid @RequestBody StrategyVO vO) {
        return strategyService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        strategyService.delete(id);
    }

    @PutMapping
    public void update(@Valid @RequestBody StrategyUpdateVO vO) {
        strategyService.update(vO);
    }

    @GetMapping("/{id}")
    public StrategyDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return strategyService.getById(id);
    }

    @GetMapping
    public Page<StrategyDTO> query(@Valid StrategyQueryVO vO) {
        return strategyService.query(vO);
    }

    @GetMapping("/all")
    public List<StrategyDTO> findAll() {
        return strategyService.findAll();
    }

    @PutMapping("/enable/all")
    public void enableAll(@RequestBody Boolean bool) {
        strategyService.enableAll(bool);
    }

}
