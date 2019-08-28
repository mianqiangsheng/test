package java8Optional去除非空校验;

import java.util.Optional;

/**
 * @author Li Zhen
 * @create 2019/8/14
 * @since 1.0.0
 */

public class Test {

    public static void main(String[] args) {

        /**
         *
         * 使用java8提供的Optional & 对象类设计(isNull标识当前对象是否为空对象、对象属性默认是空对象实例)
         * 实现对对象中的递归对象get时避免null判断；
         *
         */
        Person person = new Person();

        Car car = person.getCar().get();
        System.out.println(car);

        Person person1 = new Person(Optional.of(new Car()));
        Car car1 = person1.getCar().get();
        System.out.println(car1);

        Insurance insurance = person.getCar().get().getInsurance().get();
        System.out.println(insurance);

        String name = person.getCar().get().getInsurance().get().getName();
        System.out.println(name);

    }

}

class Person
{
    private Boolean isNull = false;
    private Optional<Car> car = Optional.of(Car.nullCar());

    public Person() {
    }

    public Person(Optional<Car> car) {
        this.car = car;
    }

    public Person(Optional<Car> car, Boolean isNull){
        this.car = car;
        this.isNull = isNull;
    }

    static Person nullPerson(){
        return new Person(Optional.of(Car.nullCar()),true);
    }

    public Optional<Car> getCar()
    {
        return car;
    }

    @Override
    public String toString() {
        return "Person[" + car.get() + "],isNull=" + isNull;
    }
}

class Car
{
    private Boolean isNull = false;
    private Optional<Insurance> insurance = Optional.of(Insurance.nullInsurance);

    public Car() {
    }

    public Car(Optional<Insurance> insurance) {
        this.insurance = insurance;
    }

    public Car(Optional<Insurance> insurance, Boolean isNull) {
        this.insurance = insurance;
        this.isNull = isNull;
    }

    static Car nullCar(){
        return new Car(Optional.of(Insurance.nullInsurance),true);
    }

    public Optional<Insurance> getInsurance()
    {
        return insurance;
    }

    @Override
    public String toString() {
        return "Car[" + insurance.get() + "],isNull=" + isNull;
    }
}

class Insurance
{
    static Insurance nullInsurance = new Insurance("null");

    public Insurance() {
    }

    public Insurance(String name) {
        this.name = name;
    }

    private String name;

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return "Insurance[" + name + "]";
    }
}