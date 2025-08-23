package com.victory.evertalk.global.common.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastApiClientService {

    private final WebClient fastApiWebClient;
    private final ObjectMapper objectMapper;



}
