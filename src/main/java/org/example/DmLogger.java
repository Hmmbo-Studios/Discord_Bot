package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class DmLogger extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!(event.getChannel() instanceof PrivateChannel)) {
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw();
        User sender = event.getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(sender.getName() + " sent a DM")
                .setDescription(content)
                .setAuthor(sender.getAsTag());

        List<Attachment> attachments = message.getAttachments();
        for (Attachment attachment : attachments) {
            if (attachment.isImage()) {
                embed.setImage(attachment.getUrl());
            } else if (attachment.isVideo()) {
                embed.setDescription(embed.getDescriptionBuilder() + "\nVideo: " + attachment.getUrl());
            } else {
                embed.setDescription(embed.getDescriptionBuilder() + "\nFile: " + attachment.getUrl());
            }
        }

        String channelId = IDS.dmschannel; // Replace with your actual channel ID
        MessageChannel channel = event.getJDA().getTextChannelById(channelId);
        if (channel != null) {
          //  channel.sendMessageEmbeds(embed.build()).queue();
        } else {
            System.out.println("Channel with ID " + channelId + " not found.");
        }
    }
}
