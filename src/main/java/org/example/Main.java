package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.OptionType.valueOf;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        File file = new File("config.properties");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        String botToken = properties.getProperty("token");
        if (botToken == null) {
            System.err.println("Bot token not found in config.properties.");
            System.exit(1);
        }


        JDA jda = JDABuilder.createDefault(botToken)
                .addEventListeners(new MessageListener(),new BugReports(),new DownloadCommand(), new DmLogger(),new Suggestions(), new GetRole(), new YmlParser())
                .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.DIRECT_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .build();



        CommandListUpdateAction commands = jda.updateCommands();

try {
    OptionData opt = new OptionData(STRING,"plugin", "plugin name",true,false)
            .addChoice("Ultimate_BlockRegen","Ultimate_BlockRegen" ).addChoice("Mine_X_Farm_Regen","Mine_X_Farm_Regen")
                    .addChoice("ResourcePack_Hub","ResourcePack_Hub").addChoice("Modify_BrushDrop","Modify_BrushDrop");

    OptionData opt2 = new OptionData(STRING,"plugin", "plugin name",true,false)
            .addChoice("Ultimate_BlockRegen","Ultimate_BlockRegen" );
    commands.addCommands(
                Commands.slash("download", "Get links to download plugin")
                        .addOptions(opt)
                        .setGuildOnly(true),
            Commands.slash("review", "Get links to download plugin")
                    .addOptions(opt)
                    .addOption(OptionType.MENTIONABLE,"user","user", true)
                    .setGuildOnly(true).setDefaultPermissions(DefaultMemberPermissions.DISABLED),
            Commands.slash("reviewer", "Get links to download plugin")
                    .addOption(STRING,"spigot_username","spigot_username",true)
                    .addOptions(opt)
                    .setGuildOnly(true),
            Commands.slash("premium", "Get links to download plugin")
                    .addOption(STRING,"spigot_username","spigot_username",true)
                    .addOptions(opt2)
                    .setGuildOnly(true),
            Commands.slash("wiki","wiki link"),
            Commands.slash("website", "web link")
        ).queue();
        System.out.println("Slash commands registered successfully.");
    } catch (Exception e) {
        System.err.println("Error occurred while registering slash commands: " + e.getMessage());
    }


        }
    }
