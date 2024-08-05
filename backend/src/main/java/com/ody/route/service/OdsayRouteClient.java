package com.ody.route.service;

import com.ody.common.exception.OdyServerErrorException;
import com.ody.meeting.domain.Location;
import com.ody.route.config.RouteProperties;
import com.ody.route.domain.RouteTime;
import com.ody.route.dto.OdsayResponse;
import com.ody.route.mapper.OdsayResponseMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

@Slf4j
public class OdsayRouteClient implements RouteClient {

    private final RouteProperties routeProperties;
    private final RestClient restClient;

    public OdsayRouteClient(
            RouteProperties routeProperties,
            RestClient.Builder routeRestClientBuilder
    ) {
        this.routeProperties = routeProperties;
        this.restClient = routeRestClientBuilder.build();
    }

    @Override
    public RouteTime calculateRouteTime(Location origin, Location target) {
        OdsayResponse response = getOdsayResponse(origin, target);
        return responseToRouteTime(response);
    }

    private OdsayResponse getOdsayResponse(Location origin, Location target) {
        OdsayResponse response = restClient.get()
                .uri(makeURI(origin, target))
                .retrieve()
                .body(OdsayResponse.class);
        return Objects.requireNonNullElseGet(response, () -> {throw new OdyServerErrorException("서버 에러");});
    }

    private URI makeURI(Location origin, Location target) {
        String uri = routeProperties.getUrl()
                + "?SX=" + origin.getLongitude()
                + "&SY=" + origin.getLatitude()
                + "&EX=" + target.getLongitude()
                + "&EY=" + target.getLatitude()
                + "&apiKey=" + routeProperties.getApiKey();
        try {
            return new URI(uri);
        } catch (URISyntaxException exception) {
            throw new OdyServerErrorException(exception.getMessage());
        }
    }

    private RouteTime responseToRouteTime(OdsayResponse response) {
        long minutes = OdsayResponseMapper.mapMinutes(response);
        return new RouteTime(minutes);
    }
}