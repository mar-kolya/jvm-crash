import jdk.jfr.Recording;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Premain {

    private static final String JFP = "config.jfp";
    private static final int RECORDING_MAX_SIZE = 64 * 1024 * 1024; // 64 megs
    private static final Duration RECORDING_MAX_AGE = Duration.ofMinutes(5);

    public static void premain(final String agentArgs, final Instrumentation inst) {
        agentmain(agentArgs, inst);
    }

    public static void agentmain(final String agentArgs, final Instrumentation inst) {
        try {
            final Map<String, String> recordingSettings =
                    readNamedJfpResource(JFP);
            createRecording("test", recordingSettings);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Recording createRecording(final String recordingName, final Map<String, String> recordingSettings) {
        final Recording recording = new Recording();
        recording.setName(recordingName);
        recording.setSettings(recordingSettings);
        recording.setMaxSize(RECORDING_MAX_SIZE);
        recording.setMaxAge(RECORDING_MAX_AGE);
        recording.start();
        return recording;
    }

    private static Map<String, String> readJfpFile(final InputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Cannot read jfp file from empty stream!");
        }
        final Properties props = new Properties();
        props.load(stream);
        final Map<String, String> map = new HashMap<>();
        for (final Map.Entry<Object, Object> o : props.entrySet()) {
            map.put(String.valueOf(o.getKey()), String.valueOf(o.getValue()));
        }
        return map;
    }

    private static InputStream getNamedResource(final String name) {
        return Premain.class.getClassLoader().getResourceAsStream(name);
    }

    public static Map<String, String> readNamedJfpResource(
            final String name) throws IOException {
        final Map<String, String> result;
        try (final InputStream stream = getNamedResource(name)) {
            result = readJfpFile(stream);
        }

        return Collections.unmodifiableMap(result);
    }
}
