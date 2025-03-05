package org.example;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.error.YAMLException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class YmlParser extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(IDS.staffchannel)) {
            TextChannel textChannel = event.getGuildChannel().asTextChannel();
            Message message = event.getMessage();

            // Check if the message has attachments
            if (!message.getAttachments().isEmpty()) {
                processAttachments(textChannel, message);
            } else {
                // Check if the message starts with !validateyaml
                String messageContent = message.getContentDisplay().trim();
                if (messageContent.startsWith("!validateyaml")) {
                    String yamlContent = messageContent.substring("!validateyaml".length()).trim();

                    if (yamlContent.startsWith("```yaml") && yamlContent.endsWith("```")) {
                        yamlContent = yamlContent.substring(6, yamlContent.length() - 3).trim(); // Remove markdown code block syntax
                    }

                    try {
                        // Check if yamlContent is directly provided YAML or a URL
                        if (yamlContent.startsWith("https://") || yamlContent.startsWith("http://")) {
                            // Assume yamlContent is a URL to a text file containing YAML
                            processYamlFileUrl(textChannel, yamlContent);
                        } else {
                            // Assume yamlContent is directly provided YAML content
                            processYamlContent(textChannel, yamlContent);
                        }
                    } catch (IOException e) {
                        textChannel.sendMessage("Error reading YAML file: " + e.getMessage()).queue();
                    } catch (MarkedYAMLException e) {
                        handleYamlParseException(textChannel, e);
                    } catch (Exception e) {
                        textChannel.sendMessage("Error: " + e.getMessage()).queue();
                    }
                }
            }
        }
    }

    private void processAttachments(TextChannel textChannel, Message message) {
        message.getAttachments().forEach(attachment -> {
            if (attachment.isImage()) {
                textChannel.sendMessage("Cannot process image attachments.").queue();
            } else {
                try {
                    InputStream attachmentStream = attachment.retrieveInputStream().get();
                    String yamlContent = readInputStream(attachmentStream);
                    validateAndRespond(textChannel, yamlContent);
                } catch (IOException e) {
                    textChannel.sendMessage("Error reading attachment: " + e.getMessage()).queue();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void processYamlFileUrl(TextChannel textChannel, String fileUrl) throws IOException {
        InputStream inputStream = fetchYamlFileFromUrl(fileUrl);
        String yamlContent = readInputStream(inputStream);
        validateAndRespond(textChannel, yamlContent);
    }

    private InputStream fetchYamlFileFromUrl(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            content.append(new String(buffer, 0, bytesRead));
        }
        return content.toString();
    }

    private void processYamlContent(TextChannel textChannel, String yamlContent) {
        validateAndRespond(textChannel, yamlContent);
    }

    private void validateAndRespond(TextChannel textChannel, String yamlContent) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlMap = yaml.load(yamlContent);

            textChannel.sendMessage("YAML is valid!\nParsed YAML:\n```\n" + yamlMap + "\n```").queue();
        } catch (MarkedYAMLException e) {
            handleYamlParseException(textChannel, e);
        } catch (Exception e) {
            textChannel.sendMessage("YAML Error: " + e.getMessage()).queue();
        }
    }


    private Map<String, Object> parseYaml(String yamlContent) throws YAMLException {
        Yaml yaml = new Yaml();
        return yaml.load(yamlContent);
    }

    private void handleYamlParseException(TextChannel textChannel, MarkedYAMLException e) {
        Mark problemMark = e.getProblemMark();
        int line = problemMark.getLine() + 1; // Convert to 1-based line number
        int column = problemMark.getColumn() + 1; // Convert to 1-based column number

        String errorMessage = String.format("YAML Error:\nMessage: %s\nProblem position: Line %d, Column %d",
                e.getMessage(), line, column);

        String[] lines = e.toString().split("\n");
        String cause = lines[lines.length - 1];
        textChannel.sendMessage(errorMessage + "\nCause:" + cause).queue();
    }

}
