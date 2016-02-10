package com.greglturnquist.devnexus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@SpringBootApplication
public class DevnexusApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevnexusApplication.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository employeeRepository,
			ManagerRepository managerRepository) {
		return (String) -> {
			Manager gandalf = managerRepository.save(new Manager("Gandalf"));

			Employee frodo = employeeRepository
					.save(new Employee("Frodo Baggins", "ring bearer"));
			Employee bilbo = employeeRepository
					.save(new Employee("Bilbo Baggins", "burglar"));

			frodo.setManager(gandalf);
			bilbo.setManager(gandalf);
			employeeRepository.save(Arrays.asList(frodo, bilbo));
		};
	}

}

@Data
@Entity
class Employee {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String role;

	@ManyToOne
	private Manager manager;

	protected Employee() {
		/* Thanks JPA! */}

	public Employee(String name, String role) {
		this.name = name;
		this.role = role;
	}
}

@RepositoryRestResource
interface EmployeeRepository extends CrudRepository<Employee, Long> {

	List<Employee> findByManagerName(@Param("name") String name);

	List<Employee> findByRoleContaining(@Param("search") String search);

}

@Data
@Entity
class Manager {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@OneToMany(mappedBy = "manager")
	private List<Employee> employees;

	protected Manager() {
	};

	public Manager(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		if (this.employees == null) {
			this.employees = new ArrayList<>();
		}
		return this.employees;
	}
}

@RepositoryRestResource
interface ManagerRepository extends CrudRepository<Manager, Long> {

	List<Manager> findByEmployeesRoleContaining(@Param("search") String search);

}