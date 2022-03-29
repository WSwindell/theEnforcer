package theEnforcer.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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

public class CollectScraps extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(CollectScraps.class.getSimpleName());
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

    private static final int DAMAGE = 0;

    // /STAT DECLARATION/
    public CollectScraps() {
        this(true);
    }

    public CollectScraps(boolean makePreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        tags.add(EnforcerTags.CAN_FLIP);
        if(makePreview) {
            cardsToPreview = new Shatter(false);
        }
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        baseDamage = m.currentBlock / 2;
        calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, damage)
        );
    }

    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    public void applyPowers() {
        super.applyPowers();
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }


    @Override
    public void triggerOnExhaust() {
        if (upgraded) {
            AbstractCard newCard = (new Shatter()).makeCopy();
            newCard.upgrade();
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(newCard)
            );
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new Shatter())
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
