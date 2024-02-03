package fi.nls.hakunapi.telemetry;

import fi.nls.hakunapi.core.telemetry.ServiceTelemetry;
import fi.nls.hakunapi.core.telemetry.TelemetryFactory;

public class LoggingTelemetryFactory implements TelemetryFactory {

    @Override
    public ServiceTelemetry createServiceTelemetry() {
        return new LoggingServiceTelemetry();
    }

}
