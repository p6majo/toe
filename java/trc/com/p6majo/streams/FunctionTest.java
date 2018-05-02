package com.p6majo.streams;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionTest {

    Function<Integer,String> half = new Function<Integer,String>(){

        @Override
        public String apply(Integer integer) {
            return integer/2.+" ";
        }
    };


    public class Employee{
        private String name;
        private int age;
        public Employee(String name,int age){
            this.name=name;
            this.age=age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name){
            this.name = name;
        }
    }

    //import statements are same as in apply() example
    @Test
    public void andThenExample(){
            Function<Employee, String> funcEmpToString= (Employee e)-> {return e.getName();};
            List<Employee> employeeList=
                    Arrays.asList(new Employee("Tom Jones", 45),
                            new Employee("Harry Major", 25),
                            new Employee("Ethan Hardy", 65),
                            new Employee("Nancy Smith", 15),
                            new Employee("Deborah Sprightly", 29));

            Function<String,String> initialFunction= (String s)->s.substring(0,1);


            List<String> empNameListInitials=convertEmpListToNamesList(employeeList, funcEmpToString.andThen(initialFunction));
            empNameListInitials.forEach(str->{System.out.print(" "+str);});
    }



    private static List<String> convertEmpListToNamesList(List<Employee> employeeList, Function<Employee, String> funcEmpToString){
        List<String> empNameList=new ArrayList<String>();
        for(Employee emp:employeeList){
            empNameList.add(funcEmpToString.apply(emp));
        }
        return empNameList;
    }


    @Test  //compose test
    public void composeTest(){
            Function<Employee, String> funcEmpToString= (Employee e)-> {return e.getName();};
            Function<Employee, Employee> funcEmpFirstName=
                    (Employee e)-> {int index= e.getName().indexOf(" ");
                        String firstName=e.getName().substring(0,index);
                        e.setName(firstName);
                        return e;};
            List<Employee> employeeList=
                    Arrays.asList(new Employee("Tom Jones", 45),
                            new Employee("Harry Major", 25),
                            new Employee("Ethan Hardy", 65),
                            new Employee("Nancy Smith", 15),
                            new Employee("Deborah Sprightly", 29));
            List<String> empFirstNameList= convertEmpListToNamesList(employeeList,funcEmpToString.compose(funcEmpFirstName));
            empFirstNameList.forEach(str->{System.out.print(" "+str);});
    }



}
