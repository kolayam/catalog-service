package eu.nimble.service.catalogue.index;

import eu.nimble.common.rest.indexing.IIndexingServiceClient;
import eu.nimble.common.rest.indexing.IIndexingServiceClientFallback;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:bootstrap.yml")
public class IndexingClientController {

    private IIndexingServiceClient nimbleIndexClient;

    private IIndexingServiceClient federatedIndexClient;

    private List<IIndexingServiceClient> clients;

    @Value("${nimble.indexing.url}")
    private String nimbleIndexUrl;

    @Value("${federated-index-enabled}")
    private boolean federatedIndexEnabled;

    @Value("${nimble.indexing.federated-index-url}")
    private String federatedIndexUrl;

    @Value("${federated-index-platform-name}")
    private String federatedIndexPlatformName;

    @Autowired
    IIndexingServiceClientFallback indexingFallback;

    public String getFederatedIndexPlatformName() {
        return federatedIndexPlatformName;
    }

    public IndexingClientController() {

    }

    public IIndexingServiceClient getNimbleIndexClient() {
        if (nimbleIndexClient == null) {
            nimbleIndexClient = createIndexingClient(nimbleIndexUrl);
        }
        return nimbleIndexClient;
    }

    public IIndexingServiceClient getFederatedIndexClient() {
        if (federatedIndexEnabled && federatedIndexClient == null) {
            federatedIndexClient = createIndexingClient(federatedIndexUrl);
        }
        return federatedIndexClient;
    }

    public List<IIndexingServiceClient> getClients() {
        if (clients == null) {
            clients = new ArrayList<IIndexingServiceClient>();
            clients.add(getNimbleIndexClient());
            if (federatedIndexEnabled) {
                clients.add(getFederatedIndexClient());
            }
        }
        return clients;
    }

    private IIndexingServiceClient createIndexingClient(String url) {
        return HystrixFeign.builder().contract(new SpringMvcContract())
                .encoder(new Encoder.Default())
                .decoder(new Decoder.Default())
                .retryer(new Retryer.Default(1, 100, 3))
                .setterFactory((target, method) -> {
                    // 为每个方法创建Hystrix配置
                    String groupKey = target.name();
                    String commandKey = groupKey + "#" + method.getName();

                    return HystrixCommand.Setter
                            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                            .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                    .withExecutionTimeoutInMilliseconds(300000) // 30秒超时
                                    .withExecutionIsolationStrategy(
                                            HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                    .withCircuitBreakerEnabled(true) // 启用熔断
                                    .withCircuitBreakerRequestVolumeThreshold(50) // 20个请求
                                    .withCircuitBreakerErrorThresholdPercentage(50) // 错误率50%
                                    .withCircuitBreakerSleepWindowInMilliseconds(5000) // 熔断5秒
                    );
                })
                .options(new Request.Options(50000, 1800000)) // 连接5秒，读取25秒
                .target(IIndexingServiceClient.class, url, indexingFallback);
    }

}
