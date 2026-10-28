package academy.backend.market_pulse.repository.store.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import academy.backend.market_pulse.factory.InstrumentFactories;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Quote;
import academy.backend.market_pulse.repository.store.QuoteFileStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (JSON, CSV, построчный текст); главное,
 * чтобы {@code load(save(quotes))} возвращал те же котировки. Проверьте себя тестом
 * {@code QuoteFileStoreContractTest} (замените в нём эталонную реализацию на свою).
 */
public class QuoteFileStoreSkeleton implements QuoteFileStore {

    @Builder
    private record QuoteDto(String ticker, String type, String name, Currency currency,
                            BigDecimal price, BigDecimal changePercent) {}

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void save(Path path, List<Quote> quotes) {

        List<QuoteDto> dtos = new ArrayList<>();

        for (Quote quote : quotes) {
            Instrument instrument = quote.getInstrument();
            dtos.add(QuoteDto.builder()
                    .ticker(instrument.getTicker())
                    .type(instrument.getType())
                    .name(instrument.getName())
                    .currency(instrument.getCurrency())
                    .price(quote.getPrice())
                    .changePercent(quote.getChangePercent())
                    .build());
        }

        try {
            mapper.writeValue(path.toFile(), dtos);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Quote> load(Path path) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        QuoteDto[] dtos;
        try {
            dtos = mapper.readValue(path.toFile(), QuoteDto[].class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        List<Quote> result = new ArrayList<>();
        for (QuoteDto dto : dtos) {
            Instrument instrument = InstrumentFactories.create(dto.type(),
                    dto.ticker(), dto.name(), dto.currency());
            result.add(new Quote(instrument, dto.price(), dto.changePercent()));
        }

        return result;
    }
}
