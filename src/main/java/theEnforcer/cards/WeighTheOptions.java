package theEnforcer.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.patches.EnforcerTags;

import static theEnforcer.DefaultMod.makeCardPath;

public class WeighTheOptions extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(WeighTheOptions.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    // Remember to add the NAME and DESCRIPTION in DefaultMod-Card-Strings.json

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    private static final int MAGIC = 3;
    private static final int SECOND_MAGIC = 1;

    // /STAT DECLARATION/
    public WeighTheOptions() {
        this(true);
    }

    public WeighTheOptions(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = SECOND_MAGIC;
        tags.add(EnforcerTags.CAN_FLIP);
        if (makePreview) {
            cardsToPreview = new WeighTheOdds(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(p, magicNumber)
        );

        AbstractDungeon.actionManager.addToBottom(
                new ExhaustAction(defaultSecondMagicNumber, false)
        );
    }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new WeighTheOdds()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new WeighTheOdds())
            );
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
