package academy.backend.market_pulse.history.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import academy.backend.market_pulse.history.PriceHistoryStore;
import academy.backend.market_pulse.history.PricePoint;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (бинарный, текстовый и т.д.); главное,
 * чтобы каждый {@link #append} создавал НОВЫЙ файл (не переписывал предыдущий) и
 * {@link #load(Path, String)} возвращал все сохранённые точки по тикеру в порядке возрастания
 * времени. Проверьте себя тестом {@code PriceHistoryStoreContractTest} (замените в нём эталонную
 * реализацию на свою).
 */
public class PriceHistoryStoreSkeleton implements PriceHistoryStore {

    @Override
    public Path append(Path directory, PricePoint point) {
        Path tickerDirectory = directory.resolve(point.ticker());
        Path file = tickerDirectory.resolve(point.timestamp().toEpochMilli() + ".txt");
        String line = point.ticker() + ";" + point.timestamp() + ";" + point.price();

        try {
            Files.createDirectories(tickerDirectory);
            Files.writeString(file, line);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return file;
    }

    @Override
    public List<PricePoint> load(Path directory, String ticker) {

        Path tickerDirectory = directory.resolve(ticker);

        if (!Files.exists(tickerDirectory)) {
            return new ArrayList<>();
        }

        List<Path> files;
        try (Stream<Path> stream = Files.list(tickerDirectory)) {
            files = stream.toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        List<PricePoint> result = new ArrayList<>();
        for (Path file : files) {
            String line;
            try {
                line = Files.readString(file).trim();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            String[] parts = line.split(";");
            result.add(new PricePoint(parts[0], Instant.parse(parts[1]), new BigDecimal(parts[2])));
        }

        result.sort(Comparator.comparing(PricePoint::timestamp));
        return result;
    }
}
