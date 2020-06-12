package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.FireAlarmReason;
import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16utils.sign.screen.ISignScreen;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignLayout;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignOS;
import com.andrew121410.mc.world16utils.sign.screen.pages.SignPage;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Map;

public class FireAlarmSignOS implements ISignScreen {

    private double version = 2.0;

    private Main plugin;

    private String name;
    private String fireAlarmName;

    private FireAlarmSignOSMenu currentMenu;

    private Map<String, IFireAlarm> fireAlarmMap;

    private SignOS signOS;

    public FireAlarmSignOS(Main plugin, String name, String fireAlarmName) {
        this.plugin = plugin;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.name = name;
        this.fireAlarmName = fireAlarmName;
    }

    @Override
    public SignOS getSignOS() {
        SignOS signOS = new SignOS("FireAlarmOS");
        //Main Layout;
        SignLayout signLayout = new SignLayout("Main", null);
        SignPage signPage = new SignPage("Main", null, 0, 0, 3);
        signPage.setLine(0, "&6Bexar system");
        signPage.setLine(1, "-&8Settings");

        SignPage signPage1 = new SignPage("SecondPage", "Main", 0, 0, 3);
        signPage1.setLine(0, "This is a");
        signPage1.setLine(1, "new page.");
        signLayout.addSignPage(signPage);
        signLayout.addSignPage(signPage1);
        signOS.putSignLayout(signLayout);
        //;
        return signOS;
    }

    @Override
    public boolean onDoneConstructed(SignScreenManager signScreenManager) {
        this.currentMenu = FireAlarmSignOSMenu.OFF;
        return true;
    }

    @Override
    public boolean onButton(SignScreenManager signScreenManager, Player player, SignLayout signLayout, SignPage signPage, int pointerLine) {
        return false;
    }

    public void resetSign(SignScreenManager signScreenManager, Sign sign, boolean backToMainMenu) {
    }


    public void sendPopup(SignScreenManager signScreenManager, Sign sign, FireAlarmReason fireAlarmReason) {

    }
}