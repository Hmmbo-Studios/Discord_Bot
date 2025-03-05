package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

import java.awt.*;
import java.util.HashMap;

public class Suggestions extends ListenerAdapter {
    public static HashMap<String,String> downloadLinks = new HashMap<>();
    public static HashMap<String,String> imageLinks = new HashMap<>();
    public static HashMap<String, Color> colorHashMap = new HashMap<>();

    public void innitHashMap() {
        downloadLinks.put("Ultimate_BlockRegen","https://www.spigotmc.org/resources/ultimate-blockregen.110552/");
        downloadLinks.put("Mine_X_Farm_Regen", "https://www.spigotmc.org/resources/mine-x-farm-regen-1-17-1-20.107060/");
        downloadLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/resources/resourcepack-hub.113337/");
        downloadLinks.put("Modify_BrushDrop","https://www.spigotmc.org/resources/modify-brush-drop.114133/");


        imageLinks.put("Ultimate_BlockRegen","https://www.spigotmc.org/data/resource_icons/110/110552.jpg?1690298769");
        imageLinks.put("Mine_X_Farm_Regen","https://www.spigotmc.org/data/resource_icons/107/107060.jpg?1690299037");
        imageLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/data/resource_icons/113/113337.jpg?1698824916");
        imageLinks.put("Modify_BrushDrop","https://www.spigotmc.org/data/resource_icons/114/114133.jpg?1703656579");

        colorHashMap.put("Ultimate_BlockRegen",Color.ORANGE);
        colorHashMap.put("Mine_X_Farm_Regen",Color.YELLOW);
        float[] rp_color = Color.RGBtoHSB(248 / 255, 229 / 255, 231 / 255, null);
        colorHashMap.put("ResourcePack_Hub", Color.getHSBColor(rp_color[0],rp_color[1],rp_color[2]));
        colorHashMap.put("Modify_BrushDrop",Color.getHSBColor(281, 0.55f, 1f));
    }

    public Suggestions() {
        innitHashMap();
    }

    public static EmbedBuilder createEmbed(String Plugin, User user, String suggestion){
        return new EmbedBuilder()
                .setTitle("Suggestion For " +Plugin)
                .setDescription("Description")
                .setAuthor(user.getName(),null,user.getAvatarUrl())
                .setDescription( suggestion )
                .setColor(colorHashMap.get(Plugin))
                .setFooter("Suggested In Hmmbo's Developer" )
                .setThumbnail(imageLinks.get(Plugin));

    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(!IDS.suggestionid.equals(event.getChannel().getId())){
            return;
        }
        EmbedBuilder e =  createEmbed("Mine_X_Farm_Regen",event.getUser(),event.getMessage().getContentRaw());
        switch (event.getComponentId()){


            case "UBR": e= createEmbed("Ultimate_BlockRegen",event.getUser(),event.getMessage().getContentRaw().replaceAll("```","").replace("**Which Plugin Your Suggestion Based On?**\n",""));
                       break;

            case "MineXFarm":   e= createEmbed("Mine_X_Farm_Regen",event.getUser(),event.getMessage().getContentRaw().replaceAll("```","").replace("**Which Plugin Your Suggestion Based On?**\n",""));
                             break;
            case "RPH":e= createEmbed("ResourcePack_Hub",event.getUser(),event.getMessage().getContentRaw().replaceAll("```","").replace("**Which Plugin Your Suggestion Based On?**\n",""));
                break;
            case "MBD":e= createEmbed("Modify_BrushDrop",event.getUser(),event.getMessage().getContentRaw().replaceAll("```","").replace("**Which Plugin Your Suggestion Based On?**\n",""));
                break;
        }

        Message msg = event.getChannel().sendMessageEmbeds(e.build()).complete();
        msg.addReaction(Emoji.fromUnicode("⬆\uFE0F")).queue();
        msg.addReaction(Emoji.fromUnicode("⬇\uFE0F")).queue();
        ThreadChannel threadChannel = msg.createThreadChannel("What do you think about this suggestion? (Discuss Here)").complete();
        threadChannel.addThreadMember(event.getMember()).queue();
        event.getMessage().delete().queue();

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

        if (event.getAuthor().isBot()) return;
        System.out.println("Pass1");
        if (!event.getChannel().getId().equals(IDS.suggestionid)) return;
        System.out.println("Pass2");

        Message message = event.getMessage();
        String content = message.getContentRaw();

        ActionRow actionRow = ActionRow.of(
                Button.primary("MineXFarm", "Mine X Farm Regen"),
                Button.success("UBR", "Ultimate Block Regen"),
                Button.secondary("RPH", "ResourcePack Hub"),
                Button.secondary("MBD", "Modify BrushDrop")
        );

        event.getMessage().delete().queue();
        // Send message with buttons
        event.getChannel().sendMessage(
                "**Which Plugin Your Suggestion Based On?**\n```" + content + "```"
        ).setActionRow(actionRow.getComponents()).queue();





    }



}
