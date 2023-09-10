package org.example.RPC.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Request implements Serializable {
    @JsonProperty("service")
    private String service;

    @JsonProperty("method")
    private String method;

    @JsonProperty("paramsTypeCount")
    private Integer typeCount;

    @JsonProperty("paramsType")
    private String[] type;

    @JsonProperty("params")
    private Object[] params;
}
