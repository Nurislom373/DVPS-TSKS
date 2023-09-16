### Why this library

It provides better way to code around exception handling. Let's take some of the most common example of exception
handling. I have seen a lot of people committing following mistake while handling exception. Go through following example
and see yourself.

```
    User getUserInformation(String username) {
        UserInfo userInfo;
        try {
            user = userService.getUser(username);
        } catch(Exception exception) {
            user = new User("some", "default", "value")
        }

        if(user != null) {
            sendInfoToReportingService(user.id);
        }
        
        return getUserWithEnrichedInfo(user);
    }

```

Do you think, the above `if` statement should be removed and code could be transformed to below piece of code :).

```
    User getUserInformation(String username) {
        UserInfo userInfo;
        try {
            user = userService.getUser(username);
            sendInfoToReportingService(user.id);
        } catch(Exception exception) {
            user = new User("some", "default", "value")
        }

        return getUserWithEnrichedInfo(user);
    }

```

do you think, the second version is better than the first one, because we don't have `if` statement in second one?
Well both of the above code might seem similar but their behaviour is different. If an exception is raised by
`sendInfoToReportingService` method then first code let it fail and pass exception to caller but second one handles it
unintentionally. And it has masked one of your code problem, the first code snippet would fail the request and
you would know that something failed and try to debug, but second one is more dangerous and you never realize it until
it has done damage to your data.

I have seen people arguing(during pairing) that `sendInfoToReportingService` can never fail and by reasoning like
`Be pragmatic` or `when something will happed, we will fix it`. They ask, **What is the harm** in doing it this way,
if `sendInfoToReportingService` never fails. Answer is, there is an **assumption** in the above method that
`sendInfoToReportingService` never fails, well you know about this assumption but not the next developer,
who will be working on `sendInfoToReportingService` and violate your assumption not ever knowing that one of it's
consumer is assuming something or you can never fail a method won't fail because that is the reason Runtime
exception exist.

Well, java does not have anything to handle such a scenario. We can write this code using the library in following way

```
    User getUserInformation(String username) {
        UserInfo userInfo = try.toGet(userService.getUser(username))
                               .ifRaises(Exception.class)
                               .thenGet((exception) -> new User("some", "default", "value"))
                               .elseCall((user) -> sendInfoToReportingService(user.id))
                               .done();

        return getUserWithEnrichedInfo(user);
    }

```

here consumer in `elseCall` calls `sendInfoToReportingService` outside try catch block. If i read this code, i find it much readable
than written in plain java.

This library is intended to cover such scenarios and help you better and readable code around exception handline by
being declarative and intuitive.

let's take a few example for the syntax of the library, when a user interacts with third party/micro service over
the network, there are multiple possibilities of failure, next section describes, how it handles them.

+ Call a method in try catch block and if it fails to trigger the data sync job then add a failure to DB, so that
  it can be latter pick up for processing or you would like to log it. then you can define exception handling like

```
    try {
        triggerSyncToDB()
    } catch(RuntimeException exception) {
        addFailureToDB(exception)
    }
```

Let's translate it to more declarative way

```
    Try.toCall(() -> triggerSyncToDB())
       .ifRaises(RuntimeException.class)
       .thenCall((exception) -> addFailureToDB(exception))
       .done()
```

or in case of logging exception details

```
    Try.toCall(() -> triggerSyncToDB())
       .ifRaises(RuntimeException.class)
       .thenCall((exception) -> LOGGER.error(exception.getMessage))
       .done()
```

+ Get data from other services and if a http server or client exception is raised, we would like to give more
  information to consumer of api/method. Rather than letting it go up in the stack, we would like to throw more sensible
  exception

```
    try {
        triggerSyncToDB()
    } catch(HttpServerException |  HttpClientException exception) {
        throw new SomeException(raisedException.getMessage())
    } catch(IOException exception) {
        throw new InternalServerException(raisedException.getMessage())
    }
```

can be translated to

```
    Try.toCall(() -> triggerSyncToDB())
        .ifRaises(HttpServerException.class, HttpClientException.class)
        .thenThrow((raisedException) -> new SomeException(raisedException.getMessage()))
        .elseIfRaises(IOException.class)
        .thenThrow((raisedException) -> new InternalServerException(raisedException.getMessage()))
        .done()
```

Taking previous example one step further, Let us assume, you would like to do something else if request timeouts
then you can do it following way

```
    try {
        triggerSyncToDB()
    } catch(TimeoutException exception) {
        retriggerSyncProcess();
    } catch(HttpServerException |  HttpClientException exception) {
        throw new SomeException(raisedException.getMessage())
    } catch(IOException exception) {
        throw new InternalServerException(raisedException.getMessage())
    }

```

can be translated to

```
    Try.toCall(() -> triggerSyncToDB())
        .ifRaises(TimeoutException.class)
        .thenCall(() -> retriggerSyncProcess())
        .ifRaises(HttpServerException.class, HttpClientException.class)
        .thenThrow((raisedException) -> new SomeException(raisedException.getMessage()))
        .elseIfRaises(IOException.class)
        .thenThrow((raisedException) -> new InternalServerException(raisedException.getMessage()))
        .done()
```

Let's assume, you want to clean up some resources then

```
    try {
        triggerSyncToDB()
    } catch(TimeoutException exception) {
        retriggerSyncProcess();
    } catch(HttpServerException |  HttpClientException exception) {
        throw new SomeException(raisedException.getMessage())
    } catch(IOException exception) {
        throw new InternalServerException(raisedException.getMessage())
    } finally {
        cleanup()
    }
```

could be written as


```
    Try.toCall(() -> triggerSyncToDB())
        .ifRaises(TimeoutException.class)
        .thenCall(() -> retriggerSyncProcess())
        .ifRaises(HttpServerException.class, HttpClientException.class)
        .thenThrow((raisedException) -> new SomeException(raisedException.getMessage()))
        .elseIfRaises(IOException.class)
        .thenThrow((raisedException) -> new InternalServerException(raisedException.getMessage()))
        .finallyDone(() -> cleanup())
```

Don't you think, this new declarative way makes good story out of the exception cases :).
