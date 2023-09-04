# Spring Boot Start Fluent

Ushbu Fluent librarysi sizga Spring framework bilan birgalikda telegram bot yozishingizni ancha osonlashtiradi.
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
handle qilishi ko'rsatish uchun ushbu annotatsiyani qo'yish majburiy!. Ushbu annotatsiya `@Controller` annotatsiyasiga
o'xshaydi. Farqi ikklasi ikki xil maqsad uchun ishlatilishida.

Boshlanishi `Handle` nomi bilan boshlanadigan annotatsiyalar barchasi ushbu method kelgan update kirishini ko'rsatish
uchun ishlatiladi. `@HandleMessage` annotatsiyasi esa message bob kelgan update larni map qilish ko'rsatish uchun.
Ushbu nomi `handle` bilan boshlangan annotatsiyalar `@RequestMapping` annotatsiyasi o'xshaydi. Ikkla annotatsiyani
bir biridan farqi ikklasi 2 xil maqsadda ishlatilishida.

`@HandleMessage` annotatsiya 2ta parameter qabul qiladi birinchi value. Ikkinchisi esa scope. Ushbu scope parameter 
default `MatchScope.EQUALS` oladi va kelgan updatedagi textni value match ekanligini tekshiradi. Biz endi kelganni 
faqat boshlanishini o'zini match bo'lgan updatelarni qabul qilishni hohlaymiz yani o'zimiz hohlaganday qilib 
customize qilishni shunday holatlar uchun scope ishlatiladi. Yani kelgan update o'zimiz hohlagandek qilib match 
qilishimiz uchun.

MatchScope ni bor scopelar:
- `EQUALS` - kelgan update to'liq mosligiga tekshiradi. EQUALS scope ishlatmoqchi bo'lsak uni ko'rsatish shart emas
   sababi Handle bilan boshlanadigan va matchScope bo'lgan annotatsiyalar default EQUALS ni oladi qiymat berilmagan
   taqdirda.
   - ```java
      @HandleMessage(value = "/start", scope = MatchScope.EQUALS)
      private void start(Update update, AbsSender sender) {
        // ...
      }
     ```
- `START_WITH` - nomidan ko'rinib turibdiki bu kelgan updateni boshi belgilangan qiymatga mosligiga tekshiradi.
  - ```java
    @HandleMessage(value = "start", scope = MatchScope.START_WITH)
    void multiMessageHandler(Update update, AbsSender sender) {
        // ...
    }
    ```
- `END_WITH` - esa kelgan updateni oxirini belgilangan qiymatga mosligiga tekshiradi.
  - ```java
    @HandleMessage(value = "end", scope = MatchScope.END_WITH)
    void multiMessageHandler(Update update, AbsSender sender) {
        // ...
    }
    ```
- `CONTAINS` - esa kelgan update da belgilangan qiymat bor yoki yoqligiga tekshiradi.
  - ```java
    @HandleMessage(value = "hello", scope = MatchScope.CONTAINS)
    void handleAny(Update update, AbsSender sender) {
        // ...
    }
    ```
- `REGEX` - esa berilgan regexga update mos keladimi yoqmi shuni tekshiradi.
  - ```java
    @HandleMessage(value = "[1-5]", scope = MatchScope.REGEX)
    public void world(AbsSender sender, Update update) {
        // ...
    }
    ```
- `EQUALS_IGNORE_CASE` - kelgan updateni ignore case orqali tekshiradi.
  - ```java
    @HandleMessage(value = "fluent", scope = MatchScope.EQUALS_IGNORE_CASE)
    public void world(AbsSender sender, Update update) {
        // ...
    }
    ```
- `EXPRESSION` - bizni tepadagi scope qoniqtirmaganda yani murakkab shartlar orqali updatelarni tekshiradigan 
  handler yozmoqchi bo'lganimizda ishlatiladi. Biz ushbu expression scopedan foydalanib o'zimiz istagandan qilib
  expression yozmas va ushbu expression mos bo'lgan update ushbu method mapping qilinadi.
  - ```java
     @HandleMessage(value = "START_WITH('javohir', value) && END_WITH('abdulloh', value)",
            scope = MatchScope.EXPRESSION)
     private void messageExp(Update update, AbsSender sender) {
        // ...
     }
    ```
- `VAR_EXPRESSION` - VAR_EXPRESSION Ant Style path pattern foydalanadi yani kelgan updatedagi patternga mos bo'lgan 
  qiymatlarni olish imkoni beradi. Ushbu xususiyat hozircha faqat HandleMessage annotatsiya uchun ishlaydi.
  - ```java
    @HandleMessage(value = "/start {name:[a-z]} -> {look:[1-5]}", scope = MatchScope.VAR_EXPRESSION)
    public void testExp3(Update update, AbsSender sender, @BotVariable("name") String name, @BotVariable("look") String look) {
        // ...
    }
    ```

## Update Handlers

Update larni handle qiladigan jami 15ta annotatsiyalar mavjud ular.

| Name            | Has Plural | Has Match Scope | Has Own Scope | 
|-----------------|:----------:|:---------------:|:-------------:|
| HandleAny       |     No     |       No        |      Yes      |
| HandleMessage   |    Yes     |       Yes       |      No       |
| HandleCallback  |    Yes     |       Yes       |      No       |
| HandleAudio     |    Yes     |       Yes       |      Yes      |
| HandleDocument  |    Yes     |       Yes       |      Yes      |
| HandlePhoto     |    Yes     |       Yes       |      Yes      |
| HandleVideo     |    Yes     |       Yes       |      Yes      |
| HandleVideoNote |    Yes     |       Yes       |      Yes      |

Updatelarni handle qilish uchun hozircha shu annotatsiyalar mavjud.
