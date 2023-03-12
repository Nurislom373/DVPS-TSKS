package org.khasanof.reactiveuicaching.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.enums.Status;
import org.khasanof.reactiveuicaching.utils.BaseUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
@Component
public class TransactionMockData {

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

    public String[] getTwoDifferentCards(List<String> cards) {
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

    private List<String> cardNumberGenerate(String cardNumber, int count) {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            numbers.add(cardNumber.concat(RandomStringUtils.random(12, false, true)));
        }
        return numbers;
    }

    private void writeCardNumbers(List<String> list, String cardNum) {

        String absolutePath = "D:\\Nurislom\\Projects\\DVPS-TSKS\\UiCachingStrategy\\src\\test\\resources\\data\\";
        String extension = "CardNumbers.txt";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath.concat(cardNum)
                .concat(extension)))) {
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

    private List<String> readFileCardNumbers(String cardNumber) throws FileNotFoundException {
        Assert.notNull(cardNumber, "CardNumber Must Be Not Null!");

        String absolutePath = "D:\\Nurislom\\Projects\\DVPS-TSKS\\UiCachingStrategy\\src\\test\\resources\\data\\";
        String extension = "CardNumbers.txt";

        String path = absolutePath.concat(cardNumber)
                .concat(extension);
        return getStrings(path);
    }

    private List<String> getStrings(String path) {
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
