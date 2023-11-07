package lotto;

import lotto.utils.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class LottoStoreTest {
    private final int LOTTO_START_NUM = 1;
    private final int LOTTO_END_NUM = 45;
    private final int LOTTO_NUM_COUNT = 6;
    private final String ERROR_PHRASES = "[ERROR]";

    @DisplayName("로또를 사기 위한 금액이 1000원 미만이면 예외가 발생한다")
    @Test
    void chargedMoneyLessThan1000() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        long money = 0;
        lottoStore.chargeMoney(money);

        //then
        assertThatThrownBy(() -> lottoStore.validateChargedMoney())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.LESS_THAN_THOUSAND.errorMessage)
                .hasMessageContaining(ERROR_PHRASES);
    }

    @DisplayName("로또를 사기 위한 금액이 1000원 단위가 아닐 경우 예외가 발생한다")
    @Test
    void chargedMoneyNotDivideBy1000() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        long money = 10001;
        lottoStore.chargeMoney(money);

        //then
        assertThatThrownBy(() -> lottoStore.validateChargedMoney())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.DIVIDED_DISABLE.errorMessage)
                .hasMessageContaining(ERROR_PHRASES);
    }

    @DisplayName("올바른 금액 저장")
    @Test
    void chargeMoney() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        long money = 13000;
        lottoStore.chargeMoney(money);
        lottoStore.validateChargedMoney();

        //then
        assertThat(lottoStore.getChargedMoney()).isEqualTo(money);
    }

    @DisplayName("로또 구매 금액 조회")
    @Test
    void checkChargedMoney() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        long money = 13000;
        lottoStore.chargeMoney(money);
        lottoStore.validateChargedMoney();

        long chargedMoney = lottoStore.getChargedMoney();

        //then
        assertThat(chargedMoney).isEqualTo(money);
    }

    @DisplayName("로또 판매 갯수 계산")
    @Test
    void calculateLottoAmount() {
        //given
        LottoStore lottoStore = new LottoStore();
        final int LOTTO_PRICE = 1000;
        long money = 20000;

        //when
        lottoStore.getMoney(money);
        lottoStore.calculateLottoAmount();
        long lottoAmountExpected = lottoStore.getChargedMoney() / LOTTO_PRICE;

        //then
        assertThat(lottoStore.getLottoAmount()).isEqualTo(lottoAmountExpected);
    }

    @DisplayName("로또 번호 생성")
    @Test
    void generateUniqueSixLottoNumbers() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        List<Integer> lottoNumbers = lottoStore.generateLottoNumbers();

        //then
        assertThat(lottoNumbers.size()).isEqualTo(LOTTO_NUM_COUNT);
        assertThat(lottoNumbers).allSatisfy(o -> assertThat(o).isBetween(LOTTO_START_NUM, LOTTO_END_NUM));
        assertThat(lottoNumbers).isSortedAccordingTo(Comparator.naturalOrder());

        Set<Integer> noDuplicatedNumber = new HashSet<>(lottoNumbers);
        assertThat(noDuplicatedNumber.size()).isEqualTo(LOTTO_NUM_COUNT);
    }

    @DisplayName("투입한 금액 만큼 로또가 생성")
    @Test
    void generateAllLottos() {
        //given
        LottoStore lottoStore = new LottoStore();

        //when
        long money = 250000;
        lottoStore.getMoney(money);
        lottoStore.calculateLottoAmount();
        lottoStore.generateAllLottos();

        //then
        List<Lotto> lottoPapers = lottoStore.showLottoPapers();
        assertThat(lottoPapers.size()).isEqualTo(lottoStore.getLottoAmount());
    }
}