package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GetRole  extends ListenerAdapter {

            @Override
            public void onButtonInteraction(ButtonInteractionEvent event) {
                System.out.println("Passed bef fun");
                if(!event.getChannel().getId().equals(IDS.staffchannel)){
                    return;
                }
                String buttonId = event.getButton().getId();
                System.out.println("Passed into fun");
                if (buttonId == null) return;
                System.out.println("Passed into null chekc");
                String[] parts = buttonId.split(":");
                if (parts.length < 2) return;
                String action = parts[0];
                String userId = parts[1];
                System.out.println(userId);
                event.getJDA().retrieveUserById(userId).queue(
                        requester -> {
                            if (requester == null) {
                                event.reply("The user could not be found.").setEphemeral(true).queue();
                                return;
                            }

                if (requester == null) {
                    event.reply("The user could not be found.").setEphemeral(true).queue();
                    return;
                }

                System.out.println(requester + action + userId);
                System.out.println("Passed into switch");

                switch (action) {
                    case "give_role_a":
                        // Give Role A
                        event.getGuild().addRoleToMember(UserSnowflake.fromId(userId), event.getGuild().getRoleById(IDS.ReviewRole)).queue();
                        event.reply("Review Role has been assigned to the user!").queue();

                        sendDM(requester, "Your request for Review Role has been approved.");
                        break;
                    case "give_role_b":
                        // Give Role B
                        event.getGuild().addRoleToMember(UserSnowflake.fromId(userId), event.getGuild().getRoleById(IDS.PaidRole)).queue();
                        event.reply("Premium Role has been assigned to the user!").queue();

                        sendDM(requester, "Your request for Premium Role has been approved.");
                        break;
                    case "reject":
                        // Reject and send a DM
                        event.getMessage().delete().queue();
                        break;
                    default:
                        event.reply("Unknown action.").queue();
                        break;
                }
                        },
                        failure -> {
                            event.reply("Failed to retrieve the user.").setEphemeral(true).queue();
                        }
                );
}

    private void sendDM(User user, String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }
}