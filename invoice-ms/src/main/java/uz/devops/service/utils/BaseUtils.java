package uz.devops.service.utils;

import uz.devops.invoice.service.dto.GetTokenRequestDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Nurislom
 * <br/>
 * Date: 02.05.2023
 * <br/>
 * Time: 18:39
 * <br/>
 * Package: uz.devops.service.utils
 */
public class BaseUtils {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String formatDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String addTwoString(String var1, String var2) {
        return String.valueOf(Long.parseLong(var1) + Long.parseLong(var2));
    }

    public static String commissionUpAmount(String senderAmount, Long systemCommissionAmount) {
        return String.valueOf((systemCommissionAmount * 100) / Double.parseDouble(senderAmount));
    }

    public static GetTokenRequestDTO tokenRequestDTO() {
        return new GetTokenRequestDTO("998998188103", "nvpqtmoi",
            "MyCompanyDevopsUz", "jYFL3fq51JHu1aZ6jSKoZaVZKqAVJb95yD7dnr3tQySnEpHrlFOBYkciJB19");
    }

    public static String ccyNumToName(Integer ccy) {
        if (ccy.equals(860)) return "сум";
        else if (ccy.equals(840)) return "доллар";
        return "unsupported ccy!";
    }

}
