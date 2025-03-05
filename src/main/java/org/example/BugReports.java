package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.HashMap;

public class BugReports extends ListenerAdapter {

    public static HashMap<String, String> downloadLinks = new HashMap<>();
    public static HashMap<String, String> imageLinks = new HashMap<>();
    public static HashMap<String, Color> colorHashMap = new HashMap<>();

    public void initHashMap() {
        downloadLinks.put("Ultimate_BlockRegen", "https://www.spigotmc.org/resources/ultimate-blockregen.110552/");
        downloadLinks.put("Mine_X_Farm_Regen", "https://www.spigotmc.org/resources/mine-x-farm-regen-1-17-1-20.107060/");
        downloadLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/resources/resourcepack-hub.113337/");
        downloadLinks.put("Modify_BrushDrop", "https://www.spigotmc.org/resources/modify-brush-drop.114133/");

        imageLinks.put("Ultimate_BlockRegen", "https://www.spigotmc.org/data/resource_icons/110/110552.jpg?1690298769");
        imageLinks.put("Mine_X_Farm_Regen", "https://www.spigotmc.org/data/resource_icons/107/107060.jpg?1690299037");
        imageLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/data/resource_icons/113/113337.jpg?1698824916");
        imageLinks.put("Modify_BrushDrop", "https://www.spigotmc.org/data/resource_icons/114/114133.jpg?1703656579");

        colorHashMap.put("Ultimate_BlockRegen", Color.ORANGE);
        colorHashMap.put("Mine_X_Farm_Regen", Color.YELLOW);
        float[] rp_color = Color.RGBtoHSB(248, 229, 231, null);
        colorHashMap.put("ResourcePack_Hub", Color.getHSBColor(rp_color[0], rp_color[1], rp_color[2]));
        colorHashMap.put("Modify_BrushDrop",Color.LIGHT_GRAY);
    }

    public BugReports() {
        initHashMap();
    }

    public static EmbedBuilder createEmbed(String plugin, String bug) {
        return new EmbedBuilder()
                .setTitle("Bug Report : " + plugin)
                .setDescription(bug)
                .setColor(colorHashMap.get(plugin))
                .setFooter("Suggested by Hmmbo's Developer")
                .setThumbnail(imageLinks.get(plugin));
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (!IDS.BugReport.contains(event.getChannel().getId())) {
            return;
        }

        EmbedBuilder e;
        switch (event.getComponentId()) {
            case "UBR":
                e = createEmbed("Ultimate_BlockRegen", event.getMessage().getContentRaw().replaceAll("```", "").replace("**Which Plugin Your Bug is Based On?**\n", ""));
                break;
            case "MineXFarm":
                e = createEmbed("Mine_X_Farm_Regen", event.getMessage().getContentRaw().replaceAll("```", "").replace("**Which Plugin Your Bug is  Based On?**\n", ""));
                break;
            case "RPH":
                e = createEmbed("ResourcePack_Hub", event.getMessage().getContentRaw().replaceAll("```", "").replace("**Which Plugin Your Bug is  Based On?**\n", ""));
                break;
            case "MBD":
                e = createEmbed("Modify_BrushDrop", event.getMessage().getContentRaw().replaceAll("```", "").replace("**Which Plugin Your Bug is  Based On?**\n", ""));
                break;
            case "Verified":
                changeEmbedTitleAndColor(event, "Verified Bug", Color.RED);
                return; // Exit early after modifying embed
            case "NonVerified":
                changeEmbedTitleAndColor(event, "Yet to verify Bug", Color.ORANGE);
                return; // Exit early after modifying embed
            case "Solved":
                changeEmbedTitleAndColor(event, "Solved Bug", Color.GREEN);
                return; // Exit early after modifying embed
            case "NotBug":
                changeEmbedTitleAndColor(event, "Not a Bug", Color.GRAY);
                return; // Exit early after modifying embed
            default:
                event.reply("Unknown action.").setEphemeral(true).queue();
                return; // Exit early for unknown actions
        }

        // Send the embed message and set up actions
        ActionRow actionRow = ActionRow.of(
                Button.primary("Verified", "Verified"),
                Button.success("Solved", "Solved"),
                Button.secondary("NonVerified", "NonVerified"),
                Button.secondary("NotBug", "NotBug")
        );

        Message msg = event.getChannel().sendMessageEmbeds(e.build()).setActionRow(actionRow.getComponents()).complete();
        msg.addReaction(Emoji.fromUnicode("⬆\uFE0F")).queue();
        msg.addReaction(Emoji.fromUnicode("⬇\uFE0F")).queue();

        ThreadChannel threadChannel = msg.createThreadChannel(event.getUser().getName() + "'s Bug Discussion").complete();
        threadChannel.addThreadMember(event.getMember()).queue();
        event.getMessage().delete().queue();
    }

    private void changeEmbedTitleAndColor(ButtonInteractionEvent event, String newTitle, Color newColor) {
        if(!event.getUser().getId().equals("850394179686105139")){
            return;
        }
        MessageEmbed originalEmbed = event.getMessage().getEmbeds().get(0); // Assuming only one embed

        EmbedBuilder newEmbedBuilder = new EmbedBuilder(originalEmbed)
                .setTitle(newTitle)
                .setColor(newColor);

        event.editMessageEmbeds(newEmbedBuilder.build()).queue();
        //event.reply("Embed title and color updated!").setEphemeral(true).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!IDS.BugReport.contains(event.getChannel().getId())) {
            return;
        }

        String content = event.getMessage().getContentRaw();

        ActionRow actionRow = ActionRow.of(
                Button.primary("MineXFarm", "Mine X Farm Regen"),
                Button.primary("UBR", "Ultimate Block Regen"),
                Button.secondary("RPH", "ResourcePack Hub"),
                Button.secondary("MBD", "Modify BrushDrop")
        );

        event.getMessage().delete().queue();

        event.getChannel().sendMessage("**Which Plugin Your Bug Based On?**\n```" + content + "```")
                .setActionRow(actionRow.getComponents()).queue();
    }
}
