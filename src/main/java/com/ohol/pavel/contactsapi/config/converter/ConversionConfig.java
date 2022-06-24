package com.ohol.pavel.contactsapi.config.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

/**
 * @author Pavel Ohol
 */
@Configuration
public class ConversionConfig  {

    @Bean
    @Primary
    public ConversionService appConversionService(
            final Set<Converter> converters
    ) {
        ConversionServiceFactoryBean serviceFactory
                = new ConversionServiceFactoryBean();
        serviceFactory.setConverters(converters);
        serviceFactory.afterPropertiesSet();
        return serviceFactory.getObject();
    }

}
