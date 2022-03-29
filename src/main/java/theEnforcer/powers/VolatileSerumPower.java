package theEnforcer.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theEnforcer.DefaultMod;
import theEnforcer.util.TextureLoader;

import static theEnforcer.DefaultMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class VolatileSerumPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID(VolatileSerumPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static int HPLossNo = 1;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public VolatileSerumPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final int HPLossAmount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.source = source;
        HPLossNo = HPLossAmount;
        type = PowerType.BUFF;
        isTurnBased = false;


        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            if (HPLossNo > 0) {
                AbstractDungeon.actionManager.addToBottom(
                        new LoseHPAction(owner, owner, HPLossNo));
            }
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.amount, true),
                            DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)
            );
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + HPLossNo + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new VolatileSerumPower(owner, source, amount, HPLossNo);
    }
}
