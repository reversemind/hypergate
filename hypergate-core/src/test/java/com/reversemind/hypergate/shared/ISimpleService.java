package com.reversemind.hypergate.shared;

/**
 * Fake interface for Client Server interaction test
 */
public interface ISimpleService {

    public static final String RETURN_VALUE = "RETURN_VALUE";

    public String getSimpleValue(String parameter);
}
