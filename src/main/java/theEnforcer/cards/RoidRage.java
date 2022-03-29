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
import com.megacrit.cardcrawl.powers.StrengthPower;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.patches.EnforcerTags;

import static theEnforcer.DefaultMod.makeCardPath;

public class RoidRage extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(RoidRage.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    // Remember to add the NAME and DESCRIPTION in DefaultMod-Card-Strings.json
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int DAMAGE = 3;

    private static final int MAGIC = 1;
    private static final int STIMMEDMAGIC = 4;

    private static final int SECOND_MAGIC = 3;
    private static final int NONSTIMMED = 9;
    // /STAT DECLARATION/
    public RoidRage() {
        this(true);
    }

    public RoidRage(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_MAGIC;
        tags.add(EnforcerTags.CAN_FLIP);
        tags.add(EnforcerTags.STIM);
        if(makePreview) {
            cardsToPreview = new Juiced(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        checkStim();
        AbstractDungeon.actionManager.addToBottom(
                new LoseHPAction(p, p, defaultSecondMagicNumber)
        );
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new StrengthPower(p, magicNumber), magicNumber));

        for(int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        if(p.hasPower("StimmedPower")) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(p, p, "StimmedPower")
            );
        }
    }

    public void checkStim() {
        if(!AbstractDungeon.player.hasPower("StimmedPower")) {
            defaultBaseSecondMagicNumber = defaultSecondMagicNumber = NONSTIMMED; //damage to take if not stimmed
            baseMagicNumber = magicNumber = MAGIC;
        } else {
            defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_MAGIC;
            baseMagicNumber = magicNumber = STIMMEDMAGIC;

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
            AbstractCard newCard = (new Juiced()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new Juiced())
            );
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
