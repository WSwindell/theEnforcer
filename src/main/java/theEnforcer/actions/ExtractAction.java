package theEnforcer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theEnforcer.powers.HypodermicExtractPower;

public class ExtractAction extends AbstractGameAction {
       private DamageInfo info;

       public ExtractAction(AbstractCreature target, DamageInfo info) {
           this.info = info;
           setValues(target, info);
           actionType = AbstractGameAction.ActionType.DAMAGE;
           startDuration = Settings.ACTION_DUR_FAST;
           duration = this.startDuration;
    }

    @Override
    public void update() {
        if (shouldCancelAction()) {
            isDone = true;
                   return;
        }
        tickDuration();

        if (isDone) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SLASH_VERTICAL, false));

            target.damage(info);
            if (target.lastDamageTaken > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(source, source,
                    new HypodermicExtractPower(source, source, target.lastDamageTaken)));
            }

            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            } else {
                addToTop((AbstractGameAction)new WaitAction(0.1F));
            }
        }
       }


}
