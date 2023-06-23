package mtd.manager.service;

import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.PatchUtils;
import jakarta.persistence.EntityNotFoundException;
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

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MtdStrategyService implements ApplicationRunner {

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
    public void run(ApplicationArguments args) throws Exception {
//        KubernetesClient kubernetesClient = new KubernetesClientBuilder()
//                .withConfig(new ConfigBuilder().withMasterUrl(masterUrl).build())
//                .build();
        KubernetesClient kubernetesClient = new KubernetesClientBuilder().build();

        while (true) {

            Strategy ipShuffling = strategyRepository.findByName(IP_SHUFFLING_CONTAINER).orElseThrow(EntityNotFoundException::new);
            Strategy saShuffling = strategyRepository.findByName(SERVICE_ACCOUNT_SHUFFLING_CONTAINER).orElseThrow(EntityNotFoundException::new);

            List<Deployment> deployments = deploymentRepository.findAll();

            for (Deployment deployment : deployments) {

                io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment = kubernetesClient.apps().deployments().inNamespace(deployment.getNamespace()).withName(deployment.getName()).get();
                log.info("deployment running: {}", runningDeployment.getMetadata().getName());
                if(Boolean.TRUE == ipShuffling.getEnabled()){
                    changeDeploymentNode(runningDeployment);
                }
                if(Boolean.TRUE == saShuffling.getEnabled()){
                    changeServiceAccount(kubernetesClient, deployment, runningDeployment);
                }

                kubernetesClient.apps().deployments()
                        .inNamespace(deployment.getNamespace())
                        .resource(runningDeployment)
                                .replace();

                log.info("Restart executed for deployment {}", runningDeployment.getMetadata().getName());
            }

            Parameter period = parameterRepository.findByKey("period").orElseThrow(EntityNotFoundException::new);
            Thread.sleep(Integer.valueOf(period.getValue()));
        }
    }

    private static void changeServiceAccount(KubernetesClient kubernetesClient, Deployment deployment, io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment) throws InterruptedException {
        String generatedString = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        String serviceAccountName = deployment.getName().replace("-", ".") + "." + generatedString;
        ServiceAccount sa = new ServiceAccountBuilder()
                .withNewMetadata()
                .withName(serviceAccountName)
                .withNamespace(deployment.getNamespace())
                .endMetadata()
                .build();
        kubernetesClient.serviceAccounts().resource(sa).createOrReplace();

        Thread.sleep(1000);

        String oldServiceAccountName = runningDeployment.getSpec().getTemplate().getSpec().getServiceAccountName();
        runningDeployment.getSpec().getTemplate().getSpec().setServiceAccountName(serviceAccountName);
        ServiceAccount old = new ServiceAccountBuilder().withNewMetadata().withName(oldServiceAccountName).endMetadata().build();
        kubernetesClient.serviceAccounts().resource(old).delete();
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
