## Идея 
 
Есть направление в Computer Scince - QBS и есть его программные реализации, как средство позволяющее переводить императивный java + hibernate код в SQL запрос.   
  
Вообще, всегда советуют, если требуется высокая скорость обработки - выполнять запросы в БД без использования ORM. 
 
Это как с реализацией b-tree для k/v хранилища. Можно получить выигрыш на несколько порядков, переведя его реализацию на "голую" работу с железом.

В hibernate проблема, что он раскручивает запросы в JOIN которые не эффективны. Если же пишем без ORM, можем тривиально мапить из ResultSet от SQL в java объект, если его поля являются простыми типами: строки, даты, boolean, числовые поля или же вложенный объект, поля которого соответсвуют описанным ранее. 
В таком случае 1 строка ResultSet будет равна 1 объекту. 

Другое дело, если один объект содержит как поле список.Если список из стандартных перечисленных выше типов, то можем выполнить запрос в БД, в котором в SQL зададим условия для агрегации нескольких значений и возврату их в одном поле перечисленными через запятую. Например, имена всех работающих на машине водителей.
Таким образом нам удастся сохранить правило о том, что 1 строка ResultSet будет равна 1 объекту. 

Если же для машины нам надо выводить список полноценных объектов водителей, то это правило изменится. Одному объекту уже будет соответствовать несколько строк из ResultSet, анализ которых мы должны как то агрегировать, за пределами перемещения курсора по списку этих результатов, кроме того, нам надо мапить полученные из БД результаты, пока мы формируем список водителей, именно к конкретной машине. Сделать это лучше всего выполнив group by по номеру машины, так, что бы мы знали, пока номер машины в рассматриваемой строке не сменился относительно прошлой строки, мы добавлять водителя из этой строки к тому же самому ранее созданному объекту машинки.

**Интересный прием**, явно указать в реализации, что возвращаем неупорядоченные строки, указав в сигнатуре метода возврат не List(как делают не задумываясь), а Set, который явно указывает, что порядок будет случайным.
Кроме того, нам обычно и требуется Set, в том плане, что элементы внутри List не повторяются.

## Примеры
1.	Простейший кейс получения информации о бренде по его названию (все лежит  в одной таблице).
Разница оказалась неожиданной. Даже на таком “в лоб” примере, на который я ожидал что hibernate сможет правильно сгенерировать запрос и парсинг результата подобно тому, как его тривиально сделал.
```
13943554 Raw Dao
40746011 Hibernate
26802457 diff RawDao and Hibernate 
```
- Тест
```java
@Test
void testFindCarBrandByName() {
  String carBrandName = "Камаз";

  long startRawDao = System.nanoTime();
  CarBrand carBrandFromRawDao = carBrandDao.findByName(carBrandName);
  long diffRawDao = System.nanoTime() - startRawDao;

  long startHibernate = System.nanoTime();
  CarBrand carBrandFromHibernate = carBrandRepository.findByName(carBrandName);
  long diffHibernate = System.nanoTime() - startHibernate;
  System.out.println(diffRawDao + " Raw Dao");
  System.out.println(diffHibernate + " Hibernate");
  System.out.println((diffHibernate - diffRawDao) + " diff RawDao and Hibernate");
  assertEquals(carBrandFromRawDao, carBrandFromHibernate);
}
```

- Hibernate
```java
public interface CarBrandRepository
    extends CrudRepository<CarBrand, Long> {

  public CarBrand findByName(String name);

}
```

- JdbcTemplate
```java
@Repository
public class CarBrandDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public CarBrandDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public CarBrand findByName(String name) {
    String sql = "SELECT * FROM car_brand WHERE name = ?";

    RowMapper<CarBrand> rowMapper = (rs, rowNum) -> {
      CarBrand carBrand = new CarBrand();
      carBrand.setId(rs.getLong("id"));
      carBrand.setName(rs.getString("name"));
      carBrand.setCarType(rs.getString("car_type"));
      carBrand.setAmountOfPlaces(rs.getInt("amount_of_places"));
      carBrand.setCarryingCapacity(rs.getInt("carrying_capacity"));
      carBrand.setVolumeOfTank(rs.getInt("volume_of_tank"));
      return carBrand;
    };

    return jdbcTemplate.queryForObject(sql, rowMapper, name);
  }
}
```
2.	Получение информации о бренде машины по id машины. Бренд и машина это две разных таблицы.
Несколько запусков, разница 2-3 раза, иногда ее практически не было. 
Результат в наносекундах
```
20679959 Raw Dao
60748241 Hibernate
40068282 diff RawDao and Hibernate
```
- Пример теста
```java
@Test
void testFindCarBrandByVehicleId() {
  Long vehicleId = enterpriseGenerator.getRandomVehicle()
                                      .getId();
  long startRawDao = System.nanoTime();
  CarBrand carBrandFromRawDao = vehicleDao.findCarBrandById(vehicleId);
  long diffRawDao = System.nanoTime() - startRawDao;

  long startHibernate = System.nanoTime();
  CarBrand carBrandFromHibernate = vehicleRepository.findCarBrandById(vehicleId)
                                                    .getCarBrand();
  long diffHibernate = System.nanoTime() - startHibernate;
  System.out.println(diffRawDao + " Raw Dao");
  System.out.println(diffHibernate + " Hibernate");
  System.out.println((diffHibernate - diffRawDao) + " diff RawDao and Hibernate");
  assertEquals(carBrandFromRawDao, carBrandFromHibernate);
}
```
- Реализация репозитория с Hibernate 
```java
public interface VehicleRepository
    extends CrudRepository<Vehicle, Long>,
            PagingAndSortingRepository<Vehicle, Long> {

  public Vehicle findCarBrandById(Long id);

}
```
- На JdbcTemplate
```java
@Repository
public class VehicleDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public VehicleDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public CarBrand findCarBrandById(Long id) {
    String sql = "SELECT cb.* FROM Car_Brand cb INNER JOIN Vehicle v ON cb.id = v.car_Brand_id WHERE v.id = ?";

    RowMapper<CarBrand> rowMapper = (rs, rowNum) -> {
      CarBrand carBrand = new CarBrand();
      carBrand.setId(rs.getLong("id"));
      carBrand.setName(rs.getString("name"));
      carBrand.setCarType(rs.getString("car_type"));
      carBrand.setAmountOfPlaces(rs.getInt("amount_of_places"));
      carBrand.setCarryingCapacity(rs.getInt("carrying_capacity"));
      carBrand.setVolumeOfTank(rs.getInt("volume_of_tank"));
      return carBrand;
    };

    return jdbcTemplate.queryForObject(sql, rowMapper, id);
  }
}
```

3.	Получение списка водителей машины по ее id. (2 таблицы).
```
23170497 Raw Dao
73155087 Hibernate
49984590 diff RawDao and Hibernate
```
- Hibernate
```java
public interface VehicleRepository
    extends CrudRepository<Vehicle, Long>,
            PagingAndSortingRepository<Vehicle, Long> {

  public Iterable<Vehicle> findAllByEnterpriseId(Long id);

  public Vehicle findCarBrandById(Long id);

}
```
- JdbcTemplate
```java
public List<Driver> findDriversByVehicleId(Long id) {
  String sql = "SELECT * FROM Driver WHERE vehicle_id = ?";

  RowMapper<Driver> rowMapper = new RowMapper<Driver>() {
    @Override
    public Driver mapRow(ResultSet rs,
                         int rowNum) throws
        SQLException {
      Driver driver = new Driver();
      driver.setId(rs.getLong("id"));
      driver.setName(rs.getString("name"));
      driver.setSalary(rs.getInt("salary"));
      driver.setWorkingTurn(rs.getBoolean("is_Working_Turn"));
      return driver;
    }
  };

  return jdbcTemplate.query(sql, rowMapper, id);
}
```
```java
@Test
void testFindDriversByVehicleId() {
  Long vehicleId = enterpriseGenerator.getRandomVehicle()
                                      .getId();
  long startRawDao = System.nanoTime();
  List<Driver> driversFromRawDao = vehicleDao.findDriversByVehicleId(vehicleId);
  long diffRawDao = System.nanoTime() - startRawDao;

  long startHibernate = System.nanoTime();
  List<Driver> driversFromHibernate = vehicleRepository.findCarBrandById(vehicleId)
                                                    .getDrivers();
  long diffHibernate = System.nanoTime() - startHibernate;
  System.out.println(diffRawDao + " Raw Dao");
  System.out.println(diffHibernate + " Hibernate");
  System.out.println((diffHibernate - diffRawDao) + " diff RawDao and Hibernate");
  assertEquals(driversFromRawDao, driversFromHibernate);
}
```

## Итог 
Даже на совсем тривиальном кейсе вариант с RawQuery окахался заметно быстрее. Хотел бы сказать, что hibernate еще можно использовать для mvp, но фактически сейчас в качестве QBS можно использовать GPT и генерирова им код будет даже проще, чем Hibernate. Но надо проверять правильность и возможно ограничение в кол-ве запросов, так что дешевле будет для MVP все таки использовать hibernate, но в этом я сомневаюсь.
