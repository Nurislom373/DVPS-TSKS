# Spring Boot Start Fluent

Ushbu Fluent librarysi sizga Spring framework bilan birlalikda telegram bot yozishingizni ancha osonlashtiradi.
Telegram bot yasash juda oson va chunarli sababi siz endi ushbu librarydan foydalanish orqali siz faqat annoatatsiyalarni
o'zi bilan bot yozishingiz mumkin. 


## Raising Bot

birinchi qilishimiz kerak bo'lgan ish bu pom.xml ga ushbu dependency qo'shishimiz kerak.

```xml
<dependency>
    <groupId>org.khasanof</groupId>
    <artifactId>spring-boot-starter-fluent</artifactId>
    <version>1.1.2</version>
</dependency>
```

keyingi qilishimiz kerak bo'lgan ish application yml yoki properties filega ushbu configuratsiyani qo'shish talab 
etiladi.

```properties
fluent.bot.token=<your bot token>
fluent.bot.username=<your bot username>
fluent.bot.process-type=both
```

```yaml
fluent:
  bot:
    token: <your bot token>
    username: <your bot username>
    process-type: both
```

keyin qiladigan ishimiz controller yozish. Ushbu Controllerlar Spring Web Controllerga juda o'xshaydi faqat farqi
shundaki ushbu Controllerlar telegramdan kelgan update handle qiladi bosa kelgan methodga map qiladi.

endi update Controller larni qanday yozilishini ko'rib chiqamiz.

```java
@UpdateController
public class FluentController {

    @HandleMessage("/start")
    public void start(Update update, AbsSender sender) throws TelegramApiException {
        String text = "start : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
```

`@UpdateController` annotatsiyasini classni ustiga qo'yishdan maqsad ushbu class Telegramdan kelgan update larni 
handle qilishi ko'rsatish uchun ushbu annotatsiyani qo'yish majburiy!.

`@HandleMessage` annotatsiyasi esa maqsadi huddi `@RequestMapping` annotatsiyasi o'xshash telegramdan kelgan update
ushbu method kirishi ko'rsatib qo'yish. 

`@HandleMessage` annotatsiya 2ta parameter qabul qiladi birinchi value. Ikkinchisi esa scope. Ushbu scope parameter 
default `MatchScope.EQUALS` oladi va kelgan updatedagi textni value match ekanligini tekshiradi. Biz endi kelganni 
faqat boshlanishini o'zini match bo'lgan updatelarni qabul qilishni hohlaymiz yani o'zimiz hohlaganday qilib 
customize qilishni shunday holatlar uchun scope ishlatiladi. Yani kelgan update o'zimiz hohlagandek qilib match 
qilishimiz uchun.

MatchScope ni bor scopelar:
- `EQUALS` - kelgan update to'liq mosligiga tekshiradi.
- `START_WITH` - nomidan ko'rinib turibdiki bu kelgan updateni boshlanishi mosligiga tekshiradi
- `END_WITH` - esa update o'xiri mosligiga tekshiradi
- `CONTAINS` - esa kelgan update ushbu berilgan qiymat bor yoki yoqligiga tekshiradi.
- `REGEX` - esa berilgan regexga update mos keladimi yoqmi shuni tekshiradi.
- `EQUALS_IGNORE_CASE` - kelgan updateni ignore case orqali tekshiradi.
- `EXPRESSION` - bizni tepadagi scope qoniqtirmaganda yani murakkab shartlar orqali updatelarni tekshiradigan 
  handler yozmoqchi bo'lganimizda ishlatiladi. Biz ushbu expression scopedan foydalanib o'zimiz istagandan qilib
  expression yozmas va ushbu expression mos bo'lgan update ushbu method mapping qilinadi.
- `VAR_EXPRESSION` - 
