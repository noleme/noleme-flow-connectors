package com.noleme.flow.connect.commons.yaml;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noleme.flow.Flow;
import com.noleme.flow.connect.commons.FlowYaml;
import com.noleme.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 04/04/2022
 */
public class FlowYamlTest
{
    static {
        Json.mapper().configure(FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testInstanceToYaml()
    {
        Assertions.assertDoesNotThrow(() -> {
            var person = new Person("SomeOne", "Maybe", 23, 45.6d, 56.7f);
            var flow = Flow
                .from(() -> person)
                .pipe(FlowYaml.instanceToYaml())
                .collect()
            ;
            String output = Flow.runAsPipeline(flow).get(flow);

            var yaml = "name: \"SomeOne\"\n"
                     + "surname: \"Maybe\"\n"
                     + "age: 23\n"
                     + "height: 45.6\n"
                     + "weight: 56.7\n"
            ;
            Assertions.assertEquals(yaml, output);
        });
    }

    @Test
    public void testYamlToInstance()
    {
        Assertions.assertDoesNotThrow(() -> {
            var yaml = "name: \"SomeOne\"\n"
                     + "surname: \"Maybe\"\n"
                     + "age: 23\n"
                     + "height: 45.6\n"
                     + "weight: 56.7\n"
            ;
            var flow = Flow
                .from(() -> yaml)
                .pipe(FlowYaml.yamlToInstance(Person.class))
                .collect()
            ;
            Person output = Flow.runAsPipeline(flow).get(flow);

            var person = new Person("SomeOne", "Maybe", 23, 45.6d, 56.7f);
            Assertions.assertEquals(person.name(), output.name());
            Assertions.assertEquals(person.surname(), output.surname());
            Assertions.assertEquals(person.age(), output.age());
            Assertions.assertEquals(person.height(), output.height());
            Assertions.assertEquals(person.weight(), output.weight());
        });
    }

    @Test
    public void testYamlListToInstance()
    {
        Assertions.assertDoesNotThrow(() -> {
            var yaml = "- name: \"SomeOne\"\n"
                     + "  surname: \"Maybe\"\n"
                     + "  age: 23\n"
                     + "  height: 45.6\n"
                     + "  weight: 56.7\n"
                     + "- name: \"SomeOther\"\n"
                     + "  surname: \"Surely\"\n"
                     + "  age: 34\n"
                     + "  height: 56.7\n"
                     + "  weight: 89.0\n"
            ;
            var flow = Flow
                .from(() -> yaml)
                .pipe(FlowYaml.yamlToList(Person.class))
                .collect()
            ;
            List<Person> output = Flow.runAsPipeline(flow).get(flow);

            var person1 = new Person("SomeOne", "Maybe", 23, 45.6d, 56.7f);
            var person2 = new Person("SomeOther", "Surely", 34, 56.7d, 89.0f);
            Assertions.assertEquals(2, output.size());
            Assertions.assertEquals(person1.name(), output.get(0).name());
            Assertions.assertEquals(person2.name(), output.get(1).name());
        });
    }

    private static class Person
    {
        private final String name;
        private final String surname;
        private final int age;
        private final double height;
        private final float weight;

        public Person(
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("age") int age,
            @JsonProperty("height") double height,
            @JsonProperty("weight") float weight
        )
        {
            this.name = name;
            this.surname = surname;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }

        @JsonGetter public String name()    { return name; }
        @JsonGetter public String surname() { return surname; }
        @JsonGetter public int age()        { return age; }
        @JsonGetter public double height()  { return height; }
        @JsonGetter public float weight()   { return weight; }
    }
}
