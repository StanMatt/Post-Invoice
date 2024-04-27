package pl.postinvoice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.postinvoice.tracking.TrackingFilter;
import pl.postinvoice.tracking.TrackingFilterSeparator;

@Configuration
public class TrackingConfig {
    @Bean
    FilterRegistrationBean<TrackingFilter> registerTruckingFilter() {
        FilterRegistrationBean<TrackingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrackingFilter());
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    FilterRegistrationBean<TrackingFilterSeparator> registerTruckingFilterSeparator() {
        FilterRegistrationBean<TrackingFilterSeparator> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TrackingFilterSeparator());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
