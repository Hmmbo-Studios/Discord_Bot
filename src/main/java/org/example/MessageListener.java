package org.example;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
//    @Override
//    public void onMessageReceived(MessageReceivedEvent event)
//    {
//
//        if (event.getAuthor().isBot()) return;
//        Message message = event.getMessage();
//        String content = message.getContentRaw();
//        System.out.println(content);
//        if (content.equals("!ping"))
//        {
//            MessageChannel channel = event.getChannel();
//            channel.sendMessage("Pong!").queue();
//        }
//    }
}