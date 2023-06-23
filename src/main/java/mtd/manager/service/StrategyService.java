package mtd.manager.service;


import mtd.manager.dto.StrategyDTO;
import mtd.manager.entity.Strategy;
import mtd.manager.repository.StrategyRepository;
import mtd.manager.vo.StrategyQueryVO;
import mtd.manager.vo.StrategyUpdateVO;
import mtd.manager.vo.StrategyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StrategyService {

    @Autowired
    private StrategyRepository strategyRepository;

    public Long save(StrategyVO vO) {
        Strategy bean = new Strategy();
        BeanUtils.copyProperties(vO, bean);
        bean = strategyRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        strategyRepository.deleteById(id);
    }

    public void update(StrategyUpdateVO vO) {
        Strategy bean = requireOne(vO.getId());
        BeanUtils.copyProperties(vO, bean);
        strategyRepository.save(bean);
    }

    public StrategyDTO getById(Long id) {
        Strategy original = requireOne(id);
        return toDTO(original);
    }

    public Page<StrategyDTO> query(StrategyQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private StrategyDTO toDTO(Strategy original) {
        StrategyDTO bean = new StrategyDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Strategy requireOne(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public List<StrategyDTO> findAll() {
        List<StrategyDTO> list = new ArrayList<>();
        strategyRepository.findAll().forEach(strategy -> list.add(toDTO(strategy)));
        return list;
    }

    public void enableAll(Boolean bool) {
        strategyRepository.updateAllStrategies(bool);
    }

}
