package br.com.github.kalilventura.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class EmployeeCriteriaRepository implements IEmployeeCriteriaRepository {
    private final Logger logger = LoggerFactory.getLogger(EmployeeCriteriaRepository.class);

    private final IEmployeeRepository employeeRepository;
    private final EntityManager entityManager;

    public EmployeeCriteriaRepository(IEmployeeRepository employeeRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findByFirstNameAndSurname(String name, String surname) {
        try {
            logger.info("Trying to find employees with name: {} and surname: {}", name, surname);
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            Predicate firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), name);
            Predicate surnamePredicate = criteriaBuilder.like(root.get("surname"), "%" + surname + "%");

            criteriaQuery.where(firstNamePredicate, surnamePredicate);

            TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);

            return query.getResultList();
        } catch (Exception e) {
            logger.error("Something has happened {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public List<Employee> findByFirstName(String name) {
        try {
            logger.info("Trying to find employees with name: {}", name);
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            Predicate firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), name);

            criteriaQuery.where(firstNamePredicate);

            TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Something has happened {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public List<Employee> findByCriteria(String name, String surname, String department) {
        try {
            Collection<Predicate> predicates = new ArrayList<>();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            if (name != null && !name.isEmpty()) {
                Predicate firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), name);
                predicates.add(firstNamePredicate);
            }
            if (surname != null && !surname.isEmpty()) {
                Predicate surnamePredicate = criteriaBuilder.like(root.get("surname"), "%" + surname + "%");
                predicates.add(surnamePredicate);
            }
            if (department != null && !department.isEmpty()) {
                Predicate departmentPredicate = criteriaBuilder.equal(root.get("department"), department);
                predicates.add(departmentPredicate);
            }

            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            criteriaQuery.where(finalPredicate);

            TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Something has happened {}", e.getLocalizedMessage());
            throw e;
        }
    }
}
