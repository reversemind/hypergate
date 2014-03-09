package org.apache.curator.x.discovery;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 */
public class JsonServiceInstance<T> extends ServiceInstance<T> {

//    private final String        setName;
//    private final String        id;
//    private final String        address;
//    private final Integer       setPort;
//    private final Integer       sslPort;
//    private final T             payload;
//    private final long          registrationTimeUTC;
//    private final ServiceType   serviceType;
//    private final UriSpec       uriSpec;

    @JsonCreator
    public JsonServiceInstance(@JsonProperty("name") String name,
                               @JsonProperty("id") String id,
                               @JsonProperty("address") String address,
                               @JsonProperty("port") Integer port,
                               @JsonProperty("sslPort") Integer sslPort,
                               @JsonProperty("payload") T payload,
                               @JsonProperty("registrationTimeUTC") long registrationTimeUTC,
                               @JsonProperty("serviceType") ServiceType serviceType,
                               @JsonProperty("uriSpec") UriSpec uriSpec) {
        super(name, id, address, port, sslPort, payload, registrationTimeUTC, serviceType, uriSpec);
    }
}
