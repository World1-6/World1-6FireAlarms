package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.FireAlarmReason;
import com.andrew121410.mc.world16firealarms.FireAlarmTempo;
import com.andrew121410.mc.world16firealarms.TroubleReason;
import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.sign.screen.ISignScreen;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

import java.util.Map;

public class FireAlarmSignOS implements ISignScreen {

    private final double version = 2.0;

    private final World16FireAlarms plugin;

    private final String name;
    private final String fireAlarmName;
    private final Map<String, SimpleFireAlarm> fireAlarmMap;
    private FireAlarmSignOSMenu currentMenu;

    public FireAlarmSignOS(World16FireAlarms plugin, String name, String fireAlarmName) {
        this.plugin = plugin;

        this.fireAlarmMap = this.plugin.getMemoryHolder().getFireAlarmMap();

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
        SimpleFireAlarm simpleFireAlarm = this.fireAlarmMap.get(this.fireAlarmName);

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
                simpleFireAlarm.alarm(new FireAlarmReason(TroubleReason.PANEL_TEST, "Panel Test"));
                player.sendMessage(Translate.color("Alarm should be going off currently."));
                return true;
            } else if (pointerLine == 2 && currentSide == 1) {
                player.sendMessage(Translate.color("NOT IMPLEMENTED."));
                return true;
            } else if (pointerLine == 3 && currentSide == 1) {
                simpleFireAlarm.reset();
                player.sendMessage(Translate.color("Fire alarm has been reset."));
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO) {
            if (pointerLine == 1 && currentSide == 1) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.MARCH_TIME);
                player.sendMessage(Translate.color("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.MARCH_TIME.name()));
                return true;
            } else if (pointerLine == 2 && currentSide == 1) {
                simpleFireAlarm.getFireAlarmSettings().setFireAlarmTempo(FireAlarmTempo.CODE_3);
                player.sendMessage(Translate.color("Fire Alarm: " + this.fireAlarmName + " tempo has changed to " + FireAlarmTempo.CODE_3.name()));
                return true;
            }
        } else if (this.currentMenu == FireAlarmSignOSMenu.ALARM_POPUP) {
            if (pointerLine == 2 && currentSide == 1) {
                simpleFireAlarm.reset();
                mainPage(signScreenManager);
                player.sendMessage(Translate.color("The fire alarm has been reset."));
                return true;
            }
        }

        return false;
    }

    public void mainPage(SignScreenManager signScreenManager) {
        this.currentMenu = FireAlarmSignOSMenu.MAIN_MENU;
        SignLayout signLayout = new SignLayout("Main", null);
        SignPage mainPage = new SignPage("MainPage", null, 0, 0, 3, null);
        mainPage.setLine(0, Translate.colorc("&6Bexar Systems"));
        mainPage.setLine(1, Translate.colorc("-Settings"));

        signLayout.addSignPage(mainPage);

        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settingsPage(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_MENU;
        SignLayout signLayout = new SignLayout("SettingsMain", "Main");
        SignPage signPage = new SignPage("Settings", null, 0, 0, 3, null);
        signPage.setLine(0, Translate.colorc("^&6Settings Page"));
        signPage.setLine(1, Translate.colorc("-Test fire alarm"));
        signPage.setLine(2, Translate.colorc("-Change tempo"));
        signPage.setLine(3, Translate.colorc("-Info"));
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_TestFireAlarm_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_TEST_FIREALARM;
        SignLayout signLayout = new SignLayout("TestFireAlarmMain", "SettingsMain");
        SignPage signPage = new SignPage("TestFireAlarm", null, 0, 0, 3, null);
        signPage.setLine(0, Translate.colorc("^&6TestFireAlarm"));
        signPage.setLine(1, Translate.colorc("-Alarm"));
        signPage.setLine(2, Translate.colorc("Trouble_NOT_IM"));
        signPage.setLine(3, Translate.colorc("-Reset"));
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_ChangeTempo_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_CHANGE_TEMPO;
        SignLayout signLayout = new SignLayout("ChangeTempoMain", "SettingsMain");
        SignPage signPage = new SignPage("ChangeTempo", null, 0, 0, 3, null);
        signPage.setLine(0, Translate.colorc("^&6ChangeTempo"));
        signPage.setLine(1, Translate.colorc("-MARCH_TIME"));
        signPage.setLine(2, Translate.colorc("-CODE_3"));
        signLayout.addSignPage(signPage);
        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void settings_Info_Page(SignScreenManager signScreenManager, Player player) {
        this.currentMenu = FireAlarmSignOSMenu.SETTINGS_INFO;
        Utils utils = new Utils();
        SignLayout signLayout = new SignLayout("InfoMain", "SettingsMain");
        SignPage signPage = new SignPage("Info", null, 0, 0, 3, null);
        SimpleFireAlarm iFireAlarm = this.fireAlarmMap.get(fireAlarmName);
        signPage.setLine(0, Translate.colorc("^&6Info"));
        signPage.setLine(1, Translate.colorc(this.fireAlarmName));
        signPage.setLine(2, Translate.colorc("Ver: " + this.version));
        signPage.setLine(3, Translate.colorc("P>NOS" + iFireAlarm.getStrobesMap().size()));

        SignPage signPage2 = new SignPage("Info2", "Info", 0, 0, 3, null);
        signPage2.setLine(0, Translate.colorc("Sound: {BELOW}"));
        // TODO Changed to toString() for Sound (which I have no idea if that's good or not)
        signPage2.setLine(1, Translate.colorc(utils.getLastStrings(iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getSound().toString(), 5)));
        signPage2.setLine(2, Translate.colorc("Volume: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getVolume()));
        signPage2.setLine(3, Translate.colorc("P>Pitch: " + iFireAlarm.getFireAlarmSettings().getFireAlarmSound().getPitch()));

        SignPage signPage3 = new SignPage("Info3", "Info2", 0, 0, 3, null);
        signPage3.setLine(0, Translate.colorc("Tempo: " + iFireAlarm.getFireAlarmSettings().getFireAlarmTempo().name()));

        signLayout.addSignPage(signPage);
        signLayout.addSignPage(signPage2);
        signLayout.addSignPage(signPage3);

        signScreenManager.updateLayoutAndPage(signLayout, 0);
    }

    public void sendPopup(SignScreenManager signScreenManager, FireAlarmReason fireAlarmReason) {
        this.currentMenu = FireAlarmSignOSMenu.ALARM_POPUP;
        SignLayout signLayout = new SignLayout("POPUP", "Main");
        SignPage signPage = new SignPage("Popup", null, 0, 0, 3, null);
        signPage.setLine(0, Translate.colorc("^&c&lPopup ALERT"));

        if (fireAlarmReason.getTroubleReason() == TroubleReason.PANEL_TEST) {
            signPage.setLine(1, Translate.colorc(fireAlarmReason.getTroubleReason().toString()));
            signPage.setLine(2, Translate.colorc("-Reset"));
        } else if (fireAlarmReason.getTroubleReason() == TroubleReason.PULL_STATION) {
            signPage.setLine(1, Translate.colorc(fireAlarmReason.getTroubleReason().toString()));
            signPage.setLine(2, Translate.colorc("-Reset"));
            signPage.setLine(3, Translate.colorc(fireAlarmReason.getReason()));
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