package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

import static org.example.DownloadEmbed.addRoleToUser;

public class DownloadCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {
            case "download" -> {
                new DownloadEmbed();
                String content = event.getOption("plugin", OptionMapping::getAsString);
                EmbedBuilder embed = DownloadEmbed.createEmbed(content);
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            }

            case "review" -> {
                new DownloadEmbed();
                String content = event.getOption("plugin", OptionMapping::getAsString);
                User user = event.getOption("user", OptionMapping::getAsUser);
                EmbedBuilder embed = DownloadEmbed.createEmbed2(content);
                // event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embed.build()))
                        .queue(
                                success -> System.out.println("Message sent successfully!"),
                                error -> System.err.println("Failed to send message: " + error.getMessage())
                        );
            }

            case "reviewer" -> {

                    Button roleAButton = Button.primary("give_role_a:"+event.getUser().getId(), "Give Reviewer Role");
              //      Button roleBButton = Button.primary("give_role_b", "Give Role B");
                    Button rejectButton = Button.danger("reject", "Reject");

                    String input = event.getOption("spigot_username").getAsString();
                    String plugin = event.getOption("plugin").getAsString();
                    String userTag = event.getUser().getAsTag();

                    event.getJDA().getTextChannelById(IDS.staffchannel).sendMessage(
                                    "User " + userTag + " requested reviewer role for "+ plugin+"\nSpigot Name : " + input + "\nChoose an action:")
                            .setActionRow(roleAButton,rejectButton)
                            .queue();

                    event.reply("Your request has been sent to the staff.").setEphemeral(true).queue();

            }
            case "premium" -> {

             //   Button roleAButton = Button.primary("give_role_a", "Give Role A");
                Button roleBButton = Button.primary("give_role_b:"+event.getUser().getId(), "Give Premium Role");
                Button rejectButton = Button.danger("reject", "Reject");

                String input = event.getOption("spigot_username").getAsString();
                String plugin = event.getOption("plugin").getAsString();
                String userTag = event.getUser().getAsTag();

                event.getJDA().getTextChannelById(IDS.staffchannel).sendMessage(
                                "User " + userTag + " requested premium role for "+ plugin+"\nSpigot Name : " + input + "\nChoose an action:")
                        .setActionRow(roleBButton,rejectButton)
                        .queue();

                event.reply("Your request has been sent to the staff.").setEphemeral(true).queue();

            }

            case "wiki" -> {
                String wikiLink = "https://wiki.hmmbo.com";
                String description = "Explore the vast knowledge of our wiki!";

                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Wiki Link")
                        .setColor(Color.white)
                        .setDescription(description)
                        .addField("Link", "[Click here](" + wikiLink + ")", false)
                        .setFooter("External Link")
                        .build();

                event.replyEmbeds(embed).queue();
            }

            case "website" -> {
                String websiteLink = "https://hmmbo.com";
                String description = "Discover more on our official website!";

                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Website Link")
                        .setColor(Color.white)
                        .setDescription(description)
                        .addField("Link", "[Click here](" + websiteLink + ")", false)
                        .setFooter("External Link")
                        .build();

                event.replyEmbeds(embed).queue();
            }

            default -> {
                return;
            }


        }
    }
}