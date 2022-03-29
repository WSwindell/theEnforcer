package theEnforcer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theEnforcer.DefaultMod;
import theEnforcer.util.TextureLoader;

import static theEnforcer.DefaultMod.*;

public class FlippantCoin extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("FlippantCoin");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public FlippantCoin() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }
    private boolean coinTrigger, activated;


    @Override
    public void atTurnStartPostDraw() {

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                coinTrigger = false;
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (DefaultMod.isFlippable(card)) {
                        coinTrigger = true;
                    }
                }
                if (coinTrigger) {
                    flash();
                    addToBot((AbstractGameAction)new ExhaustAction(1, true));
                }
                this.isDone = true;
            }
        });




    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }


}
