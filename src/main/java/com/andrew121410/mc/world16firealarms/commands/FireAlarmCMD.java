package com.andrew121410.mc.world16firealarms.commands;

import com.andrew121410.mc.world16firealarms.*;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.interfaces.IStrobe;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16firealarms.simple.SimpleStrobe;
import com.andrew121410.mc.world16firealarms.tabcomplete.FireAlarmTab;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class FireAlarmCMD implements CommandExecutor {

    private final World16FireAlarms plugin;
    private final Utils api;

    //Maps
    private final Map<String, IFireAlarm> fireAlarmMap;
    private final Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    //...

    public FireAlarmCMD(World16FireAlarms plugin) {
        this.plugin = plugin;

        this.api = new Utils();

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getCommand("firealarm").setExecutor(this);
        this.plugin.getCommand("firealarm").setTabCompleter(new FireAlarmTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }

            BlockCommandSender cmdblock = (BlockCommandSender) sender;
            Block commandblock = cmdblock.getBlock();

            if (args.length == 4 && args[0].equalsIgnoreCase("alarm")) {
                String name = args[2].toLowerCase();
                String pullstationname = args[3];

                if (this.fireAlarmMap.get(name) == null) {
                    return true;
                }

                FireAlarmReason fireAlarmReason = new FireAlarmReason(TroubleReason.PULL_STATION);
                fireAlarmReason.setOptionalPullStationName(Optional.of(pullstationname));
                this.fireAlarmMap.get(name).alarm(fireAlarmReason);
                return true;
            }
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.firealarm")) {
            p.sendMessage(Translate.chat("&bYou don't have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("/firealarm register"));
            p.sendMessage(Translate.chat("/firealarm delete"));
            p.sendMessage(Translate.chat("/firealarm alarm <FireAlarmName>"));
            p.sendMessage(Translate.chat("/firealarm reset <FireAlarmName>"));
            return true;
        } else if (args[0].equalsIgnoreCase("register")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm register firealarm <Name>"));
                p.sendMessage(Translate.chat("/firealarm register sign <FireAlarmName> <Name>"));
                p.sendMessage(Translate.chat("/firealarm register strobe <FireAlarmName> <StrobeName>"));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("firealarm")) {
                String name = args[2].toLowerCase();
                Chunk chunk = p.getLocation().getChunk();
                IFireAlarm iFireAlarm = new SimpleFireAlarm(plugin, name, new Location(chunk.getWorld(), chunk.getX(), 0, chunk.getZ()));
                this.fireAlarmMap.putIfAbsent(name, iFireAlarm);
                p.sendMessage(Translate.chat("Fire Alarm: " + name + " is now registered."));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("sign")) {
                String fireAlarmName = args[2].toLowerCase();
                String signName = args[3].toLowerCase();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                Location location = PlayerUtils.getBlockPlayerIsLookingAt(p).getLocation();
                FireAlarmScreen fireAlarmScreen = new FireAlarmScreen(plugin, signName, fireAlarmName, location);
                this.fireAlarmScreenMap.putIfAbsent(location, fireAlarmScreen);
                this.fireAlarmMap.get(fireAlarmName).registerSign(signName, fireAlarmScreen);
                p.sendMessage(Translate.chat("The sign: " + signName + " is now registered to " + fireAlarmName));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("strobe")) {
                String firealarmName = args[2].toLowerCase();
                String strobeName = args[3].toLowerCase();

                if (this.fireAlarmMap.get(firealarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + firealarmName));
                    return true;
                }

                Location location = PlayerUtils.getBlockPlayerIsLookingAt(p).getLocation();
                this.fireAlarmMap.get(firealarmName).registerStrobe(new SimpleStrobe(location, strobeName));
                p.sendMessage(Translate.chat("Strobe: " + strobeName + " is now registered with the fire alarm: " + firealarmName));
                return true;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm delete firealarm <Name>"));
                p.sendMessage(Translate.chat("/fiealarm delete strobe <FireAlarmName> <Name>"));
                p.sendMessage(Translate.chat("/firealarm delete strobe <FireAlarmName>"));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("firealarm")) {
                String fireAlarmName = args[2].toLowerCase();

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                this.plugin.getFireAlarmManager().deleteFireAlarm(fireAlarmName);
                p.sendMessage(Translate.chat("Fire alarm: " + fireAlarmName + " has been deleted."));
                return true;
            } else if (args.length == 4 && args[1].equalsIgnoreCase("strobe")) {
                String fireAlarmName = args[2].toLowerCase();
                String strobeName = args[3].toLowerCase();

                IFireAlarm iFireAlarm = this.fireAlarmMap.get(fireAlarmName);
                if (iFireAlarm == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }


                if (!this.fireAlarmMap.get(fireAlarmName).getStrobesMap().containsKey(strobeName)) {
                    p.sendMessage(Translate.chat("THere's no such strobe named: " + strobeName));
                    return true;
                }

                iFireAlarm.getStrobesMap().remove(strobeName);
                p.sendMessage(Translate.chat("The strobe: " + strobeName + " for the fire alarm: " + fireAlarmName + " has been removed."));
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("strobe")) {
                String fireAlarmName = args[2].toLowerCase();
                Location location = PlayerUtils.getBlockPlayerIsLookingAt(p).getLocation();

                IFireAlarm iFireAlarm = this.fireAlarmMap.get(fireAlarmName);
                if (iFireAlarm == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + fireAlarmName));
                    return true;
                }

                IStrobe iStrobe = null;

                for (Map.Entry<String, IStrobe> entry : this.fireAlarmMap.get(fireAlarmName).getStrobesMap().entrySet()) {
                    String k = entry.getKey();
                    IStrobe v = entry.getValue();
                    int x = v.getLocation().getBlockX();
                    int y = v.getLocation().getBlockY();
                    int z = v.getLocation().getBlockZ();

                    int x1 = location.getBlockX();
                    int y1 = location.getBlockY();
                    int z1 = location.getBlockZ();

                    if (x == x1 && y == y1 && z == z1) {
                        p.sendMessage(Translate.chat("FOUND"));
                        iStrobe = v;
                    }
                }

                if (iStrobe == null) {
                    p.sendMessage(Translate.chat("Could not find..."));
                    return true;
                }
                iFireAlarm.getStrobesMap().remove(iStrobe.getName());
                p.sendMessage(Translate.chat("The strobe: " + iStrobe.getName() + " has been deleted from fire alarm: " + fireAlarmName));
                return true;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("load")) {
            this.plugin.getFireAlarmManager().loadAllFireAlarms();
            p.sendMessage(Translate.chat("Fire alarm's have been loaded in memory."));
            return true;
        } else if (args[0].equalsIgnoreCase("unload")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm unload <Save?True/False>"));
                return true;
            }
            boolean bool = Utils.asBooleanOrElse(args[1], true);

            if (bool) {
                this.plugin.getFireAlarmManager().saveAllFireAlarms();
            }

            this.fireAlarmMap.clear();
            this.fireAlarmScreenMap.clear();
            p.sendMessage(Translate.chat("Fire alarm's has been unloaded save: " + bool));
            return true;
        } else if (args[0].equalsIgnoreCase("alarm")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm alarm test <FireAlarmName>"));
                p.sendMessage(Translate.chat("/firealarm alarm ps <FireAlarmName> <PullStationName>"));
                return true;
            } else if (args.length == 3) {
                String name = args[2].toLowerCase();

                if (this.fireAlarmMap.get(name) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + name));
                    return true;
                }

                this.fireAlarmMap.get(name).alarm(new FireAlarmReason(TroubleReason.PANEL_TEST));
                p.sendMessage(Translate.chat("Alright, the fire alarm should be going off currently."));
                return true;
            } else if (args.length == 4) {
                String name = args[2].toLowerCase();
                String pullstationname = args[3];

                if (this.fireAlarmMap.get(name) == null) {
                    p.sendMessage(Translate.chat("There's no such fire alarm called: " + name));
                    return true;
                }

                FireAlarmReason fireAlarmReason = new FireAlarmReason(TroubleReason.PULL_STATION);
                fireAlarmReason.setOptionalPullStationName(Optional.of(pullstationname));
                this.fireAlarmMap.get(name).alarm(fireAlarmReason);
                p.sendMessage(Translate.chat("Alright, the fire alarm should be going off currently."));
                return true;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            String name = args[1].toLowerCase();

            if (this.fireAlarmMap.get(name) == null) {
                p.sendMessage(Translate.chat("There's no such fire alarm called: " + name));
                return true;
            }

            this.fireAlarmMap.get(name).reset();
            p.sendMessage(Translate.chat("The fire alarm: " + name + " has been resetedede"));
            return true;
        } else if (args[0].equalsIgnoreCase("sound")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm sound <FireAlarmName> <Sound> <Volume> <Pitch>"));
                return true;
            } else if (args.length == 5) {
                String fireAlarmName = args[1].toLowerCase();
                String sound = args[2];
                String volume = args[3];
                String pitch = args[4];

                Sound realSound = Sound.valueOf(sound);
                float realVolume = Utils.asFloatOrElse(volume, 99.1F);
                float realPitch = Utils.asFloatOrElse(pitch, 99.1F);

                if (realVolume == 99.1F) {
                    return true;
                }

                if (realPitch == 99.1F) {
                    return true;
                }

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("Not a fire alarm."));
                    return true;
                }

                FireAlarmSound fireAlarmSound = new FireAlarmSound(realSound, realVolume, realPitch);
                this.fireAlarmMap.get(fireAlarmName).getFireAlarmSettings().setFireAlarmSound(fireAlarmSound);
                p.sendMessage(Translate.chat("Fire alarm sound has been set for " + fireAlarmName));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("tempo")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm tempo <FireAlarmName> <Tempo>"));
                return true;
            } else if (args.length == 3) {
                String fireAlarmName = args[1].toLowerCase();
                String temp = args[2];

                FireAlarmTempo fireAlarmTempo = FireAlarmTempo.valueOf(temp);

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("Not a fire alarm."));
                    return true;
                }

                this.fireAlarmMap.get(fireAlarmName).getFireAlarmSettings().setFireAlarmTempo(fireAlarmTempo);
                p.sendMessage(Translate.chat("Fire Alarm: " + fireAlarmName + " tempo has changed to " + fireAlarmTempo));
                return true;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("trigger")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/firealarm trigger <FireAlarm> <Command?NULL>"));
            } else if (args.length >= 3) {
                String fireAlarmName = args[1].toLowerCase();
                String command = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                if (this.fireAlarmMap.get(fireAlarmName) == null) {
                    p.sendMessage(Translate.chat("Not a fire alarm."));
                    return true;
                }

                p.sendMessage(Translate.chat("Trigger has been set to: " + command));
                this.fireAlarmMap.get(fireAlarmName).getFireAlarmSettings().setCommandTrigger(command);
                return true;
            }
        }
        return true;
    }
}
