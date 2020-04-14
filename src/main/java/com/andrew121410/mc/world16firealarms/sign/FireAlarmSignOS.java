package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.FireAlarmReason;
import com.andrew121410.mc.world16firealarms.FireAlarmTempo;
import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16firealarms.TroubleReason;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.sign.screen.ISignScreen;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireAlarmSignOS implements ISignScreen {

    private double version = 1.2;

    private Main plugin;

    private String name;
    private String fireAlarmName;

    private FireAlarmSignOSMenu currentMenu;

    private Map<String, IFireAlarm> fireAlarmMap;

    public FireAlarmSignOS(Main plugin, String name, String fireAlarmName) {
        this.plugin = plugin;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.name = name;
        this.fireAlarmName = fireAlarmName;

        this.currentMenu = FireAlarmSignOSMenu.OFF;
    }

    @Override
    public boolean onButton(SignScreenManager signScreenManager, Player player, int line, int scroll) {
        SimpleFireAlarm simpleFireAlarm = (SimpleFireAlarm) this.fireAlarmMap.get(this.fireAlarmName);
        Sign sign = signScreenManager.getSign();

        if (player == null && line == 0 && scroll == 0) {
            this.loadFirstTime(signScreenManager, sign);
        }

        //Reverse
        if (line == 0 && scroll == 0) {
            backReverse(this.currentMenu, signScreenManager, sign, line);
            return true;
        }

        if (scroll >= 1) {
//            TODO implement scrolling features.
            return true;
        }

        if (this.currentMenu == FireAlarmSignOSMenu.MAIN_MENU) {
            if (line == 1) {
                settings_menu(signScreenManager, sign);
                return true;
            }
            return true;
            //SETTINGS
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_MENU) {
            if (line == 1) {
                settings_menu_test_firealarm(signScreenManager, sign);
                return true;
            } else if (line == 2) {
                settings_menu_change_tempo(signScreenManager, sign);
                return true;
            } else if (line == 3) {
                settings_menu_info(signScreenManager, sign);
                return true;
            }
            return true;
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_TEST_FIREALARM) {
            if (line == 1) {
                simpleFireAlarm.alarm(new FireAlarmReason(TroubleReason.PANEL_TEST));
                player.sendMessage(Translate.chat("Alarm should be going off currently."));
                return true;
            } else if (line == 2) {
                player.sendMessage(Translate.chat("NOT IMPLEMENTED."));
                return true;
            } else if (line == 3) {
                simpleFireAlarm.reset();
                player.sendMessage(Translate.chat("Fire alarm has been reset."));
                return true;
            }
            return true;
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO) {
            if (line == 1) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.MARCH_TIME);
                player.sendMessage(Translate.chat("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.MARCH_TIME.name()));
                return true;
            } else if (line == 2) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.CODE_3);
                player.sendMessage(Translate.chat("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.CODE_3.name()));
                return true;
            }
            return true;
        } else if (this.currentMenu == FireAlarmSignOSMenu.ALARM_POPUP) {
            if (line == 2) {
                simpleFireAlarm.reset();
                backReverse(this.currentMenu, signScreenManager, sign, line);
                player.sendMessage(Translate.chat("The fire alarm has been reset."));
                return true;
            }
        }
        return true;
    }

    public void sendPopup(SignScreenManager signScreenManager, Sign sign, FireAlarmReason fireAlarmReason) {
        this.currentMenu = FireAlarmSignOSMenu.ALARM_POPUP;
        if (fireAlarmReason.getTroubleReason() == TroubleReason.PANEL_TEST) {
            List<String> stringList = new ArrayList<>();
            stringList.add("Popup/MENU");
            stringList.add(fireAlarmReason.getTroubleReason().toString());
            stringList.add("-Reset");
            stringList.add("");

            signScreenManager.setLine(0);
            signScreenManager.setMin(0);
            signScreenManager.setMax(3);

            signScreenManager.updateSign(sign, stringList);
        } else if (fireAlarmReason.getTroubleReason() == TroubleReason.PULL_STATION) {
            List<String> stringList = new ArrayList<>();
            stringList.add("Popup/MENU");
            stringList.add(fireAlarmReason.getTroubleReason().toString());
            stringList.add("-Reset");

            fireAlarmReason.getOptionalPullStationName().ifPresent(stringList::add);

            signScreenManager.setLine(0);
            signScreenManager.setMin(0);
            signScreenManager.setMax(3);

            signScreenManager.updateSign(sign, stringList);
        }
    }

    private void backReverse(FireAlarmSignOSMenu menu, SignScreenManager signScreenManager, Sign sign, int line) {
        switch (menu) {
            case SETTINGS_MENU:
                main_menu(signScreenManager, sign);
                break;
            case ALARM_POPUP:
                main_menu(signScreenManager, sign);
                break;
            case SETTINGS_TEST_FIREALARM:
            case SETTINGS_CHANGE_TEMPO:
            case SETTINGS_INFO:
                settings_menu(signScreenManager, sign);
                break;
        }
    }

    public void resetSign(SignScreenManager signScreenManager, Sign sign, boolean backToMainMenu) {
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");

        signScreenManager.setLine(0);
        signScreenManager.setMin(0);
        signScreenManager.setMax(3);

        signScreenManager.updateSign(sign);

        if (backToMainMenu) main_menu(signScreenManager, sign);
    }

    public void loadFirstTime(SignScreenManager SignScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.WAITING;

        sign.setLine(0, "Bexar-Systems");
        sign.setLine(1, "Version: " + this.version);
        sign.setLine(2, "Loading data...");
        sign.setLine(3, "Please wait...");
        SignScreenManager.updateSign(sign);

        new BukkitRunnable() {
            @Override
            public void run() {
                resetSign(SignScreenManager, sign, true);
            }
        }.runTaskLater(this.plugin, 100L);
    }

    private void main_menu(SignScreenManager signScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.MAIN_MENU;
        sign.setLine(0, "Bexar-Systems");
        sign.setLine(1, "-Settings");
        sign.setLine(2, "");
        sign.setLine(3, "");

        signScreenManager.setMin(1);
        signScreenManager.setLine(1);
        signScreenManager.updateSign(sign);
    }

    private void settings_menu(SignScreenManager signScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_MENU;
        sign.setLine(0, "Settings/MENU");
        sign.setLine(1, "-Test Fire Alarm");
        sign.setLine(2, "-Change Tempo");
        sign.setLine(3, "-Info");

        signScreenManager.setMin(0);
        signScreenManager.setLine(0);
        signScreenManager.updateSign(sign);
    }

    private void settings_menu_test_firealarm(SignScreenManager signScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_TEST_FIREALARM;
        sign.setLine(0, "Settings/Test");
        sign.setLine(1, "-Alarm");
        sign.setLine(2, "Trouble");
        sign.setLine(3, "-Reset");
        signScreenManager.setMin(0);
        signScreenManager.setLine(0);
        signScreenManager.updateSign(sign);
    }

    private void settings_menu_change_tempo(SignScreenManager signScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO;
        sign.setLine(0, "Settings/Tempo");
        sign.setLine(1, "-MARCH_TIME");
        sign.setLine(2, "-CODE3");
        sign.setLine(3, "");

        signScreenManager.setMin(0);
        signScreenManager.setMax(3);
        signScreenManager.setLine(0);

        signScreenManager.updateSign(sign);
    }

    public void settings_menu_info(SignScreenManager signScreenManager, Sign sign) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_INFO;
        IFireAlarm iFireAlarm = this.fireAlarmMap.get(fireAlarmName);
        List<String> stringList = new ArrayList<>();

        stringList.add("Settings/Info");
        stringList.add(this.fireAlarmName);
        stringList.add("Version: " + this.version);
        stringList.add("B2>NOS: " + iFireAlarm.getStrobesMap().size());

        stringList.add("Sound: {below}");
        stringList.add(iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getSound().name());
        stringList.add("Volume: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getVolume());
        stringList.add("Pitch: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getPitch());

        stringList.add("Tempo: " + iFireAlarm.getFireAlarmSettings().getFireAlarmTempo().name());

        signScreenManager.setMin(0);
        signScreenManager.setMax(3);
        signScreenManager.setLine(0);
        signScreenManager.updateSign(sign, stringList);
    }
}