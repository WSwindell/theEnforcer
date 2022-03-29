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
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.patches.EnforcerTags;

import static theEnforcer.DefaultMod.makeCardPath;

public class Juiced extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Juiced.class.getSimpleName());
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

    private static final int COST = 0;

    private static final int MAGIC = 3;
    private static final int NONSTIMMED = 9;
    private static final int SECOND_MAGIC = 2;
    private static final int SECOND_UPGRADE = 1;

    private static final int CARD_DRAW = 3;

    // /STAT DECLARATION/
    public Juiced() {
        this(true);
    }

    public Juiced(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_MAGIC;
        tags.add(EnforcerTags.CAN_FLIP);
        tags.add(EnforcerTags.STIM);
        if(makePreview) {
            cardsToPreview = new RoidRage(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        checkStim();
        AbstractDungeon.actionManager.addToBottom(
                new LoseHPAction(p, p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(p, CARD_DRAW)
        );
        AbstractDungeon.actionManager.addToBottom(
                new GainEnergyAction(SECOND_MAGIC)
        );
        if(p.hasPower("StimmedPower")) {
            AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(p, p, "StimmedPower")
            );
        }
    }

    public void checkStim() {
        if(!AbstractDungeon.player.hasPower("StimmedPower")) {
            baseMagicNumber = magicNumber = NONSTIMMED; //damage to take if not stimmed
        } else {
            baseMagicNumber = magicNumber = MAGIC;
        }
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }


    public void applyPowers() {
        checkStim();
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
        checkStim();
    }

    public void triggerWhenDrawn() {
        checkStim();
    }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new RoidRage()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new RoidRage())
            );
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDefaultSecondMagicNumber(SECOND_UPGRADE);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
