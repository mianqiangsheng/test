package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lizhen on 2018/10/25.
 */
class Person{

    List<String> toys;

    public Person(List<String> toys) {
        this.toys = toys;
    }

    @Override
    public String toString() {
        return "Person{" +
                "toys=" + Arrays.toString(toys.toArray()) +
                '}';
    }
}


@SpringBootApplication
public class Test2 {

    @Bean
    String str1(){
        return "a";
    }

    @Bean
    String str2(){
        return "b";
    }

    @Bean
    Person person(List<String> list){
        return new Person(list);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Test2.class);
        Person person = context.getBean(Person.class);
        System.out.println(person);
    }
}
