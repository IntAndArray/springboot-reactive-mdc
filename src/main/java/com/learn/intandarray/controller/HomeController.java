package com.learn.intandarray.controller;

import com.learn.intandarray.services.HomeService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class HomeController {

  @Autowired private HomeService homeService;

  @GetMapping("speak")
  public Mono<String> speak() {

    return Mono.just("Hey")
        .flatMap(
            data -> {
              Map<String, String> map = Map.of("trackId", "RET5464-XFEfd34");
              MDC.setContextMap(map);
              log.info("SPEAK NormL :::::: THREAD ::::: " + Thread.currentThread().getName());
              return homeService.speak("Normal Speak ::: Thread :" + Thread.currentThread().getName() + " and MDC: "+MDC.get("trackId"));
            })
        .onErrorResume(
            error -> {
              log.error("Error in speaking");
              return Mono.error(error);
            });
  }

  @GetMapping("speak-api")
  public Mono<String> speakApi() {

    return Mono.just("Hey")
        .flatMap(
            data -> {
              Map<String, String> map = Map.of("trackId", "RET5464-HHHHH89");
              MDC.setContextMap(map);
              log.info("SPEAK API :::::: THREAD ::::: " + Thread.currentThread().getName());
              return homeService.callThroughWebClient("Normal Thread change ::: Controller: Thread: "+Thread.currentThread().getName() + " & MDC : " + MDC.get("trackId"));
            })
        .onErrorResume(
            error -> {
              log.error("Error by API");
              return Mono.error(error);
            });
  }
}
