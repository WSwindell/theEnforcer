package theEnforcer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theEnforcer.powers.CommonPower;


public class ChaffAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int magicNumber;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded;

    public ChaffAction(final AbstractPlayer p, final boolean upgraded) {
        this.p = p;
        this.magicNumber = magicNumber;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        int count = AbstractDungeon.player.hand.size();
        int i;
        for (i = 0; i < count; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new ExhaustAction(1, true, true)
            );
        }
        isDone = true;
    }
}