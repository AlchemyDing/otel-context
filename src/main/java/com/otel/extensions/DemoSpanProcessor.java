package com.otel.extensions;

import io.opentelemetry.api.internal.StringUtils;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

import static com.otel.extensions.DemoPropagator.PROPAGATION_UPSTREAM_NAME;
import static com.otel.extensions.DemoPropagator.UPSTREAM_NAME;

public class DemoSpanProcessor implements SpanProcessor {


    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {

        String upstreamName = parentContext.get(PROPAGATION_UPSTREAM_NAME);
        if (!StringUtils.isNullOrEmpty(upstreamName)) {
            span.setAttribute(UPSTREAM_NAME, upstreamName);
        }
    }

    @Override
    public boolean isStartRequired() {
        return true;
    }

    @Override
    public void onEnd(ReadableSpan span) {

    }

    @Override
    public boolean isEndRequired() {
        return false;
    }

    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode forceFlush() {
        return CompletableResultCode.ofSuccess();
    }
}
