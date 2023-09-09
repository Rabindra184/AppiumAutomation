package org.example;

import lombok.SneakyThrows;

import java.io.BufferedReader;;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class AdbCommandExecutor {
    @SneakyThrows
    public static String exec(String deviceId, String command) {
        String commandString = deviceId != null
                ? String.format("adb -s %s shell %s", deviceId, command)
                : String.format("adb shell %s", command);

        System.out.println("Executing command: " + commandString);

        Process process = Runtime.getRuntime().exec(commandString);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String output = reader.lines().collect(Collectors.joining("\n"));
            int exitCode = process.waitFor();
            System.out.println("Command executed with exit code: " + exitCode);
            return output;
        }
    }

}


