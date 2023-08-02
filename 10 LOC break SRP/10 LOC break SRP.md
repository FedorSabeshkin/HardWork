## Задача
Требуется показать примеры и исправления, когда одна из "физических" строк кода нарушает SPR.
Под одной "физической" линией кода, я подразумеваю строчку даже с переносом на несколько,
но когда она не завершилась неким оператором завершения строки,
например ";" в Java.

То есть нарушение одного из правил object Cohesions, которое требует через точку обращаться только если после операции получаем тот же объект, что и до этого (исключение для финальной операции преобразование содержимого stream в list, например).
```java
stream
  .filter(...)
  .map ()
  .toList();
```
Такие проблемы часто вижу при работе с UI компонентами и с proto, которые имеют достаточно большой уровень вложенности. Когда мы сразу из тела общего ответа обращаемся к конкретному объекту (который является полем этого ответа), а от него к конкретному полю уже этого объекта. Или, что хуже, поле и само является объектом,
поле от которого нам и требуется.

В общем случае, это рефакторится созданием дополнительной переменной,
в которую кладется промежуточный результат. Переменная обладает полноценным названием, без "temp" и подобного.

Но возникает вопрос о том, насколько такое влияет на производительность,
к сборщику муссора придется подчищать несколько больше переменных, хотя в них и лежат чаще всего просто ссылкии на другие объекты.  
Но идем от правила, что сначала пишем читаемый код, и уже после этого можем говорить о его оптимизации, при сохранении после нее читаемости.
## Примеры
Приведу несколько примеров:   
a)
```java
// до
item.setUserName((authService.getAuthorizeduser(item.getUserId()).getUserDisplayName())) ;
```
```java
// после
UserId userid= item.getUserId()
User user = authService.getAuthorizedUser(userId));
UserName userName = user.getUserDisplayName();
item.setUserName (userName);
```
б)
```java
// до
item.setRegistrationStatusName(
  I18nService.getI18nValue(
                      model.getLocale(),
                      getClass(),
                      Arrays.stream (RegistrationStatus.values())
                              filter(value->value.getId().equals(item.getRegistrationStatusId()))
                              .findFirst()
                              .map(RegistrationStatus::getNameI18nKey)
                              .orElse(null))
);
```
```java
// после
RegistrationStatusId registrationStatusId = item.getRegistrationStatusId();
I18nKey i18nKey = getI18nkey(registrationStatusId);
RegistrationStatusName registrationStatusName = I18nService.getI18nValue(model.getLocale(),
                                                                        getClass(),
                                                                        i18nKey);
item.setRegistrationStatusName(registrationStatusName);

/**
* Получение i18nkey для статуса регистрации.
**/
private I18nkey getI18nkey(Registrationstatusid registrationStatusId) {

  return Arrays.stream(RegistrationStatus.values())
                                                .filter(value> value.getId().equals(registrationStatusId))
                                                .findFirst()
                                                .map (RegistrationStatus::getNameI18nkey)
                                                .orElse (null);
}
```
в)
```java
// до 
items.add(
    RestrictionsonParticipantInfo.builder()
                                  .restrictionCode Participant (code)
                                  .dateRestrictionStart(
                                        SHARED_DATE_UTILS.getLocalDateFromTimestamp(restriction.getStartDate()))
                                  .referenceInfo(restrictionsonParticipantCodeReference.getDescription())
                                  .build());
```
```java
// после
/**
* Здесь вынес и отдельные переменные преобразования и создания значений.
* Для получения значения из геттера не вижу смысла создавать новую переменную,
* т.к. это просто дублирует ту структуру, что уже находится как поле в классе объекта,
* у которого вызываю один из геттеров.
* хотя иногда это выглядит читабельнее, т.к. избавляемся от одной из точек при вызове функции,
* как в примере (а), в строке 2.
**/
LocalDate dateRestrictionStart SHARED_DATE_UTILS.getLocalDateFromTimestamp(restriction.getStartDate());
RestrictionsOnParticipantInfo restrictionsonParticipantInfo = RestrictionsonparticipantInfo.builder()
                                                                                          .restrictionCodeParticipant(code)
                                                                                          .dateRestrictionstart(dateRestrictionstart)
                                                                                          .referenceInfo(restrictionsOnParticipantCodeReference.getDescription())
                                                                                          .build();

items.add(restrictionsonParticipantInfo);
```
г) 
```java
// до
ParticipantscatalogGridItem participantscatalogdridItem = Participantscatalogariditem.builder()
  .name(jenkinsParticipant.getName())
  .type(ParticipantType.getParticipantTypeByTypeCode(
                                                jenkinsParticipant.getType()).getDescription()
                                                )
  .state(Participantstatus.getParticipantStatusByStatusCode(
                                      jenkinsParticipant.getstatus()).getDescription()
  )
  .dateInclusion(getProtoDateLocalDate(jenkinsParticipant
                                                        .getDteIn()))
  .dateExclusion(Objects.equals(Timestamp.getDefaultInstance(), jenkinsParticipant.getDateout())
                                ? null
                                :getProtoDateLocalDate(jenkinsParticipant.getDateout()))
  .build():
```
```java
// после
ParticipanType participantType = ParticipantType.getParticipantrypanyTypecode(
                                              jenkinaParticipant.getType());
String participantTypeString = participantType.getDescription();

ParticipantStatus participantStatus = ParticipantStatus.getParticipantStatusByStatusCode(
                                                jenkinaParticipant.getStatus());

String participantStateString = participantStatus.getDescription();

LocalDate dateInclusion = getProtoDateLocalDate(jenkinaParticipant.getDateIn())

LocalDate dateExclusion = Objects.equals(
                          Timestamp.getDefaultInstance(), jenkinaParticipant.getDateOut())
                          ? null
                          :getProtoDateLocalDate(jenkinsParticipant.getDateout());

ParticipantsCatalogGridItem participantsCatalogdGridItem = ParticipantsCatalogdGridItem.builder()
  .name(jenkinsParticipant.getName())
  .type(participantTypeString)
  .state(participantStateString)
  .dateInclusion(dateInclusion)
  .dateExclusion(dateExclusion))
  .build():
```
