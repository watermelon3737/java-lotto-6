package lotto;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private List<Lotto> lottoPapers;

    public void getLotto(List<Lotto> lottoPapers) {
        this.lottoPapers = lottoPapers;
    }

    public void insertMoney(final LottoStore lottoStore, final long money) {
        lottoStore.getMoney(money);
    }

    public void checkWinningNumber(LottoChecker lottoChecker) {
        lottoChecker.insertLottos(this.lottoPapers);
    }

    public List<Lotto> showLottoPapers() {
        return new ArrayList<>(this.lottoPapers);
    }
}
