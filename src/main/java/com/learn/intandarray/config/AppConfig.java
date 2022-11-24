package com.learn.intandarray.config;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

@Configuration
public class AppConfig {

  @PostConstruct
  public void inti() {

    // Hook for normal thread swap in chain
    Schedulers.onScheduleHook(
        "mdc",
        runnable -> {
          Map<String, String> map = MDC.getCopyOfContextMap();
          return () -> {
            if (map != null) {
              MDC.setContextMap(map);
            }
            try {
              runnable.run();
            } finally {
              MDC.clear();
            }
          };
        });
  }

  // Hook at thread swap for webclient
  ExchangeFilterFunction function =
      (request, next) -> {
        // Main request thread
        Map<String, String> map = MDC.getCopyOfContextMap();
        return next.exchange(request)
            .doOnNext(
                value -> {
                  // Get invoked as soon as reactor thread will change
                  if (map != null) {
                    MDC.setContextMap(map);
                  }
                });
      };

  @Bean
  public WebClient webClient() {

    return WebClient.builder()
        .filter(function)
        .baseUrl("https://reqres.in")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
