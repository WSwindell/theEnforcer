package theEnforcer.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.patches.EnforcerTags;

import static theEnforcer.DefaultMod.makeCardPath;

public class StaggeredDeflection extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(StaggeredDeflection.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    // Remember to add the NAME and DESCRIPTION in DefaultMod-Card-Strings.json

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/
    public StaggeredDeflection() {
        this(true);
    }


    public StaggeredDeflection(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        tags.add(EnforcerTags.CAN_FLIP);
        if(makePreview) {
            cardsToPreview = new Incoming(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, p, block));

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block)
        );


    }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new Incoming()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new Incoming())
            );
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
