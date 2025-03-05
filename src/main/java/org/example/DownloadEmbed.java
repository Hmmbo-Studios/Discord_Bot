package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.HashMap;

public class DownloadEmbed {
    public static HashMap<String,String> downloadLinks = new HashMap<>();
    public static HashMap<String,String> imageLinks = new HashMap<>();

    public void innitHashMap() {
        downloadLinks.put("Ultimate_BlockRegen","https://www.spigotmc.org/resources/ultimate-blockregen.110552/");
        downloadLinks.put("Mine_X_Farm_Regen", "https://www.spigotmc.org/resources/mine-x-farm-regen-1-17-1-20.107060/");
        downloadLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/resources/resourcepack-hub.113337/");
        downloadLinks.put("Modify_BrushDrop","https://www.spigotmc.org/resources/modify-brush-drop.114133/");


        imageLinks.put("Ultimate_BlockRegen","https://www.spigotmc.org/data/resource_icons/110/110552.jpg?1690298769");
        imageLinks.put("Mine_X_Farm_Regen","https://www.spigotmc.org/data/resource_icons/107/107060.jpg?1690299037");
        imageLinks.put("ResourcePack_Hub", "https://www.spigotmc.org/data/resource_icons/113/113337.jpg?1698824916");
        imageLinks.put("Modify_BrushDrop","https://www.spigotmc.org/data/resource_icons/114/114133.jpg?1703656579");
    }

    public DownloadEmbed() {
        innitHashMap();
    }

    public static EmbedBuilder createEmbed(String Plugin){

        String downloadlink = downloadLinks.get(Plugin);
        String imageLink = imageLinks.get(Plugin);

        return new EmbedBuilder()
                .setTitle(Plugin)
                .setDescription("Please click on the link to visit our plugin page\n"+ downloadlink)
                .setColor(Color.LIGHT_GRAY)
                .setFooter("Download Links", imageLink)
                .setThumbnail(imageLink);


    }
    public static EmbedBuilder createEmbed2(String Plugin ){

        String downloadlink = downloadLinks.get(Plugin);
        String imageLink = imageLinks.get(Plugin);

        return new EmbedBuilder()
                .setColor(new Color(0x0099FF))
                .setTitle("⭐ Leave a Review ⭐")
                .setDescription(
                        "Could you please leave a review for our plugin?\n" +
                                "Your feedback is greatly appreciated and helps us improve.\n" +
                                "Do /reviewer after Reviewing to access \nReviewer Role [Faster Support]\n\n" +
                                "⭐⭐⭐⭐⭐\n" +
                                "Review Here: [Plugin Link]("+downloadlink+")\n\n" +
                                "Thank You!!"
                )
                .setUrl(downloadlink)
                .setThumbnail(imageLink)
                .setTimestamp(OffsetDateTime.now());
    }
    public static void addRoleToUser(User user, Role role, Guild guild) {
        Member member = guild.getMember(user);
        if (member != null) {
            guild.addRoleToMember(member, role).queue(
                    success -> System.out.println("Role added successfully!"),
                    error -> System.err.println("Failed to add role: " + error.getMessage())
            );
        } else {
            System.err.println("User is not a member of the guild.");
        }
    }

}
