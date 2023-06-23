package mtd.manager.service;

import mtd.manager.dto.DeploymentDTO;
import mtd.manager.entity.Deployment;
import mtd.manager.repository.DeploymentRepository;
import mtd.manager.vo.DeploymentQueryVO;
import mtd.manager.vo.DeploymentUpdateVO;
import mtd.manager.vo.DeploymentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepository;

    public String save(DeploymentVO vO) {
        Deployment bean = new Deployment();
        BeanUtils.copyProperties(vO, bean);
        bean = deploymentRepository.save(bean);
        return bean.getName();
    }

    public void delete(Long id) {
        deploymentRepository.deleteById(id);
    }

    public void update(DeploymentUpdateVO vO) {
        Deployment bean = requireOne(vO.getId());
        BeanUtils.copyProperties(vO, bean);
        deploymentRepository.save(bean);
    }

    public DeploymentDTO getById(Long id) {
        Deployment original = requireOne(id);
        return toDTO(original);
    }

    public Page<DeploymentDTO> query(DeploymentQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private DeploymentDTO toDTO(Deployment original) {
        DeploymentDTO bean = new DeploymentDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Deployment requireOne(Long id) {
        return deploymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public List<DeploymentDTO> findAll() {
        List<DeploymentDTO> list = new ArrayList<>();
        deploymentRepository.findAll().forEach(depl -> list.add(toDTO(depl)));
        return list;
    }
}
