### Why this library

This library does not provide anything that you can not achieve with provided java language
`if else` statements. It does not simplify your business logic or `if` checks, so what it does basically?
It helps you to write more readable code and how it does that, let's understand it by examples.

- I saw a similar code snippet in one of projects, It throw exception based on the response code of the response
  entity

        EmployeeDTO getEmployeeDetail(String employeeCode) {
              ResponseEntity<EmployeeDTO> responseEntity;
              try {
                      responseEntity = restTemplate.getForEntity(url, EmployeeDTO.class);
              } catch(HttpServerErrorException ex) {
                  throw EmployeeServiceException("Failed to fetch.......");
              }

              if (responseEntity.getStatusCode() != HttpStatus.OK) {
                  throw new EmployeeServiceException(format(ERROR_MESSAGE, responseEntity.getStatusCode()));
              }

              if (responseEntity.getBody() == null || "".equals(responseEntity.getBody().toString())) {
                  throw new NoContentException(format(NO_CONTENT_FOUND_MESSAGE, fromDate, toDate));
              }

              return responseEntity.getBody();
        }

  Let's write this code with library

        EmployeeDTO getEmployeeDetail(String employeeCode) {
              ResponseEntity<EmployeeDTO> responseEntity;
              try {
                   responseEntity = restTemplate.getForEntity(url, EmployeeDTO.class);
              } catch(HttpServerErrorException ex) {
                  throw EmployeeServiceException("Failed to fetch.......");
              }

          return If.isTrue(responseEntity.getStatusCode() != HttpStatus.OK)
             .thenThrow(() -> new EmployeeServiceException(format(ERROR_MESSAGE, responseEntity.getStatusCode())))
             .elseIf(responseEntity.getBody() == null)
             .thenThrow(() -> new NoContentException(format(NO_CONTENT_FOUND_MESSAGE, fromDate, toDate))
             .elseGet((responseEntity) -> responseEntity.getBody())
        }

Don't you think, it is more readable. It tells a better story that you do this otherwise do this or this.

- Let's assume, you have to fetch employee skills provided by multiple services, and based on some flag you fetch the
  details from one of them and return it. see below

  List<EmployeeSkill> getEmployeeSkills(String code) {
  final EmployeeClient employeeClient;
  if(isEmployeeGlobalServiceEnabled()) {
  employeeClient = globalEmployeeServceFactory.getEmployeeClient();
  } else {
  employeeClient = localEmployeeServiceFactory.getEmployeeClient();
  }
  Employee employee = employeeRepository.findByCode(code);
  return employeeClient.getEmployeeSkills(employee.id)
  }

Now try to understand the code above, even though very simple statements but you might have spend some time with
`employeeClient` variable to track it. if this method had been large, it would have been more difficult to track it.
Every time, we refer a variable, we have to consider all possible assignments to it, if it has multiple assignments we have
to understand all of them. Now, let's write the same code using library

    List<EmployeeSkill> getEmployeeSkills(String code) {
        final EmployeeClient employeeClient = If.isTrue(isEmployeeGlobalServiceEnabled())
                                                .thenGet(() -> globalEmployeeServceFactory.getEmployeeClient())
                                                .elseGet(() -> localEmployeeServiceFactory.getEmployeeClient())

        Employee employee = employeeRepository.findByCode(code);
        return employeeClient.getEmployeeSkills(employee.id)
    }

As a programmer`if else` are now basic syntax and we don't think, it is an over head for above basic examples
because that's what we have been reading since we learnt programming, it takes few extra seconds than normal sequential
statements to understand these simple syntax?. But why to spend even seconds on these syntax?

People comes up with argument, what if statements in `if else` are large block of code. Well, we should take
large `if else` blocks in methods because they are doing one and only one thing and it very cohesive piece of code. How
can i say this? because that's the reason they belong together in the block otherwise you would have taken them out of
blocks, above or below. You must be thinking, why are we discussion it here? Because
this library implicitly(syntax will be difficult) makes you, to refactor large `if else` code in methods. If you have
large block of code, the syntax will make you think about refactoring.

    void doSomething() {
       statement0
       If.isTrue(condition)
         .thenCall(() -> {
           statement11
           statement21
           statement31
           statement41
       }).elseCall(() -> {
           statement21
           statement22
           statement23
           statement24
       })
    }

then change it to

    void doSomething1() {
           statement11
           statement21
           statement31
           statement41
    }
    
    void doSomething2() {
           statement21
           statement22
           statement23
           statement24
       }

    void doSomething() {
       statement0
       If.isTrue(condition)
         .thenCall(() -> doSomething1())
         .elseCall(() -> doSomething2())
    }


1. __*If else syntax for expression*__

+ If an expression is true then user `x` otherwise use `y`

        int allowedSpeed;
        if(currentRoad == "highWay") {
            allowedSpeed = 160km/h;
        } else {
            allowedSpeed = 40km/h;    
        }
    
        setSpeed(allowedSpeed);

  Simplified as

        int allowedSpeed = if.isTrue(currentRoad == "highWay")
                             .thenValue(160km/h)
                             .elseValue(40km/h);
        setSpeed(allowedSpeed);
  And as

        int allowedSpeed = if.orElse(currentRoad == "highWay", 160km/h, 40km/h);
        setSpeed(allowedSpeed);

+ If an expression is true then process something otherwise use some other process

        if(currentLocation == "known") {
            driveWithFun();
        } else {
            informPassenger();    
        }

  Simplified as

        if.isTrue(currentRoad == "known")
          .thenCall(() -> driveWithFun())
          .elseCall(() -> informPassenger());

  And as

        if.orElse(currentRoad == "known", () -> driveWithFun(), () -> informPassenger());

+ If an expression is true then get a value from one mechanism otherwise from a different one

        int allowedSpeed;
        if(allowedSpeedAtCurrentLocation == "unknown") {
            allowedSpeed = callAFriendAndGetIt();
        } else {
            allowedSpeed = getItFromMyBrain();
        }

        accelerateToGivenSpeed(allowedSpeed)

  Simplified as

        int allowedSpeed = if.isTrue(allowedSpeedAtCurrentLocation == "unknown")
                             .thenGet(() -> callAFriendAndGetIt())
                             .elseGet(() -> getItFromMyBrain());
        accelerateToGivenSpeed(allowedSpeed)

  And as

        int allowedSpeed = if.orElse(allowedSpeedAtCurrentLocation == "unknown",
                                     () -> callAFriendAndGetIt(),
                                     () -> getItFromMyBrain());

        accelerateToGivenSpeed(allowedSpeed)


+ If something unexpected occurred and need to raise exception else process
  if(currentSpeed == "unacceptable-level") {
  throw new UnacceptableSpeed(currentSpeed);
  } else {
  informSpeedIsAcceptable();
  }

        driveSmoothly(allowedSpeed)

  Simplified as

        if.isTrue(currentSpeed == "unacceptable-level")
          .thenThrow(() -> new UnacceptableSpeed(currentSpeed))
          .elseCall(() -> informSpeedIsAcceptable());
        driveSmoothly(allowedSpeed)


    all the above methods could be used in combinations, please refer test cases for all possible scenarios

2. __*If else for handling Null object*__

+ when a value is calculated one way, if a variable is null and other way if variable is not null
  String boxMessage;
  if(box == null) {
  boxMessage = "Box does not exist";
  } else {
  boxMessage = "Box exist";
  }

        showMessage(boxMessage)

  Simplified as

        String boxMessage = if.isNull(box)
                              .thenValue("Box does not exist")
                              .elseValue("Box exist");
        showMessage(boxMessage);

  And as

        String boxMessage = if.nullOrElse(box, "Box does not exist", "Box exist");
        showMessage(boxMessage);

+ A value is calculated if a variable is null otherwise use given value,

        if(boxMessage == null) {
            boxMessage = generateMessage();
        }

        showMessage(boxMessage);

  Simplified version:

        String boxMessage = if.isNull(boxMessage)
                              .thenGet(() -> generateMessage())
                              .elseValue(boxMessage);

        showMessage(boxMessage);


+ A value is calculated, if a variable is null one way, otherwise use some other way,
  if(receivedBoxMessage == null) {
  boxMessage = generateMessage();
  } else {
  boxMessage = generateMessageOtherWay();
  }

        showMessage(boxMessage);

  Simplified version:

        String boxMessage = if.isNull(receivedBoxMessage)
                              .thenGet(() -> generateMessage())
                              .elseGet(() -> generateMessageOtherWay());

        showMessage(boxMessage);

  And as:

        String boxMessage = if.isNull(receivedBoxMessage, () -> generateMessage(), () -> generateMessageOtherWay());    
        showMessage(boxMessage);

+ A value is calculated one way, if a variable is null, otherwise use non null value to generate value,
  if(receivedBoxMessage == null) {
  boxMessage = generateMessage();
  } else {
  boxMessage = generateMessage(boxMessage);
  }

        showMessage(boxMessage);


    Simplified version:
        String boxMessage = if.isNull(receivedBoxMessage)
                              .thenGet(() -> generateMessage())
                              .elseMap((nonNullReceivedBoxMessage) -> generateMessage(nonNullReceivedBoxMessage));

        showMessage(boxMessage);

    And as:

        String boxMessage = if.isNull(receivedBoxMessage,
                                      () -> generateMessage()),
                                      (nonNullReceivedBoxMessage) -> generateMessage(nonNullReceivedBoxMessage));

        showMessage(boxMessage);

+ If you would like to throw an exception, if a value is null, otherwise proceed,

        if(boxMessage == null) {
            throw new NotFoundException("Message not found");
        } else {
            boxMessage = generateMessage(boxMessage);
        }

        showMessage(boxMessage)

  Simplified version:

        String boxMessage = if.isNull(receivedBoxMessage)
                              .thenThrow(new NotFoundException("Message not found"))
                              .elseMap((nonNullReceivedBoxMessage) -> generateMessage(nonNullReceivedBoxMessage));

        showMessage(boxMessage)

and so on. see test cases to know more about available options.

3. __*do something only if some condition is true, else part is empty*__

+ If you like to process something when condition is true else nothing

        if(purseStatus == "not found) {
            putCashInPocket(cash);
        }
        goToShopping();

  Simplified version:

        if.isTrueThen(purseStatus == "not found").thenCall(() -> putCashInPocket(cash))
        goToShopping();

  And as:

        if.isTrueThen(purseStatus == "not found", () -> putCashInPocket(cash))
        goToShopping();

+ If you throw some exception when condition is met otherwise perform operations normally

        if(breakStatus == "not working") {
            throw new BreakNotWorking();
        }
        keepDriving();

  Simplified version:

        if.isTrueThen(breakStatus == "not working")).thenThrow(() -> new BreakNotWorking())
        keepDriving();
