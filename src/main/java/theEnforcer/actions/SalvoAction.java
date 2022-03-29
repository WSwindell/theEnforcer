package theEnforcer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import theEnforcer.powers.CommonPower;

public class SalvoAction extends AbstractGameAction {
    public int[] multiDamage;
    private boolean freeToPlayOnce = false;
    private int energyOnUse = -1;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int magicNumber;

    public SalvoAction(AbstractPlayer p, int[] multiDamage,
                       DamageInfo.DamageType damageType,
                       boolean freeToPlayOnce, int energyOnUse, int magicNumber) {

        this.multiDamage = multiDamage;
             this.damageType = damageType;
             this.p = p;
             this.freeToPlayOnce = freeToPlayOnce;
             this.duration = Settings.ACTION_DUR_XFAST;
             this.actionType = AbstractGameAction.ActionType.SPECIAL;
             this.energyOnUse = energyOnUse;
             this.magicNumber = magicNumber;

    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAllEnemiesAction(p, multiDamage, damageType,
                                AttackEffect.FIRE, true)
                );
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));


                    if (!this.freeToPlayOnce) {
                        this.p.energy.use(EnergyPanel.totalCount);
                    }
                }
                this.isDone = true;
            }
        }
    }
}
