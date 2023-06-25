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
import mtd.manager.entity.Parameter;
import mtd.manager.entity.Strategy;
import mtd.manager.repository.DeploymentRepository;
import mtd.manager.repository.ParameterRepository;
import mtd.manager.repository.StrategyRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class ServiceAccountStrategyService implements Runnable {
    public static final String SERVICE_ACCOUNT_SHUFFLING_CONTAINER = "service-account-shuffling-container";

    @Value("${kubernetes.master.url}")
    private String masterUrl;
    @Autowired
    private DeploymentRepository deploymentRepository;
    @Autowired
    private StrategyRepository strategyRepository;
    @Autowired
    private ParameterRepository parameterRepository;

    @Override
    @SneakyThrows
    public void run()  {
//        KubernetesClient kubernetesClient = new KubernetesClientBuilder()
//                .withConfig(new ConfigBuilder().withMasterUrl(masterUrl).build())
//                .build();
        log.info("ServiceAccountStrategyService running");
        KubernetesClient kubernetesClient = new KubernetesClientBuilder().build();
        log.info("KubernetesClient builded");

        while (true) {

            Strategy saShuffling = strategyRepository.findByName(SERVICE_ACCOUNT_SHUFFLING_CONTAINER).orElseThrow(EntityNotFoundException::new);

            List<Deployment> deployments = deploymentRepository.findAll();
            log.info("deployments found: {}", deployments.size());
            for (Deployment deployment : deployments) {
                if(Boolean.TRUE == saShuffling.getEnabled()){
                    io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment = kubernetesClient.apps().deployments().inNamespace(deployment.getNamespace()).withName(deployment.getName()).get();
                    log.info("deployment running: {}", runningDeployment.getMetadata().getName());
                    changeServiceAccount(kubernetesClient, deployment, runningDeployment);
                    kubernetesClient.apps().deployments()
                            .inNamespace(deployment.getNamespace())
                            .resource(runningDeployment)
                            .replace();

                    log.info("Restart executed for deployment {}", runningDeployment.getMetadata().getName());
                    String oldServiceAccountName = runningDeployment.getSpec().getTemplate().getSpec().getServiceAccountName();
                    if(!"default".equals(oldServiceAccountName)){
                        ServiceAccount old = new ServiceAccountBuilder().withNewMetadata().withName(oldServiceAccountName).endMetadata().build();
                        kubernetesClient.serviceAccounts().resource(old).delete();
                    }
                }
            }

            Parameter period = parameterRepository.findByKey("period-sa").orElseThrow(EntityNotFoundException::new);
            Thread.sleep(Integer.valueOf(period.getValue()));
        }
    }

    private static void changeServiceAccount(KubernetesClient kubernetesClient, Deployment deployment, io.fabric8.kubernetes.api.model.apps.Deployment runningDeployment) throws InterruptedException {
        String generatedString = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        String serviceAccountName = deployment.getName().replace("-", ".") + "." + generatedString;
        Map<String, String> annotations = new HashMap<>();
        annotations.put("iam.kubesphere.io/role", "admin");
        ServiceAccount sa = new ServiceAccountBuilder()
                .withNewMetadata()
                .withName(serviceAccountName)
                .withNamespace(deployment.getNamespace())
                .withAnnotations(annotations)
                .endMetadata()
                .build();
        kubernetesClient.serviceAccounts().resource(sa).createOrReplace();

        Thread.sleep(1000);


        runningDeployment.getSpec().getTemplate().getSpec().setServiceAccountName(serviceAccountName);

    }

}
