package com.otel.extensions;

import io.opentelemetry.api.internal.StringUtils;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;

import java.util.Collections;
import java.util.List;

public class DemoPropagator implements TextMapPropagator {
    private static final String FIELD = "X-propagation";
    public static final String UPSTREAM_NAME = "upstream_name";
    public static final ContextKey<String> PROPAGATION_UPSTREAM_NAME = ContextKey.named(UPSTREAM_NAME);

    private final String serviceName;

    public DemoPropagator(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public List<String> fields() {
        return Collections.singletonList(FIELD);
    }

    @Override
    public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
        String upstreamName = context.get(PROPAGATION_UPSTREAM_NAME);
        if (StringUtils.isNullOrEmpty(upstreamName)) {
            upstreamName = serviceName;
        }
        setter.set(carrier, FIELD, upstreamName);
    }

    @Override
    public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
        String propagationStart = getter.get(carrier, FIELD);
        if (propagationStart != null) {
            return context.with(PROPAGATION_UPSTREAM_NAME, propagationStart);
        } else {
            return context;
        }
    }
}
