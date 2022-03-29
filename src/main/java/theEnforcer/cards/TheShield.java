package theEnforcer.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;

import static theEnforcer.DefaultMod.makeCardPath;

public class TheShield extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(TheShield.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    // Remember to add the NAME and DESCRIPTION in DefaultMod-Card-Strings.json

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 0;

    private static final int DAMAGE = 3;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    private static final int EXHAUSTNO = 1;
    // /STAT DECLARATION/
    public TheShield() {
        this(true);
    }

    public TheShield(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = MAGIC;
        magicNumber = baseMagicNumber = MAGIC;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = EXHAUSTNO;
        if(makePreview) {
            cardsToPreview = new TheStick(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT)
        );
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber)
        );
        AbstractDungeon.actionManager.addToBottom(
            new ExhaustAction(EXHAUSTNO, false)
        );
    }

    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new TheStick()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new TheStick())
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
