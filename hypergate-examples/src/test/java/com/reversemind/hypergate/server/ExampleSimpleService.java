package com.reversemind.hypergate.server;

import com.reversemind.hypergate.shared.IExampleSimpleService;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Fake ExampleSimpleService implementation on server side
 */
public class ExampleSimpleService implements IExampleSimpleService {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ExampleSimpleService.class);

    @Override
    public String getSimpleValue(String parameter) {
        LOG.info("get from remote client parameter:" + parameter);
        Date date = new Date();
        String responseValue = " Sended from remote server date:" + date + " ms:" + date.getTime() + " parameter from client:" + parameter;
        LOG.info("Sending from server response value:" + responseValue);
        return responseValue;
    }
}
