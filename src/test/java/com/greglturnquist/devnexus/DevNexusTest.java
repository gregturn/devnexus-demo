/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.devnexus;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DevNexusTest {

	@Test
	public void test() {
		// Test fails without reaching a single statement.
	}

	@Configuration
	@EnableJpaRepositories
	@ComponentScan(basePackageClasses = {DevNexusTest.class})
	static class Config {

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

		protected Employee() {}

		public Employee(String name, String role) {
			this.name = name;
			this.role = role;
		}
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

		protected Manager() {}

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

	interface ManagerRepository extends CrudRepository<Manager, Long> {
		List<Manager> findByEmployeesRoleContaining(@Param("search") String search);
	}

}
