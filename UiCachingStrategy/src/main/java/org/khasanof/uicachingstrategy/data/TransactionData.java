package org.khasanof.uicachingstrategy.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.enums.Status;
import org.khasanof.uicachingstrategy.utils.BaseUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 5:45 PM
 * <br/>
 * Package: org.khasanof.uicaching.data
 */
public class TransactionData {

    public List<TransactionEntity> getMockList(String jsonPath, String cardNumber) {

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<TransactionEntity>> reference = new TypeReference<>() {
        };
        try (InputStream inputStream = getClass().getResourceAsStream(jsonPath)) {
            List<TransactionEntity> list = objectMapper.readValue(inputStream, reference);

            List<String> numbers = readFileCardNumbers(cardNumber);

            list.forEach(obj -> {
                String[] cards = getTwoDifferentCards(numbers);
                obj.setFromCard(cards[0]);
                obj.setToCard(cards[1]);
                obj.setStatus(Status.findAny());
                obj.setCreatedAt(BaseUtils.getRandomLocalDateToBetween(1, 20));
            });

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static String[] getTwoDifferentCards(List<String> cards) {
        int from = RandomUtils.nextInt(0, 10);
        int to = RandomUtils.nextInt(0, 10);

        while (from == to) {
            from = RandomUtils.nextInt(0, 10);
            to = RandomUtils.nextInt(0, 10);
        }

        String[] res = new String[2];
        res[0] = cards.get(from);
        res[1] = cards.get(to);
        return res;
    }

    private static List<String> cardNumberGenerate(String cardNumber, int count) {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            numbers.add(cardNumber.concat(RandomStringUtils.random(12, false, true)));
        }
        return numbers;
    }

    private static void writeCardNumbers(List<String> list) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("UiCachingStrategy/src/main/resources/data/9860CardNumbers.txt"))) {
            list.forEach(o -> {
                try {
                    bufferedWriter.write(o.concat("\n"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readFileCardNumbers(String cardNumber) {
        String path;

        if (cardNumber.equals("8600")) {
            path = "UiCachingStrategy/src/main/resources/data/8600CardNumbers.txt";
        } else {
            path = "UiCachingStrategy/src/main/resources/data/9860CardNumbers.txt";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> numbers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(line);
            }
            return numbers;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


}
