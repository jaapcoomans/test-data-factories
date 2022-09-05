# Fairies, Fakers and Factories: Boost your tests with better test data

This project is a simple demo belonging to my conference talk "Fairies, Fakers and Factories" which I've presented at Devoxx UK 2021, J-Fall 2021, JavaLand 2022 and Javazone 2022.

## Current version

The demo consists of a partial implementation of a boardgame webshop. 
It demonstrates how Java-faker can be used to create Test Data Factories and Test Data Builders.

### Test Data Factories
The Game store contains several Test Data Factories:
* [GameTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/catalog/GameTestDataFactory.java)
* [PublisherTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/catalog/PublisherTestDataFactory.java)
* [CustomerTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/store/CustomerTestDataFactory.java)
* [DeliveryMethodTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/store/DeliveryMethodTestDataFactory.java)
* [OrderTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/store/OrderTestDataFactory.java)
* [PaymentTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/gamestore/store/PaymentTestDataFactory.java)

## Previous version

Previously (for the talks at Devoxx UK, J-Fall and JavaLand), the demo consisted of an (incomplete) Call for Papers application.
The main purpose is to demonstrate how Java-faker can be used to create Test Data Factories for unit tests.

### Test Data Factories
The CFP part of the project contains 4 Test Data Factories:
* [PaperTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/conference/domain/PaperTestDataFactory.java)
* [SpeakerTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/conference/domain/SpeakerTestDataFactory.java)
* [RoomTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/conference/domain/RoomTestDataFactory.java)
* [ScheduleTestDataFactory](src/test/java/nl/jaapcoomans/demo/testdata/conference/domain/ScheduleTestDataFactory.java)

The latter combines the Factory with a Builder to demonstrate complex objects.

## Conferences

I presented the conference talk to which this project belongs at the following conferences:

| Conference | Date       | Link to conference site                                                 |
|------------|------------|-------------------------------------------------------------------------|
| Devoxx UK  | 02-11-2021 | https://www.devoxx.co.uk/talk/?id=12628                                 |
| J-Fall     | 04-11-2021 | https://jfall.nl/timetable-2021/                                        |
| JavaLand   | 16-03-2022 | https://en.shop.doag.org/javaland/2022/agenda/                          |
| JavaZone   | 07-09-2022 | https://2022.javazone.no/#/program/362babf9-6c32-42cb-8dd7-6d84b600f139 |
