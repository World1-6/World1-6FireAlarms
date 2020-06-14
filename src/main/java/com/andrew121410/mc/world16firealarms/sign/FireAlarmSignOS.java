package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.FireAlarmReason;
import com.andrew121410.mc.world16firealarms.FireAlarmTempo;
import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16firealarms.TroubleReason;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16utils.Utils;
import com.andrew121410.mc.world16utils.chat.LanguageLocale;
import com.andrew121410.mc.world16utils.sign.screen.ISignScreen;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.entity.Player;

import java.util.Map;

public class FireAlarmSignOS implements ISignScreen {

    private double version = 2.0;

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
    }

    @Override
    public boolean onDoneConstructed(SignScreenManager signScreenManager) {
        mainPage(signScreenManager);
        return true;
    }

    @Override
    public boolean onButton(SignScreenManager signScreenManager, Player player, SignLayout signLayout, SignPage signPage, int pointerLine, int currentSide) {
        SimpleFireAlarm simpleFireAlarm = (SimpleFireAlarm) this.fireAlarmMap.get(this.fireAlarmName);

        if (this.currentMenu == FireAlarmSignOSMenu.MAIN_MENU) {
            if (pointerLine == 1 && currentSide == 1) {
                settingsPage(signScreenManager, player);
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_MENU) {
            if (pointerLine == 1 && currentSide == 1) {
                settings_TestFireAlarm_Page(signScreenManager, player);
                return true;
            } else if (pointerLine == 2 && currentSide == 1) {
                settings_ChangeTempo_Page(signScreenManager, player);
                return true;
            } else if (pointerLine == 3 && currentSide == 1) {
                settings_Info_Page(signScreenManager, player);
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_TEST_FIREALARM) {
            if (pointerLine == 1 && currentSide == 1) {
                simpleFireAlarm.alarm(new FireAlarmReason(TroubleReason.PANEL_TEST));
                player.sendMessage(LanguageLocale.color("Alarm should be going off currently."));
                return true;
            } else if (pointerLine == 2 && currentSide == 1) {
                player.sendMessage(LanguageLocale.color("NOT IMPLEMENTED."));
                return true;
            } else if (pointerLine == 3 && currentSide == 1) {
                simpleFireAlarm.reset();
                player.sendMessage(LanguageLocale.color("Fire alarm has been reset."));
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO) {
            if (pointerLine == 1 && currentSide == 1) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.MARCH_TIME);
                player.sendMessage(LanguageLocale.color("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.MARCH_TIME.name()));
                return true;
            } else if (pointerLine == 2 && currentSide == 1) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.CODE_3);
                player.sendMessage(LanguageLocale.color("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.CODE_3.name()));
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.ALARM_POPUP) {
            if (pointerLine == 2 && currentSide == 1) {
                simpleFireAlarm.reset();
                mainPage(signScreenManager);
                player.sendMessage(LanguageLocale.color("The fire alarm has been reset."));
                return true;
            }
        }

        return false;
    }

    public void mainPage(SignScreenManager signScreenManager) {
        this.currentMenu = FireAlarmSignOSMenu.MAIN_MENU;
        SignLayout signLayout = new SignLayout("Main", null);
        SignPage mainPage = new SignPage("MainPage", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        mainPage.setLine(0, "&6Bexar Systems");
        mainPage.setLine(1, "-Settings");

        signLayout.addSignPage(mainPage);

        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settingsPage(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_MENU;
        SignLayout signLayout = new SignLayout("SettingsMain", "Main");
        SignPage signPage = new SignPage("Settings", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage.setLine(0, "^&6Settings Page");
        signPage.setLine(1, "-Test fire alarm");
        signPage.setLine(2, "-Change tempo");
        signPage.setLine(3, "-Info");
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_TestFireAlarm_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_TEST_FIREALARM;
        SignLayout signLayout = new SignLayout("TestFireAlarmMain", "SettingsMain");
        SignPage signPage = new SignPage("TestFireAlarm", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage.setLine(0, "^&6TestFireAlarm");
        signPage.setLine(1, "-Alarm");
        signPage.setLine(2, "Trouble_NOT_IM");
        signPage.setLine(3, "-Reset");
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_ChangeTempo_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO;
        SignLayout signLayout = new SignLayout("ChangeTempoMain", "SettingsMain");
        SignPage signPage = new SignPage("ChangeTempo", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage.setLine(0, "^&6ChangeTempo");
        signPage.setLine(1, "-MARCH_TIME");
        signPage.setLine(2, "-CODE_3");
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_Info_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_INFO;
        Utils utils = new Utils();
        SignLayout signLayout = new SignLayout("InfoMain", "SettingsMain");
        SignPage signPage = new SignPage("Info", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        IFireAlarm iFireAlarm = this.fireAlarmMap.get(fireAlarmName);
        signPage.setLine(0, "^&6Info");
        signPage.setLine(1, this.fireAlarmName);
        signPage.setLine(2, "Ver: " + this.version);
        signPage.setLine(3, "P>NOS" + iFireAlarm.getStrobesMap().size());

        SignPage signPage2 = new SignPage("Info2", "Info", 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage2.setLine(0, "Sound: {BELOW}");
        signPage2.setLine(1, utils.getLastStrings(iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getSound().name(), 5));
        signPage2.setLine(2, "Volume: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getVolume());
        signPage2.setLine(3, "P>Pitch: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getPitch());

        SignPage signPage3 = new SignPage("Info3", "Info2", 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage3.setLine(0, "Tempo: " + iFireAlarm.getFireAlarmSettings().getFireAlarmTempo().name());

        signLayout.addSignPage(signPage);
        signLayout.addSignPage(signPage2);
        signLayout.addSignPage(signPage3);

        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void sendPopup(SignScreenManager signScreenManager, FireAlarmReason fireAlarmReason) {
        this.currentMenu = FireAlarmSignOSMenu.ALARM_POPUP;
        SignLayout signLayout = new SignLayout("POPUP", "Main");
        SignPage signPage = new SignPage("Popup", null, 0, 0, 3, new String[]{"*", "*", "*", "*"});
        signPage.setLine(0, "^&c&lPopup ALERT");

        if (fireAlarmReason.getTroubleReason() == TroubleReason.PANEL_TEST) {
            signPage.setLine(1, fireAlarmReason.getTroubleReason().toString());
            signPage.setLine(2, "-Reset");
        } else if (fireAlarmReason.getTroubleReason() == TroubleReason.PULL_STATION) {
            signPage.setLine(1, fireAlarmReason.getTroubleReason().toString());
            signPage.setLine(2, "-Reset");

            fireAlarmReason.getOptionalPullStationName().ifPresent(ok -> signPage.setLine(3, ok));
        }
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
        signScreenManager.tick(); //Tick because it's an alert.
    }

    @Override
    public boolean nullPage(SignScreenManager signScreenManager, Player player, boolean up) {
        SignLayout signLayout = signScreenManager.getCurrentLayout();
        if (up) {
            if (signLayout.getReverseLayout() != null) {
                switch (signLayout.getReverseLayout()) {
                    case "Main":
                        mainPage(signScreenManager);
                        break;
                    case "SettingsMain":
                        settingsPage(signScreenManager, player);
                    default:
                        break;
                }
                return true;
            } else return false;
        }
        return false;
    }
}