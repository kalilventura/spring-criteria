package br.com.github.kalilventura.criteria;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeCriteriaRepository {
    Employee save(Employee employee);
    List<Employee> findByFirstNameAndSurname(String name, String surname);
    List<Employee> findByFirstName(String name);
    List<Employee> findByCriteria(String name, String surname, String department);
}
