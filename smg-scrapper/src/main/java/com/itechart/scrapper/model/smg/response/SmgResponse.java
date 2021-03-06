package com.itechart.scrapper.model.smg.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;

@Data
public class SmgResponse {

    private static final Logger log = LoggerFactory.getLogger(SmgResponse.class);

    public String ErrorCode;
    public String Permission;

    public static <T extends SmgResponse> T getResponse(URL requestUrl, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T response = mapper.readValue(
            requestUrl, aClass);
        if (!StringUtils.isEmpty(response.getErrorCode())) {
            log.error("Smg response permission: {}, error: {}", response.getPermission(), response.getErrorCode());
            throw new RuntimeException(response.getErrorCode());
        }
        return response;
    }
}
