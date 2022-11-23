package com.learn.intandarray.services;

import reactor.core.publisher.Mono;

public interface HomeService {

  Mono<String> speak(String threadDetails);

  Mono<String> callThroughWebClient(String threadDetails);
}
