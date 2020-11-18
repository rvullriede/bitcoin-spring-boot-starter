package org.tbk.bitcoin.tool.fee.blockstreaminfo.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tbk.bitcoin.tool.fee.blockstreaminfo.BlockstreamInfoFeeApiClient;
import org.tbk.bitcoin.tool.fee.blockstreaminfo.BlockstreamInfoFeeApiClientImpl;
import org.tbk.bitcoin.tool.fee.blockstreaminfo.BlockstreamInfoFeeProvider;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableConfigurationProperties(BlockstreamInfoFeeClientAutoConfigProperties.class)
@ConditionalOnProperty(name = {
        "org.tbk.bitcoin.tool.fee.enabled",
        "org.tbk.bitcoin.tool.fee.blockstreaminfo.enabled"
}, havingValue = "true", matchIfMissing = true)
// @AutoConfigureBefore
public class BlockstreamInfoFeeClientAutoConfiguration {

    private final BlockstreamInfoFeeClientAutoConfigProperties properties;

    public BlockstreamInfoFeeClientAutoConfiguration(BlockstreamInfoFeeClientAutoConfigProperties properties) {
        this.properties = requireNonNull(properties);
    }

    @Bean
    @ConditionalOnClass(BlockstreamInfoFeeApiClient.class)
    @ConditionalOnMissingBean(BlockstreamInfoFeeApiClient.class)
    public BlockstreamInfoFeeApiClient blockstreamInfoFeeApiClient() {
        return new BlockstreamInfoFeeApiClientImpl(properties.getBaseUrl(), properties.getToken().orElse(null));
    }

    @Bean
    @ConditionalOnClass(BlockstreamInfoFeeProvider.class)
    @ConditionalOnMissingBean(BlockstreamInfoFeeProvider.class)
    public BlockstreamInfoFeeProvider blockstreamInfoFeeProvider(BlockstreamInfoFeeApiClient blockstreamInfoFeeApiClient) {
        return new BlockstreamInfoFeeProvider(blockstreamInfoFeeApiClient);
    }
}
