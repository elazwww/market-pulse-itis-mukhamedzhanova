package academy.backend.market_pulse.history.impl;

import academy.backend.market_pulse.history.PriceHistoryStore;
import academy.backend.market_pulse.history.PriceHistoryStoreContractTest;

public class PriceHistoryStoreSkeletonImplTest extends PriceHistoryStoreContractTest {
    @Override
    protected PriceHistoryStore createStore() {
        return new PriceHistoryStoreSkeleton();
    }
}
