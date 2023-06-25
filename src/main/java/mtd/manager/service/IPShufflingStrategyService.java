package mtd.manager.service;

import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import mtd.manager.entity.Deployment;
import mtd.manager.entity.Node;
import mtd.manager.entity.Parameter;
import mtd.manager.entity.Strategy;
import mtd.manager.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IPShufflingStrategyService implements Runnable {

    public static final String IP_SHUFFLING_CONTAINER = "ip-shuffling-container";
    public static final String SERVICE_ACCOUNT_SHUFFLING_CONTAINER = "service-account-shuffling-container";

    @Value("${kubernetes.master.url}")
    private String masterUrl;
    @Autowired
    private DeploymentRepository deploymentRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private NodeLabelRepository nodeLabelRepository;
    @Autowired
    private StrategyRepository strategyRepository;
    @Autowired
    private ParameterRepository parameterRepository;

    @Override
    @SneakyThrows
    public void run() {
//        KubernetesClient kubernetesClient = new KubernetesClientBuilder()
//                .withConfig(new ConfigBuilder().withMasterUrl(masterUrl).build())
//                .build();
        log.info("IPShufflingStrategyService running");
        KubernetesClient kubernetesClient = new KubernetesClientBuilder().build();
        log.info("KubernetesClient builded");
        while (true) {

            Strategy ipShuffling = strategyRepository.findByName(IP_SHUFFLING_CONTAINER).orElseThrow(EntityNotFoundException::new);

            List<Deployment> deployments = deploymentRepository.findAll();
            log.info("deployments found: {}", deployments.size());
            for (Deployment deployment : deployments) {
                if(Boolean.TRUE == ipShuffling.getEnabled()){
                    io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment = kubernetesClient.apps().deployments().inNamespace(deployment.getNamespace()).withName(deployment.getName()).get();
                    log.info("deployment running: {}", runningDeployment.getMetadata().getName());
                    changeDeploymentNode(runningDeployment);
                    kubernetesClient.apps().deployments()
                            .inNamespace(deployment.getNamespace())
                            .resource(runningDeployment)
                            .replace();

                    log.info("Restart executed for deployment {}", runningDeployment.getMetadata().getName());
                }
            }

            Parameter period = parameterRepository.findByKey("period-ip").orElseThrow(EntityNotFoundException::new);
            Thread.sleep(Integer.valueOf(period.getValue()));
        }
    }


    private void changeDeploymentNode(io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment) {
        Map<String, String> map = runningDeployment.getSpec().getTemplate().getSpec().getNodeSelector();
        Node node = nodeRepository.findRandomNode();

        runningDeployment.getSpec()
                .getTemplate()
                .getSpec()
                .setNodeName(node.getHostname());
    }

}
