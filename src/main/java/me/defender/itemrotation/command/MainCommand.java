package me.defender.itemrotation.command;

import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.utils.ColorUtil;
import me.defender.itemrotation.API;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@BaseCommand(
        name = "itemrotation",
        description = "Main Command for Item Rotation Addon for Bedwars1058",
        aliases = {"ir", "itemro", "itro", "rotation"},
        tabComplete = true
)
public class MainCommand {


    @SubCommand(
    )
    public void helpMsg(CommandSender sender, String[] args){
        if(sender.hasPermission("itemroation.help") || sender.hasPermission("itemrotation.admin")){
            sender.sendMessage(ColorUtil.colored("&eCommands:"));
            sender.sendMessage(ColorUtil.colored("&e1. /itemrotation nextItem &7(When executed, will change the rotation item to next)"));
            sender.sendMessage(ColorUtil.colored("&e2. /itemrotation reload &7(When executed, reload's the config)"));
        }
    }

    @SubCommand(
            args = {"reload", "reloadConfig", "configReload"},
            permission = "itemroation.admin",
            permissionMessage = "§cYou don't have permission to do that!"
    )
    public void reloadConfig(CommandSender sender, String[] args){
        API.getMain().reloadConfig();
        sender.sendMessage(ColorUtil.colored("Config have been reloaded without any errors!"));
    }

    @SubCommand(
            args = "nextItem",
            permission = "itemrotation.admin",
            permissionMessage = "§cYou don't have permission to do that!"
    )
    public void nextItem(CommandSender sender, String[] args){
        int item_index = API.getMain().getConfig().getInt("item-index");
        if(item_index + 1 == API.items.size()){
            API.getMain().getConfig().set("item", API.items.get(0).defaultName());
        }else{
            API.getMain().getConfig().set("item", API.items.get(item_index + 1).defaultName());
        }
        API.getMain().getConfig().options().copyDefaults(true);
        API.getMain().saveConfig();
        API.updateSelectedItem(API.items, API.getMain().getConfig());
        item_index = API.getMain().getConfig().getInt("item-index");
        sender.sendMessage(ChatColor.GREEN + "Done! Rotation Item is set to " + API.items.get(item_index).defaultName() + "!");
    }
}
