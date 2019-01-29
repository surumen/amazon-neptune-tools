package com.amazonaws.services.neptune.auth;

import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;

public class HandshakeRequestConfig {

    public static HandshakeRequestConfig parse(String s) {
        String[] values = s.split(",");

        boolean removeHostHeaderAfterSigning = Boolean.parseBoolean(values[0]);
        int port = Integer.parseInt(values[1]);

        Collection<String> endpoints = new ArrayList<>();
        endpoints.addAll(Arrays.asList(values).subList(2, values.length));

        return new HandshakeRequestConfig(endpoints, port, removeHostHeaderAfterSigning);
    }

    private final List<String> endpoints;
    private final int port;
    private final boolean removeHostHeaderAfterSigning;
    private final Random random = new Random(DateTime.now().getMillis());

    public HandshakeRequestConfig(Collection<String> endpoints, int port, boolean removeHostHeaderAfterSigning) {
        this.endpoints = new ArrayList<>(endpoints);
        this.port = port;
        this.removeHostHeaderAfterSigning = removeHostHeaderAfterSigning;
    }

    public String chooseHostHeader() {
        return String.format("%s:%s", endpoints.get(random.nextInt(endpoints.size())), port);
    }

    public boolean removeHostHeaderAfterSigning() {
        return removeHostHeaderAfterSigning;
    }

    public String value() {
        return String.format("%s,%s,%s", removeHostHeaderAfterSigning, port, endpoints.stream().collect(Collectors.joining(",")));
    }

    @Override
    public String toString() {
        return value();
    }
}
