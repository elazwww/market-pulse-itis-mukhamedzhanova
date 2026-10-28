package academy.backend.market_pulse.repository.store.impl;

import academy.backend.market_pulse.repository.store.WatchlistStore;
import academy.backend.market_pulse.repository.store.WatchlistStoreContractTest;

public class WatchlistStoreContractImplTest extends WatchlistStoreContractTest {
    @Override
    protected WatchlistStore createStore() {
        return new WatchlistStoreSkeleton();
    }
}
