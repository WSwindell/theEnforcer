package theEnforcer.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.patches.EnforcerTags;
import theEnforcer.powers.PreppedPower;
import theEnforcer.powers.StimmedPower;

import static theEnforcer.DefaultMod.makeCardPath;

public class RegrowthStims extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(RegrowthStims.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    // Remember to add the NAME and DESCRIPTION in DefaultMod-Card-Strings.json

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 1;


    // /STAT DECLARATION/
    public RegrowthStims() {
        this(true);
    }

    public RegrowthStims(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        tags.add(EnforcerTags.CAN_FLIP);
        tags.add(EnforcerTags.STIM);
        if(makePreview){
            cardsToPreview = new CombatStims(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(!p.hasPower("PreppedPower")) {
            AbstractDungeon.actionManager.addToBottom(
                    new LoseHPAction(p, p, damage));
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(p, p, "PreppedPower", 1)
            );
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new RegenPower(p, magicNumber), magicNumber));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new StimmedPower(p, p, -1), -1));
    }



    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
             boolean canUse = super.canUse(p, m);
             if (!canUse) {
                   return false;
                 }
             if (p.hasPower("StimmedPower")) {
                 canUse = false;
                 this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
             }


             return canUse;
           }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new CombatStims()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new CombatStims())
            );
        }
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            initializeDescription();
        }
    }
}
