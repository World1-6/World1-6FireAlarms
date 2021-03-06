package com.andrew121410.mc.world16firealarms.tabcomplete;

import com.andrew121410.mc.world16firealarms.FireAlarmTempo;
import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FireAlarmTab implements TabCompleter {

    private Map<String, SimpleFireAlarm> iFireAlarmMap;
    private List<String> soundsList;

    private World16FireAlarms plugin;

    public FireAlarmTab(World16FireAlarms plugin) {
        this.plugin = plugin;
        this.iFireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.soundsList = new ArrayList<>();
        
        for (Sound value : Sound.values()) {
            this.soundsList.add(value.name());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("firealarm") || !p.hasPermission("world16.firealarm")) {
            return null;
        }

        List<String> fireAlarmList = new ArrayList<>(this.iFireAlarmMap.keySet());

        if (args.length == 1) {
            return getContainsString(args[0], Arrays.asList("register", "delete", "alarm", "reset", "sound", "tempo", "trigger"));
        } else if (args[0].equalsIgnoreCase("register")) {
            if (args.length == 2) {
                return getContainsString(args[1], Arrays.asList("firealarm", "sign", "strobe"));
            } else if (args.length == 3 && args[1].equalsIgnoreCase("sign") || args[1].equalsIgnoreCase("strobe")) {
                return getContainsString(args[2], fireAlarmList);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                return getContainsString(args[1], Arrays.asList("firealarm", "strobe"));
            } else if (args.length == 3) {
                return getContainsString(args[2], fireAlarmList);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("sound")) {
            if (args.length == 2) {
                return getContainsString(args[1], fireAlarmList);
            } else if (args.length == 3) {
                return getContainsString(args[2], this.soundsList);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("tempo")) {
            List<String> tempos = new ArrayList<>();
            for (FireAlarmTempo value : FireAlarmTempo.values()) tempos.add(value.name());
            if (args.length == 2) {
                return getContainsString(args[1], fireAlarmList);
            } else if (args.length == 3) {
                return getContainsString(args[2], tempos);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("trigger")) {
            if (args.length == 2) {
                return getContainsString(args[1], fireAlarmList);
            }
            return null;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            return getContainsString(args[1], fireAlarmList);
        } else if (args[0].equalsIgnoreCase("alarm")) {
            if (args.length == 2) {
                return getContainsString(args[1], Arrays.asList("test", "ps"));
            } else if (args.length == 3) {
                return getContainsString(args[2], fireAlarmList);
            }
            return null;
        }
        return null;
    }

    public static List<String> getContainsString(String args, List<String> oldArrayList) {
        return StringUtil.copyPartialMatches(args, oldArrayList, new ArrayList<>());
    }
}
