package com.learn.intandarray.services;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class HomeServiceImpl implements HomeService {

  @Autowired private WebClient webClient;

  @Override
  public Mono<String> speak(String threadDetails) {

    return Mono.just("")
        .flatMap(
            data -> {
              return Mono.just(
                  data.concat(
                          threadDetails + " -----> Service Details:::: Thread ::: "
                          + Thread.currentThread().getName()
                          + " MDC Context : "
                          + MDC.get("trackId")));
            })
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<String> callThroughWebClient(String threadDetails) {

    return webClient
        .get()
        .uri("/api/users?page=2")
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(
            data -> {
              return Mono.just(threadDetails + " ----> Service Thread Details :::" +
                  "Thread : "
                      + Thread.currentThread().getName()
                      + " MDC Context : "
                      + MDC.get("trackId"));
            })
        .subscribeOn(Schedulers.boundedElastic());
  }
}
