package br.com.github.kalilventura.criteria;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final IEmployeeCriteriaRepository employeeRepository;

    public EmployeeController(IEmployeeCriteriaRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Employee> post(@RequestBody Employee employee) {
        var addedEmployee = this.employeeRepository.save(employee);
        return ResponseEntity.ok().body(addedEmployee);
    }

    @GetMapping("/find")
    public ResponseEntity<List<Employee>> findByFirstName(@RequestParam(name = "firstname", required = false) String firstname,
                                                          @RequestParam(name = "lastname", required = false) String lastname,
                                                          @RequestParam(name = "department", required = false) String department) {

        var employees = employeeRepository.findByCriteria(firstname, lastname, department);
        return ResponseEntity.ok().body(employees);
    }
}
