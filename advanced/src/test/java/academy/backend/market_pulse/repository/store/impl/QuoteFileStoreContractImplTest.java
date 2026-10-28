package academy.backend.market_pulse.repository.store.impl;

import academy.backend.market_pulse.repository.store.QuoteFileStore;

public class QuoteFileStoreContractImplTest extends academy.backend.market_pulse.repository.store.QuoteFileStoreContractTest {
    @Override
    protected QuoteFileStore createStore() {
        return new QuoteFileStoreSkeleton();
    }
}
